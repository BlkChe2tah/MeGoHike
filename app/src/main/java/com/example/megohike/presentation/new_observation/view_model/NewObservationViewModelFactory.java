package com.example.megohike.presentation.new_observation.view_model;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.megohike.domain.use_case.AddNewObservationUseCase;

public class NewObservationViewModelFactory implements ViewModelProvider.Factory {


    public NewObservationViewModelFactory() {
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(NewObservationViewModel.class)) {
            return (T)new NewObservationViewModel();
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
