package com.example.megohike.presentation.main_activity.view_model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.megohike.domain.use_case.LoadAllHikeInfoUseCase;

public class HikingListViewModelFactory implements ViewModelProvider.Factory {

    private LoadAllHikeInfoUseCase useCase;

    public HikingListViewModelFactory(@NonNull LoadAllHikeInfoUseCase useCase) {
        this.useCase = useCase;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(HikingListViewModel.class)) {
            return (T)new HikingListViewModel(useCase);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
