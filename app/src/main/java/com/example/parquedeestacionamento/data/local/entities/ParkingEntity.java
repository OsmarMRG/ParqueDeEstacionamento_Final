package com.example.parquedeestacionamento.data.local.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "parking")
public class ParkingEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    public String plate;

    public long entryTime;
    public Long exitTime;
    public boolean isInside;

    public ParkingEntity(@NonNull String plate, long entryTime) {
        this.plate = plate;
        this.entryTime = entryTime;
        this.exitTime = null;
        this.isInside = true;
    }
}
