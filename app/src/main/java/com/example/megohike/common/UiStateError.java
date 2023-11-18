package com.example.megohike.common;

public final class UiStateError extends  UiState{
    private final String message;

    public UiStateError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
