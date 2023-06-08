package com.lwk.wochat.api.data.crud;

public interface Retrieve<ID, T> {
    T retrieve(ID id);
}

