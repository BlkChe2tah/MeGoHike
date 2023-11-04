package com.example.megohike.presentation.hiking_list.view_model;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.megohike.common.UiState;
import com.example.megohike.common.UiStateEmpty;
import com.example.megohike.common.UiStateSuccess;
import com.example.megohike.data.data_source.database.HikeInformationDatabase;
import com.example.megohike.data.data_source.database.entities.Equipment;
import com.example.megohike.data.data_source.database.entities.HikeInfo;
import com.example.megohike.domain.use_case.AddNewEquipmentUseCase;
import com.example.megohike.domain.use_case.AddNewHikeInfoUseCase;
import com.example.megohike.domain.use_case.LoadAllHikeInfoUseCase;

import java.util.Collections;
import java.util.List;

public class HikingListViewModel extends ViewModel {

    private final LoadAllHikeInfoUseCase useCase;

    private final MutableLiveData<UiState<List<HikeInfo>>> uiState = new MutableLiveData<>();

    public HikingListViewModel(@NonNull LoadAllHikeInfoUseCase useCase) {
        this.useCase = useCase;
    }

    public void loadHikingList() {
        HikeInformationDatabase.databaseWriteExecutor.execute(() -> {
            final List<HikeInfo> result = useCase.getAllHikeInfo();
            if (result != null && result.size() != 0) {
                uiState.postValue(new UiStateSuccess<>(result));
            } else {
                uiState.postValue(new UiStateEmpty<>());
            }
        });
    }

    public LiveData<UiState<List<HikeInfo>>> getUiState() {
        return uiState;
    }

}
