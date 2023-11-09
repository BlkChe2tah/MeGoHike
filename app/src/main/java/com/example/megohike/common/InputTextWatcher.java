package com.example.megohike.common;

import android.text.Editable;
import android.text.TextWatcher;

import androidx.annotation.NonNull;

public class InputTextWatcher implements TextWatcher {

    public interface TextChangedListener {
        void onTextChanged(String s);
    }

    private final TextChangedListener textWatcher;

    public InputTextWatcher(@NonNull TextChangedListener textWatcher) {
        this.textWatcher = textWatcher;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        textWatcher.onTextChanged(s.toString());
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
