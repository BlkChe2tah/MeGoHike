package com.example.megohike.data.use_case;

import androidx.annotation.NonNull;

import com.example.megohike.data.data_source.database.HikeInformationDatabase;
import com.example.megohike.data.data_source.database.entities.HikeInfo;
import com.example.megohike.data.data_source.database.entities.Observation;
import com.example.megohike.domain.use_case.LoadAllHikeInfoUseCase;
import com.example.megohike.domain.use_case.LoadAllObservationsUseCase;

import java.util.List;

public class LoadAllObservationsUseCaseImpl implements LoadAllObservationsUseCase {
    private HikeInformationDatabase db;

    public LoadAllObservationsUseCaseImpl(@NonNull HikeInformationDatabase db) {
        this.db = db;
    }

    @Override
    public List<Observation> getAllObservationsByHikeId(int hikeId) {
        return db.observationDao().getAllObservationsByHikeId(hikeId);
    }

}
