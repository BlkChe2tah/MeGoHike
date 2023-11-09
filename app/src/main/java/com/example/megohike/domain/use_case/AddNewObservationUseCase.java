package com.example.megohike.domain.use_case;

import com.example.megohike.data.data_source.database.entities.Equipment;
import com.example.megohike.data.data_source.database.entities.Observation;

public interface AddNewObservationUseCase {
    void insertObservation(Observation data);
}
