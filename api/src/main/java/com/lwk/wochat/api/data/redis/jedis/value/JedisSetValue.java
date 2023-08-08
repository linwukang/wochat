package com.lwk.wochat.api.data.redis.jedis.value;

import com.lwk.wochat.api.data.redis.RedisMap;
import com.lwk.wochat.api.data.redis.jedis.JedisMap;
import com.lwk.wochat.api.data.redis.value.RedisSetValue;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Stream;

public class JedisSetValue<K, V> implements RedisSetValue<K, V> {
    private final JedisMap<K, V> redisMap;
    private final K key;
    private final String fullKeyString;
    private final Jedis jedis;

    public JedisSetValue(JedisMap<K, V> redisMap, K key) {
        this.redisMap = redisMap;
        this.key = key;

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
        return jedis.sismember(
                fullKeyString,
                redisMap.valueToString((V) o));
    }

    @Override
    public Iterator<V> iterator() {
        return jedis
                .smembers(fullKeyString)
                .stream()
                .map(redisMap::stringToValue)
                .iterator();
    }

    @Override
    public Object[] toArray() {
        return jedis
                .smembers(fullKeyString)
                .toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return new ArrayList<>(jedis
                .smembers(fullKeyString))
                .toArray(a);
    }

    @Override
    public boolean add(V v) {
        jedis.sadd(fullKeyString, redisMap.valueToString(v));
        return true;
    }

    @Override
    public boolean remove(Object o) {
        long result = jedis.srem(
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
        jedis.sadd(
                fullKeyString,
                (String[]) c.stream()
                        .map(redisMap::valueToString)
                        .toArray());
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
                "local members = redis.call('SMEMBERS', KEYS[1])" +
                "for _, member in ipairs(members) do\n" +
                "if not redis.call('SISMEMBER', c, member) then\n" +
                "        redis.call('SREM', KEYS[1], member)\n" +
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
                "    redis.call('SREM', KEYS[i], ARGV[i])" +
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
