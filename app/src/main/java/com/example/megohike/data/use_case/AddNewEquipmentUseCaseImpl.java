package com.example.megohike.data.use_case;

import androidx.annotation.NonNull;

import com.example.megohike.data.data_source.database.HikeInformationDatabase;
import com.example.megohike.data.data_source.database.entities.Equipment;
import com.example.megohike.data.data_source.database.entities.HikeInfo;
import com.example.megohike.domain.use_case.AddNewEquipmentUseCase;
import com.example.megohike.domain.use_case.AddNewHikeInfoUseCase;

public class AddNewEquipmentUseCaseImpl implements AddNewEquipmentUseCase {
    private HikeInformationDatabase db;

    public AddNewEquipmentUseCaseImpl(@NonNull HikeInformationDatabase db) {
        this.db = db;
    }

    @Override
    public void insertEquipment(Equipment equipment) {
        db.equipmentDao().insert(equipment);
    }
}
