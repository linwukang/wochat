package com.lwk.wochat.api.data.redis.jedis.value;

import com.lwk.wochat.api.data.redis.RedisMap;
import com.lwk.wochat.api.data.redis.jedis.JedisMap;
import com.lwk.wochat.api.data.redis.value.RedisSortedSetValue;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JedisSortedSetValue<K, V> implements RedisSortedSetValue<K, V> {
    private final JedisMap<K, V> redisMap;
    private final K key;
    private final String fullKeyString;
    private final Jedis jedis;
    private final Function<? super V, Double> scorer;

    public JedisSortedSetValue(JedisMap<K, V> redisMap, K key, Function<? super V, Double> scorer) {
        this.redisMap = redisMap;
        this.key = key;
        this.scorer = scorer;

        fullKeyString = redisMap.fullKey(key);
        jedis = redisMap.jedis();
    }

    @Override
    public int size() {
        return Math.toIntExact(jedis.scard(fullKeyString));
    }

    @Override
    public boolean isEmpty() {
        return size() > 0;
    }

    @Override
    public boolean contains(Object o) {
        return null != jedis.zscore(
                fullKeyString,
                redisMap.valueToString((V) o));
    }

    @Override
    public Iterator<V> iterator() {
        return jedis
                .zrange(fullKeyString, 0, -1)
                .stream()
                .map(redisMap::stringToValue)
                .iterator();
    }

    @Override
    public Object[] toArray() {
        return jedis
                .zrange(fullKeyString, 0, -1)
                .toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return new ArrayList<>(jedis.zrange(fullKeyString, 0, -1))
                .toArray(a);
    }

    @Override
    public boolean add(V v) {
        jedis.zadd(fullKeyString, scorer.apply(v), redisMap.valueToString(v));
        return true;
    }

    @Override
    public boolean remove(Object o) {
        long result = jedis.zrem(
                fullKeyString,
                redisMap.valueToString((V) o));
        return result == 1;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return c
                .stream()
                .allMatch(this::contains);
    }

    @Override
    public boolean addAll(Collection<? extends V> c) {
        jedis.zadd(
                fullKeyString,
                c.stream().collect(Collectors.toMap(
                        redisMap::valueToString,
                        scorer)));
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        String[] array = (String[]) Stream.concat(
                        Stream.of(fullKeyString, String.valueOf(c.size())),
                        c
                                .stream()
                                .map(v -> (V) v)
                                .map(redisMap::valueToString))
                .toArray();

        String luaScript = "local c = {}" +
                "for i = 2, ARGV[1], 1 do\n" +
                "    table.insert(c, ARGV[i])" +
                "end" +
                "local members = redis.call('ZRANGE', KEYS[1], 0, -1)" +
                "for _, member in ipairs(members) do\n" +
                "if 'nil' != redis.call('ZSCORE', c, member) then\n" +
                "        redis.call('ZREM', KEYS[1], member)\n" +
                "    end\n" +
                "end\n";

        jedis.eval(luaScript, 1, array);

        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        String[] array = (String[]) Stream.concat(
                        Stream.of(fullKeyString, String.valueOf(c.size())),
                        c
                                .stream()
                                .map(v -> (V) v)
                                .map(redisMap::valueToString))
                .toArray();
        String luaScript = "for i = 2, ARGV[1], 1 do\n" +
                "    redis.call('ZREM', KEYS[i], ARGV[i])" +
                "end\n";

        jedis.eval(luaScript, 1, array);
        return true;
    }

    @Override
    public void clear() {
        jedis.del(fullKeyString);
    }

    @Override
    public RedisMap<K, V> redis() {
        return redisMap;
    }
}
