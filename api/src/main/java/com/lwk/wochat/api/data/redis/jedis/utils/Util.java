package com.lwk.wochat.api.data.redis.jedis.utils;

import com.lwk.wochat.api.data.serialization.Serializer;

import java.nio.charset.Charset;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Util {
    public static <T> List<String> toStringList(Collection<?> c, Serializer<T> serializer, Charset charset) {
        return c.stream()
                .filter(e -> {
                    try {
                        T t = (T) e;
                        return true;
                    } catch (Exception _e) {
                        return false;
                    }
                })
                .map(e -> (T) e)
                .map(serializer::serialize)
                .map(bs -> new String(bs, charset))
                .collect(Collectors.toList());
    }
}
