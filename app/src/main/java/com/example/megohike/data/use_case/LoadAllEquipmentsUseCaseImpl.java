package com.example.megohike.data.use_case;

import androidx.annotation.NonNull;

import com.example.megohike.data.data_source.database.HikeInformationDatabase;
import com.example.megohike.data.data_source.database.entities.Equipment;
import com.example.megohike.data.data_source.database.entities.Observation;
import com.example.megohike.domain.use_case.LoadAllEquipmentsUseCase;
import com.example.megohike.domain.use_case.LoadAllObservationsUseCase;

import java.util.List;

public class LoadAllEquipmentsUseCaseImpl implements LoadAllEquipmentsUseCase {
    private HikeInformationDatabase db;

    public LoadAllEquipmentsUseCaseImpl(@NonNull HikeInformationDatabase db) {
        this.db = db;
    }

    @Override
    public List<Equipment> getAllEquipmentsByHikeId(int hikeId) {
        return db.equipmentDao().getAllEquipmentsByHikeId(hikeId);
    }

}
