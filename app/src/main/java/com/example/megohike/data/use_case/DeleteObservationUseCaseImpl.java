package com.example.megohike.data.use_case;

import androidx.annotation.NonNull;

import com.example.megohike.data.data_source.database.HikeInformationDatabase;
import com.example.megohike.domain.use_case.DeleteEquipmentUseCase;
import com.example.megohike.domain.use_case.DeleteObservationUseCase;

public class DeleteObservationUseCaseImpl implements DeleteObservationUseCase {
    private HikeInformationDatabase db;

    public DeleteObservationUseCaseImpl(@NonNull HikeInformationDatabase db) {
        this.db = db;
    }

    @Override
    public void deleteObservation(int observationId) {
        db.observationDao().delete(observationId);
    }
}
