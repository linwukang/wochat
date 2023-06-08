package com.lwk.wochat.api.data.crud;

public interface Update<ID, T> {
    void update(ID id, T entity);
}

