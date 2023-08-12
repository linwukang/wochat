package com.lwk.wochat.api.data.redis.jedis;

import com.lwk.wochat.api.data.redis.RedisMap;
import com.lwk.wochat.api.data.redis.jedis.utils.LuaScripts;
import com.lwk.wochat.api.data.redis.jedis.value.JedisHashValue;
import com.lwk.wochat.api.data.redis.jedis.value.JedisListValue;
import com.lwk.wochat.api.data.redis.jedis.value.JedisSetValue;
import com.lwk.wochat.api.data.redis.jedis.value.JedisSortedSetValue;
import com.lwk.wochat.api.data.redis.value.RedisHashValue;
import com.lwk.wochat.api.data.redis.value.RedisListValue;
import com.lwk.wochat.api.data.redis.value.RedisSetValue;
import com.lwk.wochat.api.data.redis.value.RedisSortedSetValue;
import com.lwk.wochat.api.data.serialization.Serializer;
import redis.clients.jedis.Jedis;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class JedisMap<K, V> implements RedisMap<K, V> {
    private final Jedis jedis;
    private final K separator;
    private final K keyPrefix;

//    private final byte[] separatorBytes;
    private final String separatorString;
    //    private final byte[] keyPrefixBytes;
    private final String keyPrefixString;

    private final Serializer<K> keySerializer;
    private final Serializer<V> valueSerializer;

    public final Charset keyEncoding;
    public final Charset valueEncoding;

    private final String prefix;


    public JedisMap(
            Jedis jedis,
            K separator,
            K keyPrefix,
            Serializer<K> keySerializer,
            Serializer<V> valueSerializer,
            Charset keyEncoding,
            Charset valueEncoding) {
        this.jedis = jedis;
        this.separator = separator;
        this.keyPrefix = keyPrefix;
//        this.separatorBytes = keySerializer.serialize(separator);
        this.separatorString = new String(keySerializer.serialize(separator), keyEncoding);
//        this.keyPrefixBytes = keySerializer.serialize(keyPrefix);
        this.keyPrefixString = new String(keySerializer.serialize(keyPrefix), keyEncoding);
        this.keySerializer = keySerializer;
        this.valueSerializer = valueSerializer;
        this.keyEncoding = keyEncoding;
        this.valueEncoding = valueEncoding;

        prefix = keyPrefixString + separatorString;
    }

    public JedisMap(
            Jedis jedis,
            K separator,
            K keyPrefix,
            Serializer<K> keySerializer,
            Serializer<V> valueSerializer,
            Charset encoding) {
        this(jedis, separator, keyPrefix, keySerializer, valueSerializer, encoding, encoding);
    }

    public JedisMap(
            Jedis jedis,
            K separator,
            K keyPrefix,
            Serializer<K> keySerializer,
            Serializer<V> valueSerializer) {
        this(jedis, separator, keyPrefix, keySerializer, valueSerializer, StandardCharsets.UTF_8);
    }

    public String keyToString(K key) {
        return new String(
                keySerializer().serialize(key),
                keyEncoding);
    }

    public K concatKey(K key1, K key2) {
        byte[] keyBytes1 = keySerializer.serialize(key1);
        byte[] keyBytes2 = keySerializer.serialize(key2);
        byte[] result = new byte[keyBytes1.length + keyBytes2.length];
        System.arraycopy(keyBytes1, 0, result, 0, keyBytes1.length);
        System.arraycopy(keyBytes2, 0, result, keyBytes1.length, keyBytes2.length);

        return keySerializer.deserialize(result);
    }

    public String valueToString(V value) {
        return new String(
                valueSerializer().serialize(value),
                valueEncoding);
    }

    public K stringToKey(String keyString) {
        if (keyString == null || keyString.isEmpty()) {
            return null;
        }

        return keySerializer.deserialize(keyString.getBytes(keyEncoding));
    }

    public V stringToValue(String valueString) {
        if (valueString == null || valueString.isEmpty()) {
            return null;
        }

        return valueSerializer().deserialize(valueString.getBytes(valueEncoding));
    }

    public String fullKey(K key) {
        return prefix +
                new String(keySerializer().serialize(key), keyEncoding);
    }

    public String rawKey(K fullKey) {
        if (fullKey == null) {
            throw new NullPointerException();
        }

        byte[] keyBytes = keySerializer().serialize(fullKey);
        int prefixLength = keyPrefixString.getBytes(keyEncoding).length
                + separatorString.getBytes(keyEncoding).length;

        if (keyBytes.length < prefixLength) {
            throw new IllegalArgumentException();
        }

        if (prefixLength == 0) {
            return new String(keyBytes, keyEncoding);
        }

        return new String(
                keyBytes,
                prefixLength,
                keyBytes.length - prefixLength,
                keyEncoding);
    }

    public Jedis jedis() {
        return jedis;
    }

    @Override
    public K getSeparator() {
        return separator;
    }

    @Override
    public K getKeyPrefix() {
        return keyPrefix;
    }

    @Override
    public Serializer<K> keySerializer() {
        return keySerializer;
    }

    @Override
    public Serializer<V> valueSerializer() {
        return valueSerializer;
    }

    @Override
    public V put(K key, V value, Duration ttl) {
        jedis.psetex(fullKey(key), ttl.toMillis(), valueToString(value));
        return value;
    }

    @Override
    public boolean expire(K key, Duration ttl) {
        long result = jedis.pexpire(fullKey(key), ttl.toMillis());
        return result == 1;
    }

    @Override
    public boolean expire(K key, long ttl) {
        long result = jedis.pexpire(fullKey(key), ttl);
        return result == 1;
    }

    @Override
    public RedisMap<K, V> of(K keyPrefix) {

        return new JedisMap<>(
                jedis,
                separator,
                concatKey(concatKey(this.keyPrefix, separator), keyPrefix),
                keySerializer,
                valueSerializer,
                keyEncoding,
                valueEncoding);
    }

    @Override
    public RedisListValue<K, V> listOf(K key) {
        return new JedisListValue<>(this, key);
    }

    @Override
    public RedisHashValue<K, V> hashOf(K key) {
        return new JedisHashValue<>(this, key);
    }

    @Override
    public RedisSetValue<K, V> setOf(K key) {
        return new JedisSetValue<>(this, key);
    }

    @Override
    public RedisSortedSetValue<K, V> sortedSetOf(K key, Function<? super V, Double> scorer) {
        return new JedisSortedSetValue<>(this, key, scorer);
    }

//    @Override
//    public Pairs<K, V> pairsOf(K key) {
//        return null;
//    }

    @Override
    public int size() {
        return jedis
                .keys(prefix + "*")
                .size();
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        try {
            return jedis.keys(fullKey((K) key)).size() == 1;
        }
        catch (ClassCastException e) {
            return false;
        }
    }

    @Override
    @Deprecated
    public boolean containsValue(Object value) {
        return keySet()
                .stream()
                .map(k -> jedis.get(fullKey(k)))
                .map(v -> valueSerializer().deserialize(v.getBytes()))
                .anyMatch(v -> Objects.equals(v, value));
    }

    @Override
    public V get(Object key) {
        try {
            String valueString = jedis.get(fullKey((K) key));
            return valueString == null
                    ? null
                    : stringToValue(valueString);
        }
        catch (ClassCastException e) {
            return null;
        }
    }

    @Override
    public V put(K key, V value) {
        jedis.set(fullKey(key), valueToString(value));

        return value;
    }

    @Override
    public V remove(Object key) {
        try {
            String fullKey = fullKey((K) key);

            String valueString = (String) jedis.eval(LuaScripts.REDIS_REMOVE, 1, fullKey);
            return valueString == null
                    ? null
                    : stringToValue(valueString);
        }
        catch (ClassCastException e) {
            return null;
        }

    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        if (m == null || m.isEmpty()) {
            return;
        }

        String[] kv = new String[m.size() * 2 + 1];
        kv[0] = Integer.toString(m.size() * 2);
        int i = 1;
        for (Entry<? extends K, ? extends V> entry : m.entrySet()) {
            K k = entry.getKey();
            V v = entry.getValue();
            kv[i] = fullKey(k);
            kv[i + 1] = valueToString(v);
            i += 2;
        }

        jedis.eval(
                LuaScripts.REDIS_PUT_ALL,
                0,
                kv
        );
    }

    @Override
    public void clear() {
        jedis.eval(LuaScripts.REDIS_CLEAR, 0, prefix);
    }

    @Override
    public Set<K> keySet() {
        return jedis
                .keys(prefix + "*")
                .stream()
                .map(this::stringToKey)
                .map(this::rawKey)
                .map(this::stringToKey)
                .collect(Collectors.toSet());
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<V> values() {
        return new HashSet<>(((List<V>) jedis.eval(LuaScripts.REDIS_VALUES, 0, prefix)));
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return jedis
                .keys(prefix + "*")
                .stream()
                .map(k -> new Entry<K, V>() {
                    private final K key = stringToKey(k);
                    private V value = stringToValue(jedis.get(k));
                    @Override
                    public K getKey() {
                        return key;
                    }

                    @Override
                    public V getValue() {
                        return value;
                    }

                    @Override
                    public V setValue(V value) {
                        put(key, value);
                        this.value = value;
                        return value;
                    }
                })
                .collect(Collectors.toSet());
    }
}
