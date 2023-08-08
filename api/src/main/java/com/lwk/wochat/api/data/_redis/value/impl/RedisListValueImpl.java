package com.lwk.wochat.api.data._redis.value.impl;

import com.lwk.wochat.api.data._redis.value.RedisListValue;
import org.springframework.data.redis.core.ListOperations;

import java.util.*;

public class RedisListValueImpl<K, V> implements RedisListValue<K, V> {
    private final K key;
    private final ListOperations<K, V> listOperations;

    @SuppressWarnings("all")
    public RedisListValueImpl(K key, ListOperations<K, V> listOperations) {
        this.key = key;
        this.listOperations = listOperations;
    }

    @Override
    public void addFirst(V v) {
        listOperations.leftPush(key, v);
    }

    @Override
    public void addLast(V v) {
        listOperations.leftPush(key, v);
    }

    @Override
    public boolean offerFirst(V v) {
        listOperations.leftPush(key, v);
        return true;
    }

    @Override
    public boolean offerLast(V v) {
        listOperations.leftPush(key, v);
        return true;
    }

    @Override
    public V removeFirst() {
        return Optional
                .ofNullable(listOperations.leftPop(key))
                .orElseThrow();
    }

    @Override
    public V removeLast() {
        return Optional
                .ofNullable(listOperations.rightPop(key))
                .orElseThrow();
    }

    @Override
    public V pollFirst() {
        return listOperations.leftPop(key);
    }

    @Override
    public V pollLast() {
        return listOperations.rightPop(key);
    }

    @Override
    public V getFirst() {
        return Optional
                .ofNullable(listOperations.index(key, 0))
                .orElseThrow();
    }

    @Override
    public V getLast() {
        return Optional
                .ofNullable(listOperations.index(key, size() - 1))
                .orElseThrow();
    }

    @Override
    public V peekFirst() {
        return listOperations.index(key, 0);
    }

    @Override
    public V peekLast() {
        return listOperations.index(key, size() - 1);
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        Long count = listOperations.remove(key, 1, o);
        return count != null && count == 1L;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        Long count = listOperations.remove(key, -1, o);
        return count != null && count == 1L;
    }

    @Override
    public boolean add(V v) {
        listOperations.rightPush(key, v);
        return true;
    }

    @Override
    public boolean offer(V v) {
        listOperations.rightPush(key, v);
        return true;
    }

    @Override
    public V remove() {
        return removeFirst();
    }

    @Override
    public V poll() {
        return pollFirst();
    }

    @Override
    public V element() {
        return getFirst();
    }

    @Override
    public V peek() {
        return peekFirst();
    }

    @Override
    public boolean addAll(Collection<? extends V> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        c.forEach(this::remove);
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        Optional.ofNullable(listOperations.range(key, 0, size()))
                .ifPresent(list -> list.forEach(e -> {
                    if (!c.contains(e)) {
                        listOperations.remove(key, 1, e);
                    }
                }));
        return true;
    }

    @Override
    public void clear() {
        listOperations.getOperations().delete(key);
    }

    @Override
    public void push(V v) {
        addFirst(v);
    }

    @Override
    public V pop() {
        return removeFirst();
    }

    @Override
    public boolean remove(Object o) {
        return removeFirstOccurrence(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return c
                .stream()
                .allMatch(e -> listOperations.indexOf(key, (V) e) != -1);
    }

    @Override
    public boolean contains(Object o) {
        return listOperations.indexOf(key, (V) o) != -1;
    }

    @Override
    public int size() {
        return Math.toIntExact(
                Optional
                        .ofNullable(listOperations.size(key))
                        .orElse(0L));
    }

    @Override
    public boolean isEmpty() {
        return size() == 0L;
    }

    @Override
    public Iterator<V> iterator() {
        return new Iterator<V>() {
            private int currentIndex = 0;
            @Override
            public boolean hasNext() {
                return currentIndex < size();
            }

            @Override
            public V next() {
                V v = listOperations.index(key, currentIndex);
                currentIndex++;
                return v;
            }
        };
    }

    @Override
    public Object[] toArray() {
        return Optional
                .ofNullable(listOperations.range(key, 0, size()))
                .map(List::toArray)
                .orElse(new Object[0]);
    }

    @Override
    @Deprecated
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("傻逼 java");
    }

    @Override
    public Iterator<V> descendingIterator() {
        return new Iterator<V>() {
            private int currentIndex = size() - 1;
            @Override
            public boolean hasNext() {
                return currentIndex >= 0;
            }

            @Override
            public V next() {
                V v = listOperations.index(key, currentIndex);
                currentIndex--;
                return v;
            }
        };
    }
}
