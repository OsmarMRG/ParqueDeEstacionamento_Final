package com.example.parquedeestacionamento.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.parquedeestacionamento.data.local.entities.ParkingEntity;

import java.util.List;

/**
 * DAO para operações com registos de estacionamento.
 * Define métodos de acesso à tabela de estacionamento.
 */
@Dao
public interface ParkingDao {

    /**
     * Conta o número total de registos de estacionamento.
     *
     * @return Número total de registos
     */
    @Query("SELECT COUNT(*) FROM parking")
    int countAll();

    /**
     * Conta o número de veículos atualmente dentro do parque.
     *
     * @return Número de veículos dentro
     */
    @Query("SELECT COUNT(*) FROM parking WHERE isInside = 1")
    int countInside();

    /**
     * Obtém todos os registos de estacionamento ordenados por ID (mais recentes
     * primeiro).
     *
     * @return Lista de todos os registos
     */
    @Query("SELECT * FROM parking ORDER BY id DESC")
    List<ParkingEntity> getAll();

    /**
     * Procura um veículo que esteja atualmente dentro do parque pela matrícula.
     *
     * @param plate Matrícula a procurar
     * @return Registo do veículo ou null se não estiver dentro
     */
    @Query("SELECT * FROM parking WHERE plate = :plate AND isInside = 1 LIMIT 1")
    ParkingEntity getInsideByPlate(String plate);

    /**
     * Insere um novo registo de estacionamento.
     *
     * @param parking Registo a inserir
     */
    @Insert
    void insert(ParkingEntity parking);

    /**
     * Atualiza um registo de estacionamento existente.
     *
     * @param parking Registo a atualizar
     */
    @Update
    void update(ParkingEntity parking);
}
