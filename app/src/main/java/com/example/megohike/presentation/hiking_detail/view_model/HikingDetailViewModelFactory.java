package com.example.megohike.presentation.hiking_detail.view_model;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.megohike.domain.use_case.DeleteEquipmentUseCase;
import com.example.megohike.domain.use_case.DeleteHikeInfoUseCase;
import com.example.megohike.domain.use_case.DeleteObservationUseCase;
import com.example.megohike.domain.use_case.HikeInfoDetailUseCase;
import com.example.megohike.domain.use_case.LoadAllEquipmentsUseCase;
import com.example.megohike.domain.use_case.LoadAllObservationsUseCase;

public class HikingDetailViewModelFactory implements ViewModelProvider.Factory {

    private LoadAllObservationsUseCase observationUseCase;
    private LoadAllEquipmentsUseCase equipmentsUseCase;
    private DeleteHikeInfoUseCase deleteUseCase;
    private final DeleteEquipmentUseCase deleteEquipmentUseCase;
    private final DeleteObservationUseCase deleteObservationUseCase;
    private final HikeInfoDetailUseCase hikeInfoDetailUseCase;

    public HikingDetailViewModelFactory(@NonNull LoadAllObservationsUseCase observationUseCase, @NonNull LoadAllEquipmentsUseCase equipmentsUseCase, @NonNull DeleteHikeInfoUseCase deleteUseCase, @NonNull DeleteEquipmentUseCase deleteEquipmentUseCase, @NonNull DeleteObservationUseCase deleteObservationUseCase , @NonNull HikeInfoDetailUseCase hikeInfoDetailUseCase) {
        this.observationUseCase = observationUseCase;
        this.equipmentsUseCase = equipmentsUseCase;
        this.deleteUseCase = deleteUseCase;
        this.deleteEquipmentUseCase = deleteEquipmentUseCase;
        this.deleteObservationUseCase = deleteObservationUseCase;
        this.hikeInfoDetailUseCase = hikeInfoDetailUseCase;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(HikingDetailViewModel.class)) {
            return (T)new HikingDetailViewModel(observationUseCase, equipmentsUseCase, deleteUseCase, deleteEquipmentUseCase, deleteObservationUseCase, hikeInfoDetailUseCase);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
