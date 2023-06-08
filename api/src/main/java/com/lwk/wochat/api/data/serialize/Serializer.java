package com.lwk.wochat.api.data.serialize;

/**
 * 序列化器接口
 * 实现了将类型 {@link E} 序列化为类型 {@link S} 的方法 {@link Serializer#serialize},
 * 以及将类型 {@link S} 反序列化为 {@link E} 的方法 {@link Serializer#deserialize}
 * @param <S> 序列类型
 * @param <E> 实体类型
 */
public interface Serializer<S, E> {

    /**
     * 序列化方法
     * @param entity 将要序列化的实体对象
     * @return 序列
     */
    S serialize(E entity);

    /**
     * 反序列化方法
     * @param serialized 将要反序列化的序列
     * @param type 反序列化的返回类型
     * @return 实体对象
     */
    E deserialize(S serialized, Class<E> type);
}
