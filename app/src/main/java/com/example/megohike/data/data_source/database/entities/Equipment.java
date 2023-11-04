package com.example.megohike.data.data_source.database.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "equipments",
        foreignKeys = {
                @ForeignKey(
                        entity = HikeInfo.class,
                        parentColumns = "id",
                        childColumns = "hike_info_id",
                        onDelete = ForeignKey.CASCADE
                )
        }
)
public class Equipment {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int equipmentId = 0;

    @ColumnInfo(name = "hike_info_id", index = true)
    @NonNull
    private int hikeInfoId;
    @NonNull
    private String name;
    private int count = 0;

    public Equipment(int equipmentId, @NonNull int hikeInfoId, @NonNull String name, int count) {
        this.equipmentId = equipmentId;
        this.hikeInfoId = hikeInfoId;
        this.name = name;
        this.count = count;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(int id) {
        this.equipmentId = id;
    }

    public int getHikeInfoId() {
        return hikeInfoId;
    }

    public void setHikeInfoId(int hikeInfoId) {
        this.hikeInfoId = hikeInfoId;
    }
}
