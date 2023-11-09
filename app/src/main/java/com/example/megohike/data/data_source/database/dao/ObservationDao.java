package com.example.megohike.data.data_source.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.megohike.data.data_source.database.entities.Equipment;
import com.example.megohike.data.data_source.database.entities.Observation;

import java.util.List;

@Dao
public interface ObservationDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Observation observation);

    @Query("SELECT * FROM observations WHERE hike_info_id = :id")
    List<Observation> getAllObservationsByHikeId(int id);
}
