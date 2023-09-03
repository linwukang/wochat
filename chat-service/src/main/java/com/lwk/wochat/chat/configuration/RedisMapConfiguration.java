package com.lwk.wochat.chat.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lwk.wochat.api.data.redis.RedisMap;
import com.lwk.wochat.api.data.redis.jedis.JedisMap;
import com.lwk.wochat.api.data.serialization.Serializer;
import com.lwk.wochat.api.data.serialization.serializer.StringSerializer;
import com.lwk.wochat.api.pojo.entity.UserChattingRecord;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.Objects;

@Configuration
public class RedisMapConfiguration {
    @Bean
    public RedisMap<String, String> stringRedisMap(Jedis jedis) {
        return new JedisMap<>(
                jedis,
                ":",
                "chat",
                new StringSerializer(),
                new StringSerializer());
    }

    @Bean
    public RedisMap<Long, UserChattingRecord> userChatRedisMap(Jedis jedis) {

        Serializer<Long> idSerializer = new Serializer<>() {
            @Override
            public byte[] serialize(Long object) {
                return Objects
                        .toString(object)
                        .getBytes();
            }

            @Override
            public Long deserialize(byte[] serialized) {
                return Long.parseLong(new String(serialized));
            }
        };

        Serializer<UserChattingRecord> chattingRecordSerializer = new Serializer<>() {
            @Override
            public byte[] serialize(UserChattingRecord object) {
                ObjectMapper mapper = new ObjectMapper();

                try {
                    String chattingRecordString = mapper.writeValueAsString(object);

                    return chattingRecordString.getBytes();
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public UserChattingRecord deserialize(byte[] serialized) {
                ObjectMapper mapper = new ObjectMapper();

                try {
                    return mapper.readValue(serialized, UserChattingRecord.class);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        return new JedisMap<>(
                jedis,
                ":",
                "chat:sender",
                idSerializer,
                chattingRecordSerializer);
    }
}
