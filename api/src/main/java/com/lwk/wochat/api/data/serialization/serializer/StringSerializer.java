package com.lwk.wochat.api.data.serialization.serializer;


import com.lwk.wochat.api.data.serialization.Serializer;

/**
 * 用于字符串的简单序列化器
 */
public class StringSerializer implements Serializer<String> {
    @Override
    public byte[] serialize(String object) {
        return object.getBytes();
    }

    @Override
    public String deserialize(byte[] serialized) {
        return new String(serialized);
    }
}
