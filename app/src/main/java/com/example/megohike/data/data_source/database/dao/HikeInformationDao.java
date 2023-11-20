package com.example.megohike.data.data_source.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Upsert;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.example.megohike.data.data_source.database.entities.HikeInfo;

import java.util.List;

@Dao
public interface HikeInformationDao {
    @RawQuery
    List<HikeInfo> getAll(SimpleSQLiteQuery query);

    @Query("SELECT * FROM hike_infos WHERE id = :hikeId")
    HikeInfo get(int hikeId);

    @Upsert()
    void insert(HikeInfo info);

    @Query("DELETE FROM hike_infos WHERE id = :hikeId")
    void delete(int hikeId);

}
