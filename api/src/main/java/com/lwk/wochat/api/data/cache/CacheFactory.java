package com.lwk.wochat.api.data.cache;


import com.lwk.wochat.api.data.crud.Crud;

import java.io.Serializable;
import java.util.function.Function;

public interface CacheFactory<K extends Serializable, V> {
    Cache<K, V> create(Crud<K, V> crud);
}
