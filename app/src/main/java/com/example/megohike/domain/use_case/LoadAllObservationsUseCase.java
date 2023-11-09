package com.example.megohike.domain.use_case;

import com.example.megohike.data.data_source.database.entities.Observation;

import java.util.List;

public interface LoadAllObservationsUseCase {
    List<Observation> getAllObservationsByHikeId(int hikeId);
}
