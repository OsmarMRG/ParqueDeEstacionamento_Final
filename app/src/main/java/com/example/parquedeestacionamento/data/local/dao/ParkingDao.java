package com.example.parquedeestacionamento.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.parquedeestacionamento.data.local.entities.ParkingEntity;

import java.util.List;

// DAO para aceder à tabela de estacionamento
@Dao
public interface ParkingDao {

    // Conta veículos que estão dentro do parque
    @Query("SELECT COUNT(*) FROM parking WHERE isInside = 1")
    int countInside();

    // Lista todos os registos (mais recentes primeiro)
    @Query("SELECT * FROM parking ORDER BY id DESC")
    List<ParkingEntity> getAll();

    // Procura um veículo que esteja dentro pela matrícula
    @Query("SELECT * FROM parking WHERE plate = :plate AND isInside = 1 LIMIT 1")
    ParkingEntity getInsideByPlate(String plate);

    @Insert
    void insert(ParkingEntity parking);

    @Update
    void update(ParkingEntity parking);
}
