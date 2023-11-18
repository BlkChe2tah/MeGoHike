package com.example.megohike.data.use_case;

import androidx.annotation.NonNull;

import com.example.megohike.data.data_source.database.HikeInformationDatabase;
import com.example.megohike.data.data_source.database.entities.HikeInfo;
import com.example.megohike.domain.use_case.HikeInfoDetailUseCase;

public class HikeInfoDetailUseCaseImpl implements HikeInfoDetailUseCase {
    private HikeInformationDatabase db;

    public HikeInfoDetailUseCaseImpl(@NonNull HikeInformationDatabase db) {
        this.db = db;
    }

    @Override
    public HikeInfo loadHikeInfo(int hikeId) {
        return db.hikeInformationDao().get(hikeId);
    }
}
