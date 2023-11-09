package com.example.megohike.data.use_case;

import androidx.annotation.NonNull;

import com.example.megohike.data.data_source.database.HikeInformationDatabase;
import com.example.megohike.data.data_source.database.entities.HikeInfo;
import com.example.megohike.domain.use_case.AddNewHikeInfoUseCase;
import com.example.megohike.domain.use_case.DeleteHikeInfoUseCase;

public class DeleteHikeInfoUseCaseImpl implements DeleteHikeInfoUseCase {
    private HikeInformationDatabase db;

    public DeleteHikeInfoUseCaseImpl(@NonNull HikeInformationDatabase db) {
        this.db = db;
    }

    @Override
    public void deleteHikeInfo(int hikeId) {
        db.hikeInformationDao().delete(hikeId);
    }
}
