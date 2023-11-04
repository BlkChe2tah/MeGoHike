package com.example.megohike.data.data_source.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.megohike.data.data_source.database.entities.Equipment;

import java.util.List;

@Dao
public interface EquipmentDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Equipment equipment);

    @Query("SELECT * FROM equipments WHERE hike_info_id = :id")
    List<Equipment> getAllEquipmentsById(int id);
}
