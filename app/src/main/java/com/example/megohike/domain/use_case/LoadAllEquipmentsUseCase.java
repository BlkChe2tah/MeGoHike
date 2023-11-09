package com.example.megohike.domain.use_case;

import com.example.megohike.data.data_source.database.entities.Equipment;
import com.example.megohike.data.data_source.database.entities.Observation;

import java.util.List;

public interface LoadAllEquipmentsUseCase {
    List<Equipment> getAllEquipmentsByHikeId(int hikeId);
}
