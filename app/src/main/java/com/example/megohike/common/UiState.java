package com.example.megohike.common;

public abstract sealed class UiState permits UiStateError, UiStateEmpty, UiStateSuccess, UiStateSearchEmpty {
}
