package com.example.megohike.presentation.new_observation_confirm.view_model;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.megohike.data.data_source.database.HikeInformationDatabase;
import com.example.megohike.data.data_source.database.entities.HikeInfo;
import com.example.megohike.data.data_source.database.entities.Observation;
import com.example.megohike.domain.use_case.AddNewHikeInfoUseCase;
import com.example.megohike.domain.use_case.AddNewObservationUseCase;

public class NewObservationConfirmViewModel extends ViewModel {

    final private AddNewObservationUseCase useCase;

    final private MutableLiveData<Boolean> uiState = new MutableLiveData<>(null);
    final public LiveData<Boolean> getUiState = uiState;

    private Observation data = null;

    public NewObservationConfirmViewModel(@NonNull AddNewObservationUseCase useCase) {
        this.useCase = useCase;
    }

    public void setObservationData(Observation data) {
        this.data = data;
    }

    public Observation getObservationData() {
        return data;
    }

    public void save() {
        HikeInformationDatabase.databaseWriteExecutor.execute(() ->{
            try {
                useCase.insertObservation(data);
                uiState.postValue(true);
            } catch (NumberFormatException e) {
                uiState.postValue(false);
            }
        });
    }

}
