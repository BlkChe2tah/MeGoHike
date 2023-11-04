package com.example.megohike.domain.use_case;

import com.example.megohike.data.data_source.database.entities.Equipment;

public interface AddNewEquipmentUseCase {
    void insertEquipment(Equipment equipment);
}
