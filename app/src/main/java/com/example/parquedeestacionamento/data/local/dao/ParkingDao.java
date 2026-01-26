package com.example.parquedeestacionamento.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.parquedeestacionamento.data.local.entities.ParkingEntity;

import java.util.List;

@Dao
public interface ParkingDao {

    @Query("SELECT COUNT(*) FROM parking")
    int countAll();

    @Query("SELECT COUNT(*) FROM parking WHERE isInside = 1")
    int countInside();

    @Query("SELECT * FROM parking ORDER BY id DESC")
    List<ParkingEntity> getAll();

    @Query("SELECT * FROM parking WHERE plate = :plate AND isInside = 1 LIMIT 1")
    ParkingEntity getInsideByPlate(String plate);

    @Insert
    void insert(ParkingEntity p);

    @Update
    void update(ParkingEntity p);
}
