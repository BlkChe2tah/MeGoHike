package com.example.megohike.presentation.new_observation_confirm.view_model;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.megohike.domain.use_case.AddNewHikeInfoUseCase;
import com.example.megohike.domain.use_case.AddNewObservationUseCase;
import com.example.megohike.presentation.new_hiking_confirm.view_model.NewHikingConfirmViewModel;

public class NewObservationConfirmViewModelFactory implements ViewModelProvider.Factory {

    private AddNewObservationUseCase useCase;

    public NewObservationConfirmViewModelFactory(@NonNull AddNewObservationUseCase useCase) {
        this.useCase = useCase;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(NewObservationConfirmViewModel.class)) {
            return (T) new NewObservationConfirmViewModel(useCase);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
