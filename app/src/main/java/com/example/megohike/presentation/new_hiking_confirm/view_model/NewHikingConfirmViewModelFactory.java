package com.example.megohike.presentation.new_hiking_confirm.view_model;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.megohike.domain.use_case.AddNewHikeInfoUseCase;

public class NewHikingConfirmViewModelFactory implements ViewModelProvider.Factory {

    private AddNewHikeInfoUseCase useCase;

    public NewHikingConfirmViewModelFactory(@NonNull AddNewHikeInfoUseCase useCase) {
        this.useCase = useCase;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(NewHikingConfirmViewModel.class)) {
            return (T) new NewHikingConfirmViewModel(useCase);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
