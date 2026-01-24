package com.example.parquedeestacionamento.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ParkingDao {

    @Insert
    long insert(com.example.parquedeestacionamento.data.local.ParkingEntity record);

    @Update
    void update(com.example.parquedeestacionamento.data.local.ParkingEntity record);

    @Query("SELECT * FROM parking_records ORDER BY entryTime DESC")
    List<com.example.parquedeestacionamento.data.local.ParkingEntity> getAll();

    @Query("SELECT * FROM parking_records WHERE plate = :plate AND isInside = 1 LIMIT 1")
    com.example.parquedeestacionamento.data.local.ParkingEntity getInsideByPlate(String plate);

    @Query("SELECT COUNT(*) FROM parking_records WHERE isInside = 1")
    int countInside();
}
