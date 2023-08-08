package com.lwk.wochat.api.data.redis.jedis.value;

import com.lwk.wochat.api.data.redis.RedisMap;
import com.lwk.wochat.api.data.redis.jedis.JedisMap;
import com.lwk.wochat.api.data.redis.jedis.utils.LuaScripts;
import com.lwk.wochat.api.data.redis.jedis.utils.Util;
import com.lwk.wochat.api.data.redis.value.RedisListValue;
import redis.clients.jedis.Jedis;

import java.util.*;
import java.util.stream.Collectors;

public class JedisListValue<K, V> implements RedisListValue<K, V> {
    private final JedisMap<K, V> redisMap;
    private final K key;
    private final String fullKeyString;
    private final Jedis jedis;

    public JedisListValue(JedisMap<K, V> redisMap, K key) {
        this.redisMap = redisMap;
        this.key = key;

        fullKeyString = redisMap.fullKey(key);
        jedis = redisMap.jedis();
    }

    @Override
    public void addFirst(V v) {
        jedis.lpush(fullKeyString, redisMap.valueToString(v));
    }

    @Override
    public void addLast(V v) {
        jedis.rpush(fullKeyString, redisMap.valueToString(v));
    }

    @Override
    public boolean offerFirst(V v) {
        addFirst(v);
        return true;
    }

    @Override
    public boolean offerLast(V v) {
        addLast(v);
        return true;
    }

    @Override
    public V removeFirst() {
        String valueString = jedis.lpop(fullKeyString);
        return redisMap.stringToValue(valueString);
    }

    @Override
    public V removeLast() {
        String valueString = jedis.rpop(fullKeyString);
        return redisMap.stringToValue(valueString);
    }

    @Override
    public V pollFirst() {
        if (isEmpty()) {
            return null;
        }

        String valueString = jedis.lpop(fullKeyString);
        return redisMap.stringToValue(valueString);
    }

    @Override
    public V pollLast() {
        if (isEmpty()) {
            return null;
        }

        String valueString = jedis.rpop(fullKeyString);
        return redisMap.stringToValue(valueString);
    }

    @Override
    public V getFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        String valueString = jedis.lindex(fullKeyString, 0);

        return redisMap.stringToValue(valueString);
    }

    @Override
    public V getLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        String valueString = jedis.lindex(fullKeyString, -1);

        return redisMap.stringToValue(valueString);
    }

    @Override
    public V peekFirst() {
        if (isEmpty()) {
            return null;
        }

        String valueString = jedis.lindex(fullKeyString, 0);

        return redisMap.stringToValue(valueString);
    }

    @Override
    public V peekLast() {
        if (isEmpty()) {
            return null;
        }

        String valueString = jedis.lindex(fullKeyString, -1);

        return redisMap.stringToValue(valueString);
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        long count = jedis.lrem(
                fullKeyString,
                1,
                redisMap.valueToString((V) o));

        return count == 1;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        long count = jedis.lrem(
                fullKeyString,
                -1,
                redisMap.valueToString((V) o));

        return count == 1;
    }

    @Override
    public boolean add(V v) {
        push(v);
        return true;
    }

    @Override
    public boolean offer(V v) {
        return add(v);
    }

    @Override
    public V remove() {
        return removeFirst();
    }

    @Override
    public V poll() {
        return pollFirst();
    }

    @Override
    public V element() {
        return getFirst();
    }

    @Override
    public V peek() {
        return peekFirst();
    }

    @Override
    public boolean addAll(Collection<? extends V> c) {
        jedis.rpush(
                fullKeyString,
                (String[]) c.stream()
                        .map(redisMap::valueToString)
                        .toArray());
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
//        c.forEach(this::remove);
        jedis.eval(LuaScripts.REDIS_REMOVE_ALL,
                List.of(fullKeyString),
                Util.toStringList(c, redisMap.valueSerializer(), redisMap.valueEncoding));
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        jedis.eval(LuaScripts.REDIS_RETAIN_ALL,
                List.of(fullKeyString),
                Util.toStringList(c, redisMap.valueSerializer(), redisMap.valueEncoding));
        return true;
    }

    @Override
    public void clear() {
        jedis.del(fullKeyString);
    }

    @Override
    public void push(V v) {
        addLast(v);
    }

    @Override
    public V pop() {
        return removeLast();
    }

    @Override
    public boolean remove(Object o) {
        return removeFirstOccurrence(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return c
                .stream()
                .allMatch(this::contains);
    }

    @Override
    public boolean contains(Object o) {
        Long pos = jedis.lpos(fullKeyString, redisMap.valueToString((V) o));
        return pos != null && pos >= 0;
    }

    @Override
    public int size() {
        return Math.toIntExact(jedis.llen(fullKeyString));
    }

    @Override
    public boolean isEmpty() {
        return !jedis.exists(fullKeyString) || jedis.llen(fullKeyString) == 0;
    }

    @Override
    public Iterator<V> iterator() {
        return jedis
                .lrange(fullKeyString, 0, -1)
                .stream()
                .map(redisMap::stringToValue)
                .iterator();
    }

    @Override
    public Object[] toArray() {
        return jedis
                .lrange(fullKeyString, 0, -1)
                .stream()
                .map(redisMap::stringToValue)
                .toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return jedis
                .lrange(fullKeyString, 0, -1)
                .stream()
                .map(redisMap::stringToValue)
                .map(v -> (T) v)
                .collect(Collectors.toList())
                .toArray(a);
    }

    @Override
    public Iterator<V> descendingIterator() {
        List<V> list = jedis
                .lrange(fullKeyString, 0, -1)
                .stream()
                .map(redisMap::stringToValue)
                .collect(Collectors.toList());
        Collections.reverse(list);
        return list.iterator();
    }

    @Override
    public RedisMap<K, V> redis() {
        return redisMap;
    }

    @Override
    public V get(int index) {
        return redisMap.stringToValue(jedis.lindex(fullKeyString, index));
    }

    @Override
    public V set(int index, V element) {
        if (index >= size() || index < -size()) {
            throw new IndexOutOfBoundsException();
        }

        V previouslyValue = get(index);

        jedis.lset(fullKeyString, index, redisMap.valueToString(element));

        return previouslyValue;
    }
}
