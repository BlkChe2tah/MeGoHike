package com.example.megohike.data.use_case;

import androidx.annotation.NonNull;

import com.example.megohike.data.data_source.database.HikeInformationDatabase;
import com.example.megohike.domain.use_case.DeleteEquipmentUseCase;
import com.example.megohike.domain.use_case.DeleteHikeInfoUseCase;

public class DeleteEquipmentUseCaseImpl implements DeleteEquipmentUseCase {
    private HikeInformationDatabase db;

    public DeleteEquipmentUseCaseImpl(@NonNull HikeInformationDatabase db) {
        this.db = db;
    }

    @Override
    public void deleteEquipment(int equipmentId) {
        db.equipmentDao().delete(equipmentId);
    }
}
