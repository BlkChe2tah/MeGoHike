package com.example.megohike.presentation.main_activity.view_model;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.example.megohike.common.InputDateConverter;
import com.example.megohike.common.InputDateFormatter;
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
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class HikingListViewModel extends ViewModel {
    private static final String BASE_QUERY = "SELECT * FROM hike_infos";

    private final LoadAllHikeInfoUseCase useCase;

    private final MutableLiveData<UiState> _uiState = new MutableLiveData<>(new UiStateEmpty());
    public final LiveData<UiState> uiState = _uiState;

    private final MutableLiveData<List<HikeInfo>> _hikeItems = new MutableLiveData<>(new ArrayList<>());
    public final LiveData<List<HikeInfo>> hikeItems = _hikeItems;

    private static final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private final Runnable loadInfoDataRunnable;

    private ScheduledFuture<?> future = null;

    // for advance search
    private String hikeName = null;

    private String startDate = null;
    private int level = -1;

    private String cacheQuery = null;

    public HikingListViewModel(@NonNull LoadAllHikeInfoUseCase useCase) {
        this.useCase = useCase;
        loadInfoDataRunnable = (Runnable) () -> loadHikingList(false);
    }

    public void loadHikingList(boolean isForceLoad) {
        HikeInformationDatabase.databaseWriteExecutor.execute( () -> {
            loadData(isForceLoad);
        });
    }

    private void loadData(boolean isForceLoad) {
        final String queryStr = loadQuery();
        if (!queryStr.equals(cacheQuery) || isForceLoad) {
            cacheQuery = queryStr;
            final List<HikeInfo> result = useCase.getAllHikeInfo(cacheQuery);
            if (result != null && result.size() != 0) {
                _hikeItems.postValue(result);
                _uiState.postValue(new UiStateSuccess());
            } else {
                _hikeItems.postValue(new ArrayList<>());
                if (Objects.equals(queryStr, BASE_QUERY)) {
                    _uiState.postValue(new UiStateEmpty());
                } else {
                    _uiState.postValue(new UiStateSearchEmpty());
                }
            }
        }
    }

    public void setHikeNameByQuickSearch(String name) {
        setHikeName(name);
        if (future != null) {
            future.cancel(true);
        }
        future = executorService.schedule(loadInfoDataRunnable, 1, TimeUnit.SECONDS);
    }

    public void setHikeName(String name) {
        hikeName = name.trim();
    }

    public String getHikeName() {
        return this.hikeName;
    }

    public void setStartDate(String date) {
        startDate = date;
    }

    public String getStartDate() {
        return this.startDate;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return this.level;
    }

    public boolean isDateFormatCorrect() {
        final Boolean isCorrect = InputDateFormatter.isFormatCorrect(startDate);
        if (isCorrect == null || startDate.isEmpty()) return true;
        return isCorrect;
    }

    private String loadQuery() {
        ArrayList<String> querys = new ArrayList<>();
        if (hikeName != null && !hikeName.isEmpty()) {
            querys.add("name_of_hike LIKE '%" + hikeName + "%'");
        }
        if (startDate != null && !startDate.isEmpty()) {
            querys.add("start_date = " + InputDateConverter.covert(startDate));
        }
        if (level != -1) {
            querys.add("level_of_difficulty = " + level);
        }
        StringBuilder temp = new StringBuilder(BASE_QUERY);
        if (!querys.isEmpty()) {
            temp.append(" WHERE");
            for (int i = 0; i < querys.size(); i++) {
                temp.append(" ").append(querys.get(i));
                if (querys.size() != 1 && i != querys.size() -1) {
                    temp.append(" AND");
                }
            }
        }
        temp.append(";");
        return temp.toString();
    }

}
