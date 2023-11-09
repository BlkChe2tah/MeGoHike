package com.example.megohike.presentation.new_equipment.view_model;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.megohike.domain.use_case.AddNewEquipmentUseCase;
import com.example.megohike.domain.use_case.AddNewObservationUseCase;
import com.example.megohike.presentation.new_observation.view_model.NewObservationViewModel;

public class NewEquipmentViewModelFactory implements ViewModelProvider.Factory {

    private AddNewEquipmentUseCase useCase;

    public NewEquipmentViewModelFactory(@NonNull AddNewEquipmentUseCase useCase) {
        this.useCase = useCase;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(NewEquipmentViewModel.class)) {
            return (T)new NewEquipmentViewModel(useCase);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
