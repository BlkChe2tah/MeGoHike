package com.example.megohike.data.use_case;

import androidx.annotation.NonNull;

import com.example.megohike.data.data_source.database.HikeInformationDatabase;
import com.example.megohike.data.data_source.database.entities.HikeInfo;
import com.example.megohike.domain.use_case.AddNewHikeInfoUseCase;

public class AddNewHikeInfoUseCaseImpl implements AddNewHikeInfoUseCase {
    private HikeInformationDatabase db;

    public AddNewHikeInfoUseCaseImpl(@NonNull HikeInformationDatabase db) {
        this.db = db;
    }

    @Override
    public void insertHikeInfo(HikeInfo info) {
        db.hikeInformationDao().insert(info);
    }
}
