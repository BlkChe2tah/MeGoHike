package com.example.megohike.common;

public abstract sealed class UiState<T> permits UiStateError, UiStateEmpty, UiStateSuccess {
}
