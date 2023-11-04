package com.example.megohike.data.data_source.database.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "observations",
        foreignKeys = {
                @ForeignKey(
                        entity = HikeInfo.class,
                        parentColumns = "id",
                        childColumns = "hike_info_id",
                        onDelete = ForeignKey.CASCADE
                )
        }
)
public class Observation {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int observationId = 0;
    @ColumnInfo(name = "hike_info_id", index = true)
    @NonNull
    private int hikeInfoId;
    @NonNull
    private String observation;
    private String image;
    private int time;
    private String comment;

    public Observation(int observationId, @NonNull int hikeInfoId, @NonNull String observation, String image, int time, String comment) {
        this.observationId = observationId;
        this.hikeInfoId = hikeInfoId;
        this.observation = observation;
        this.image = image;
        this.time = time;
        this.comment = comment;
    }

    @NonNull
    public String getObservation() {
        return observation;
    }

    public void setObservation(@NonNull String observation) {
        this.observation = observation;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getObservationId() {
        return observationId;
    }

    public void setObservationId(int id) {
        this.observationId = id;
    }

    public int getHikeInfoId() {
        return hikeInfoId;
    }

    public void setHikeInfoId(int hikeInfoId) {
        this.hikeInfoId = hikeInfoId;
    }
}
