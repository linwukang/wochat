package com.lwk.wochat.api.data.redis.value;

public interface RedisCounterValue<K, V> extends RedisValue<K, V> {
    boolean isZero();
    void makeZero();
    long value();
    long increment();
    long increment(long delta);
    long decrement();
    long decrement(long delta);
}
