package com.lwk.wochat.api.data.serialization;

public interface Serializer<T> {
    byte[] serialize(T object);

    T deserialize(byte[] serialized);

    default T deserializeString(String serialized) {
        return deserialize(serialized.getBytes());
    }
}
