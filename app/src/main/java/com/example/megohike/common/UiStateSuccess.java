package com.example.megohike.common;

public final class UiStateSuccess<T> extends UiState<T> {
    private final T data;

    public UiStateSuccess(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }
}
