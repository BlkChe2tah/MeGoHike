package com.example.megohike.data.data_source.database.entities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "hike_infos")
public class HikeInfo {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int hikeInfoId = 0;
    @NonNull
    @ColumnInfo(name = "name_of_hike")
    private String nameOfHike;
    @Nullable
    private String latitude;
    @Nullable
    private String longitude;
    @ColumnInfo(name = "start_date")
    private long startDate;
    @ColumnInfo(name = "expected_date")
    private long expectedDate;
    @ColumnInfo(name = "length_of_hike")
    private Double lengthOfHike = 0.0;
    @ColumnInfo(name = "level_of_difficulty")
    private int levelOfDifficulty;

    public HikeInfo(
            int hikeInfoId,
            @NonNull String nameOfHike,
            @Nullable String latitude,
            @Nullable String longitude,
            long startDate,
            long expectedDate,
            @NonNull Double lengthOfHike,
            int levelOfDifficulty
    ) {
        this.hikeInfoId = hikeInfoId;
        this.nameOfHike = nameOfHike;
        this.latitude = latitude;
        this.longitude = longitude;
        this.startDate = startDate;
        this.expectedDate = expectedDate;
        this.lengthOfHike = lengthOfHike;
        this.levelOfDifficulty = levelOfDifficulty;
    }

    @NonNull
    public String getNameOfHike() {
        return nameOfHike;
    }

    public void setNameOfHike(@NonNull String nameOfHike) {
        this.nameOfHike = nameOfHike;
    }

    @Nullable
    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(@Nullable String latitude) {
        this.latitude = latitude;
    }

    @Nullable
    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(@Nullable String longitude) {
        this.longitude = longitude;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getExpectedDate() {
        return expectedDate;
    }

    public void setExpectedDate(long expectedDate) {
        this.expectedDate = expectedDate;
    }

    public Double getLengthOfHike() {
        return lengthOfHike;
    }

    public void setLengthOfHike(Double lengthOfHike) {
        this.lengthOfHike = lengthOfHike;
    }

    public int getLevelOfDifficulty() {
        return levelOfDifficulty;
    }

    public void setLevelOfDifficulty(int levelOfDifficulty) {
        this.levelOfDifficulty = levelOfDifficulty;
    }

    public int getHikeInfoId() {
        return hikeInfoId;
    }

    public void setHikeInfoId(int id) {
        this.hikeInfoId = id;
    }
}
