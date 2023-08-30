package com.lwk.wochat.api.data.jpa.repository;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;

public interface JpaRepositoryAsync<T, ID> extends JpaRepository<T, ID> {
    @Async
    default Future<List<T>> findAllAsync() {
        return new AsyncResult<>(findAll());
    }

    @Async
    default Future<List<T>> findAllAsync(Sort sort) {
        return new AsyncResult<>(findAll(sort));
    }

    @Async
    default Future<List<T>> findAllByIdAsync(Iterable<ID> iterable) {
        return new AsyncResult<>(findAllById(iterable));
    }

    @Async
    default <S extends T> Future<List<S>> saveAllAsync(Iterable<S> iterable) {
        return new AsyncResult<>(saveAll(iterable));
    }

    @Async
    default void flushAsync() {
        flush();
    }

    @Async
    default <S extends T> Future<S> saveAndFlushAsync(S s) {
        return new AsyncResult<>(saveAndFlush(s));
    }

    @Async
    default void deleteInBatchAsync(Iterable<T> iterable) {
        deleteInBatch(iterable);
    }

    @Async
    default void deleteAllInBatchAsync() {
        deleteAllInBatch();
    }

    @Async
    default Future<T> getOneAsync(ID id) {
        return new AsyncResult<>(getOne(id));
    }

    @Async
    default <S extends T> Future<List<S>> findAllAsync(Example<S> example) {
        return new AsyncResult<>(findAll(example));
    }

    @Async
    default <S extends T> Future<List<S>> findAllAsync(Example<S> example, Sort sort) {
        return new AsyncResult<>(findAll(example, sort));
    }

    @Async
    default Future<Page<T>> findAllAsync(Pageable pageable) {
        return new AsyncResult<>(findAll(pageable));
    }

    @Async
    default <S extends T> Future<S> saveAsync(S s) {
        return new AsyncResult<>(save(s));
    }

    @Async
    default Future<Optional<T>> findByIdAsync(ID id) {
        return new AsyncResult<>(findById(id));
    }

    @Async
    default Future<Boolean> existsByIdAsync(ID id) {
        return new AsyncResult<>(existsById(id));
    }

    @Async
    default Future<Long> countAsync() {
        return new AsyncResult<>(count());
    }

    @Async
    default void deleteByIdAsync(ID id) {
        deleteById(id);
    }

    @Async
    default void deleteAsync(T t) {
        delete(t);
    }

    @Async
    default void deleteAllAsync(Iterable<? extends T> iterable) {
        deleteAll(iterable);
    }

    @Async
    default void deleteAllAsync() {
        deleteAll();
    }

    @Async
    default <S extends T> Future<Optional<S>> findOneAsync(Example<S> example) {
        return new AsyncResult<>(findOne(example));
    }

    @Async
    default <S extends T> Future<Page<S>> findAllAsync(Example<S> example, Pageable pageable) {
        return new AsyncResult<>(findAll(example, pageable));
    }

    @Async
    default <S extends T> Future<Long> countAsync(Example<S> example) {
        return new AsyncResult<>(count(example));
    }

    @Async
    default <S extends T> Future<Boolean> existsAsync(Example<S> example) {
        return new AsyncResult<>(exists(example));
    }
}
