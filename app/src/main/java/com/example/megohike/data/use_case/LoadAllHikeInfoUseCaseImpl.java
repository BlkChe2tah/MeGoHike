package com.example.megohike.data.use_case;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.example.megohike.data.data_source.database.HikeInformationDatabase;
import com.example.megohike.data.data_source.database.entities.Equipment;
import com.example.megohike.data.data_source.database.entities.HikeInfo;
import com.example.megohike.domain.use_case.LoadAllHikeInfoUseCase;

import java.util.List;

public class LoadAllHikeInfoUseCaseImpl implements LoadAllHikeInfoUseCase {
    private HikeInformationDatabase db;

    public LoadAllHikeInfoUseCaseImpl(@NonNull HikeInformationDatabase db) {
        this.db = db;
    }

    @Override
    public List<HikeInfo> getAllHikeInfo(String query) {
        return db.hikeInformationDao().getAll(new SimpleSQLiteQuery(query));
    }

}
