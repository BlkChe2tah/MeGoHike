package com.example.megohike.presentation.main_activity.view_model;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.megohike.common.UiState;
import com.example.megohike.common.UiStateEmpty;
import com.example.megohike.common.UiStateSearchEmpty;
import com.example.megohike.common.UiStateSuccess;
import com.example.megohike.data.data_source.database.HikeInformationDatabase;
import com.example.megohike.data.data_source.database.entities.Equipment;
import com.example.megohike.data.data_source.database.entities.HikeInfo;
import com.example.megohike.domain.use_case.AddNewEquipmentUseCase;
import com.example.megohike.domain.use_case.AddNewHikeInfoUseCase;
import com.example.megohike.domain.use_case.LoadAllHikeInfoUseCase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class HikingListViewModel extends ViewModel {

    private final LoadAllHikeInfoUseCase useCase;

    private final MutableLiveData<UiState> _uiState = new MutableLiveData<>(new UiStateEmpty());
    public final LiveData<UiState> uiState = _uiState;

    private final MutableLiveData<List<HikeInfo>> _hikeItems = new MutableLiveData<>(new ArrayList<>());
    public final LiveData<List<HikeInfo>> hikeItems = _hikeItems;

    private static final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private final Runnable loadInfoDataRunnable;

    private ScheduledFuture<?> future = null;

    private String cacheNameQuery = null;
    private String hikeNameQuery = null;

    public HikingListViewModel(@NonNull LoadAllHikeInfoUseCase useCase) {
        this.useCase = useCase;
        loadInfoDataRunnable = () -> {
            if (hikeNameQuery.trim().equals(cacheNameQuery)) {
                return;
            }
            cacheNameQuery = hikeNameQuery.trim();
            final List<HikeInfo> result = cacheNameQuery.isEmpty() ? useCase.getAllHikeInfo() : useCase.getAllHikeInfoByName(cacheNameQuery);
            if (result != null && result.size() != 0) {
                _hikeItems.postValue(result);
                _uiState.postValue(new UiStateSuccess());
            } else {
                _hikeItems.postValue(new ArrayList<>());
                _uiState.postValue(new UiStateSearchEmpty());
            }
        };
    }

    public void loadHikingList() {
        HikeInformationDatabase.databaseWriteExecutor.execute(() -> {
            final List<HikeInfo> result = cacheNameQuery == null || cacheNameQuery.isEmpty() ? useCase.getAllHikeInfo() : useCase.getAllHikeInfoByName(hikeNameQuery);
            if (result != null && result.size() != 0) {
                _hikeItems.postValue(result);
                _uiState.postValue(new UiStateSuccess());
            } else {
                _hikeItems.postValue(new ArrayList<>());
                if (cacheNameQuery == null || cacheNameQuery.isEmpty()) {
                    _uiState.postValue(new UiStateEmpty());
                } else {
                    _uiState.postValue(new UiStateSearchEmpty());
                }
            }
        });
    }

    public void setHikeNameQuery(String name) {
        hikeNameQuery = name;
        if (future != null) {
            future.cancel(true);
        }
        future = executorService.schedule(loadInfoDataRunnable, 1, TimeUnit.SECONDS);
    }

}
