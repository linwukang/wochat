package com.lwk.wochat.api.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.beans.BeanMap;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class BeanUtil {
    static Logger logger = LoggerFactory.getLogger(BeanUtil.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        // 忽略未知属性
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * 将 Map 对象转换为 bean
     * @param map Map 对象
     * @param type bean 的类型
     * @return bean 对象
     * @param <T> bean 的类型
     */
    public static <T> T mapToBean(Object map, Class<T> type) {
        synchronized (objectMapper) {
            if ((map instanceof Integer
                || map instanceof Long
                || map instanceof Float
                || map instanceof Double
                || map instanceof String) && String.class == type) {
                return (T) String.valueOf(map);
            }
            else if (map instanceof Map) {
                try {
                    String mapJson = objectMapper.writeValueAsString(map);
                    return objectMapper.readValue(mapJson, type);
                } catch (IOException e) {
                    logger.error("JSON parsing error: map=" + map + ", type=" + type);
                    logger.error(e.getMessage());
                    throw new RuntimeException(e);
                }
            }
            else {
                logger.error("The map argument is an illegal type: map=" + map + ", type=" + type);
                throw new IllegalArgumentException("The map argument is an illegal type");
            }
        }
    }

    public static <T> T jsonStringToBean(String json, Class<T> type) {
        synchronized (objectMapper) {
            try {
                return objectMapper.readValue(json, type);
            } catch (IOException e) {
                logger.error(e.getMessage());
                logger.error("JSON parsing error: json=" + json + ", type=" + type);
                throw new RuntimeException(e);
            }
        }
    }

    public static<T> String beanToJsonString(T bean) {
        synchronized (objectMapper) {
            try {
                return objectMapper.writeValueAsString(bean);
            } catch (JsonProcessingException e) {
                logger.error(e.getMessage());
                logger.error("JSON processing error: bean=" + bean + ", type=" + bean.getClass());
                throw new RuntimeException(e);
            }
        }
    }
}
