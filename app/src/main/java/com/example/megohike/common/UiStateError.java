package com.example.megohike.common;

public final class UiStateError<String> extends  UiState<String>{
    private final String message;

    public UiStateError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
