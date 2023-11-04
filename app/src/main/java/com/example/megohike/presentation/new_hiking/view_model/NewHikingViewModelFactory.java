package com.example.megohike.presentation.new_hiking.view_model;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.megohike.domain.use_case.AddNewHikeInfoUseCase;
import com.example.megohike.domain.use_case.LoadAllHikeInfoUseCase;
import com.example.megohike.presentation.hiking_list.view_model.HikingListViewModel;

public class NewHikingViewModelFactory implements ViewModelProvider.Factory {

    private AddNewHikeInfoUseCase useCase;

    public NewHikingViewModelFactory(@NonNull AddNewHikeInfoUseCase useCase) {
        this.useCase = useCase;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(NewHikingViewModel.class)) {
            return (T)new NewHikingViewModel(useCase);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
