package com.lwk.wochat.api.data.utils;


import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public final class Result<T> {
    private final T value;
    private final Throwable error;

    private Result(T value, Throwable error) {
        this.value = value;
        this.error = error;

    }

    public boolean isSucceeded() {
        return value != null;
    }

    public boolean isFailed() {
        return error != null;
    }

    public T get() {
        if (value == null) {
            throw new NoSuchElementException("Value is null");
        }
        return value;
    }

    public Throwable error() {
        if (error == null) {
            throw new NoSuchElementException("Error is null");
        }
        return error;
    }

    public void ifSucceeded(Consumer<? super T> action) {
        if (value != null) {
            action.accept(value);
        }
    }

    public void ifFailed(Consumer<? super Throwable> action) {
        if (error != null) {
            action.accept(error);
        }
    }

    public void ifSucceededOrElse(Consumer<? super T> action, Runnable emptyAction) {
        if (value != null) {
            action.accept(value);
        } else {
            emptyAction.run();
        }
    }

    public void ifFailedOrElse(Consumer<? super Throwable> action, Runnable emptyAction) {
        if (error != null) {
            action.accept(error);
        } else {
            emptyAction.run();
        }
    }

    public Result<T> filter(Predicate<? super T> predicate, Throwable orError) {
        Objects.requireNonNull(predicate);
        if (isFailed()) {
            return this;
        } else {
            return predicate.test(value) ? this : fail(orError);
        }
    }

    public Result<T> filter(Predicate<? super T> predicate) {
        return filter(predicate, new IllegalArgumentException());
    }

    public <U> Result<U> map(Function<? super T, U> mapper) {
        return value != null
                ? succeed(mapper.apply(value))
                : fail(error);
    }

    public <U> Result<U> flatMap(Function<? super T, ? extends Result<? extends U>> mapper, Throwable orError) {
        Objects.requireNonNull(mapper);
        if (isFailed()) {
            return fail(orError);
        } else {
            @SuppressWarnings("unchecked")
            Result<U> r = (Result<U>) mapper.apply(value);
            return Objects.requireNonNull(r);
        }
    }

    public <U> Result<U> flatMap(Function<? super T, ? extends Result<? extends U>> mapper) {
        return flatMap(mapper, new IllegalArgumentException());
    }

    public Result<T> or(Supplier<? extends Result<? extends T>> supplier) {
        Objects.requireNonNull(supplier);
        if (isSucceeded()) {
            return this;
        } else {
            @SuppressWarnings("unchecked")
            Result<T> r = (Result<T>) supplier.get();
            return Objects.requireNonNull(r);
        }
    }

    public Stream<T> stream() {
        if (isFailed()) {
            return Stream.empty();
        } else {
            return Stream.of(value);
        }
    }

    public T orElse(T other) {
        return value != null ? value : other;
    }

    public T orElseGet(Supplier<? extends T> supplier) {
        return value != null ? value : supplier.get();
    }

    public T orElseThrow() throws Throwable {
        if (value == null) {
            throw error;
        }
        return value;
    }

    public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        if (value != null) {
            return value;
        } else {
            throw exceptionSupplier.get();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        return obj instanceof Result<?> other
                && Objects.equals(value, other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        return value != null
                ? ("Result[" + value + "]")
                : ("Result.fail(" + error + ")");
    }

    public static <T> Result<T> succeed(T value) {
        if (value == null) {
            throw new NoSuchElementException("Value is null");
        }
        return new Result<>(value, null);
    }

    public static <T> Result<T> fail(Throwable error) {
        return new Result<>(null, error);
    }

}
