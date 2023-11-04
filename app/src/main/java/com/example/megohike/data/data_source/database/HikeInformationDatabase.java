package com.example.megohike.data.data_source.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.megohike.data.data_source.database.dao.EquipmentDao;
import com.example.megohike.data.data_source.database.dao.HikeInformationDao;
import com.example.megohike.data.data_source.database.entities.Equipment;
import com.example.megohike.data.data_source.database.entities.HikeInfo;
import com.example.megohike.data.data_source.database.entities.Observation;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {HikeInfo.class, Equipment.class, Observation.class}, version = 1, exportSchema = false)
public abstract class HikeInformationDatabase extends RoomDatabase {

    public abstract HikeInformationDao hikeInformationDao();
    public abstract EquipmentDao equipmentDao();

    private static volatile HikeInformationDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static HikeInformationDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (HikeInformationDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    HikeInformationDatabase.class, "hike_info_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
