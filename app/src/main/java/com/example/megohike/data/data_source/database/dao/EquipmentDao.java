package com.example.megohike.data.data_source.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Upsert;

import com.example.megohike.data.data_source.database.entities.Equipment;

import java.util.List;

@Dao
public interface EquipmentDao {
    @Upsert()
    void insert(Equipment equipment);

    @Query("SELECT * FROM equipments WHERE hike_info_id = :id")
    List<Equipment> getAllEquipmentsByHikeId(int id);

    @Query("DELETE FROM equipments WHERE id = :equipmentId")
    void delete(int equipmentId);

}
