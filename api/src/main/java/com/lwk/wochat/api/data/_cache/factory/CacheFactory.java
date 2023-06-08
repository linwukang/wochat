package com.lwk.wochat.api.data._cache.factory;


import com.lwk.wochat.api.data._cache.Cache;
import com.lwk.wochat.api.data.crud.Crud;

import java.io.Serializable;

public interface CacheFactory<K extends Serializable, V> {
    Cache<K, V> create(Crud<K, V> crud);
}
