package com.lwk.wochat.redis_service.configuration;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Redis Key 的序列化方式
 * 为每个 key 都加上一个前缀 "wochat:"
 */
public class RedisKeyPrefixSerializer implements RedisSerializer<String> {
    /**
     * 字符集
     */
    private final Charset charset;
    /**
     * 前缀
     */
    private final String PREFIX_KEY = "wochat:";

    public RedisKeyPrefixSerializer() {
        this(StandardCharsets.UTF_8);
    }

    public RedisKeyPrefixSerializer(Charset charset) {
        this.charset = charset;
    }

    /**
     * 序列化
     * @param s
     * @return
     * @throws SerializationException
     */
    @Override
    public byte[] serialize(String s) throws SerializationException {
        String key = PREFIX_KEY + s;
        return key.getBytes(charset);
    }

    /**
     * 反序列化
     * @param bytes
     * @return
     * @throws SerializationException
     */
    @Override
    public String deserialize(byte[] bytes) throws SerializationException {
        String key = new String(bytes, charset);
        int i = key.indexOf(PREFIX_KEY);
        if (i == -1) {
            throw new SerializationException("不合法的key");
        }
        else {
            return key.replace(PREFIX_KEY, "");
        }
    }
}
