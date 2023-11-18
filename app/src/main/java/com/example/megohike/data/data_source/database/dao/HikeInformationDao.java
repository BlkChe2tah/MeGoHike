package com.example.megohike.data.data_source.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Upsert;

import com.example.megohike.data.data_source.database.entities.HikeInfo;

import java.util.List;

@Dao
public interface HikeInformationDao {
    @Query("SELECT * FROM hike_infos")
    List<HikeInfo> getAll();

    @Query("SELECT * FROM hike_infos WHERE name_of_hike LIKE '%' || :name || '%'")
    List<HikeInfo> getHikeByName(String name);

    @Query("SELECT * FROM hike_infos WHERE id = :hikeId")
    HikeInfo get(int hikeId);

    @Upsert()
    void insert(HikeInfo info);

    @Query("DELETE FROM hike_infos WHERE id = :hikeId")
    void delete(int hikeId);

}
