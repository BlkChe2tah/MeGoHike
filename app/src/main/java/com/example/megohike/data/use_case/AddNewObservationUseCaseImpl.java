package com.example.megohike.data.use_case;

import androidx.annotation.NonNull;

import com.example.megohike.data.data_source.database.HikeInformationDatabase;
import com.example.megohike.data.data_source.database.entities.Observation;
import com.example.megohike.domain.use_case.AddNewObservationUseCase;

public class AddNewObservationUseCaseImpl implements AddNewObservationUseCase {
    private HikeInformationDatabase db;

    public AddNewObservationUseCaseImpl(@NonNull HikeInformationDatabase db) {
        this.db = db;
    }

    @Override
    public void insertObservation(Observation data) {
        db.observationDao().insert(data);
    }
}
