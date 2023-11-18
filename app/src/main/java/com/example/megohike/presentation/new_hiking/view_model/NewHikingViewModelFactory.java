package com.example.megohike.presentation.new_hiking.view_model;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class NewHikingViewModelFactory implements ViewModelProvider.Factory {

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(NewHikingViewModel.class)) {
            return (T)new NewHikingViewModel();
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
