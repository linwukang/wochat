package com.lwk.wochat.api.data.crud;

public interface Crud<ID, T>
        extends
        Create<T>,
        Retrieve<ID, T>,
        Update<ID, T>,
        Delete<ID> {

}

