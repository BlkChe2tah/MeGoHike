package com.example.megohike.presentation.hiking_detail.view_model;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.megohike.domain.use_case.DeleteHikeInfoUseCase;
import com.example.megohike.domain.use_case.LoadAllEquipmentsUseCase;
import com.example.megohike.domain.use_case.LoadAllObservationsUseCase;

public class HikingDetailViewModelFactory implements ViewModelProvider.Factory {

    private LoadAllObservationsUseCase observationUseCase;
    private LoadAllEquipmentsUseCase equipmentsUseCase;
    private DeleteHikeInfoUseCase deleteUseCase;

    public HikingDetailViewModelFactory(@NonNull LoadAllObservationsUseCase observationUseCase, @NonNull LoadAllEquipmentsUseCase equipmentsUseCase, @NonNull DeleteHikeInfoUseCase deleteUseCase) {
        this.observationUseCase = observationUseCase;
        this.equipmentsUseCase = equipmentsUseCase;
        this.deleteUseCase = deleteUseCase;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(HikingDetailViewModel.class)) {
            return (T)new HikingDetailViewModel(observationUseCase, equipmentsUseCase, deleteUseCase);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
