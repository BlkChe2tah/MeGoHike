package com.example.megohike.presentation.new_hiking_confirm.view_model;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.megohike.data.data_source.database.HikeInformationDatabase;
import com.example.megohike.data.data_source.database.entities.HikeInfo;
import com.example.megohike.domain.use_case.AddNewHikeInfoUseCase;

public class NewHikingConfirmViewModel extends ViewModel {

    final private AddNewHikeInfoUseCase useCase;

    final private MutableLiveData<Boolean> uiState = new MutableLiveData<>(null);
    final public LiveData<Boolean> getUiState = uiState;

    private HikeInfo data = null;

    public NewHikingConfirmViewModel(@NonNull AddNewHikeInfoUseCase useCase) {
        this.useCase = useCase;
    }

    public void setHikeInfoData(HikeInfo data) {
        this.data = data;
    }

    public HikeInfo getHikeInfoData() {
        return data;
    }

    public void save() {
        HikeInformationDatabase.databaseWriteExecutor.execute(() ->{
            try {
                useCase.insertHikeInfo(data);
                uiState.postValue(true);
            } catch (NumberFormatException e) {
                uiState.postValue(false);
            }
        });
    }

}
