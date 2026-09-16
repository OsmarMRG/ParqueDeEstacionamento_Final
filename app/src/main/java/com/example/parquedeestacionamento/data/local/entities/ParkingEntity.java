package com.example.parquedeestacionamento.data.local.entities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

// Entidade que representa um registo de estacionamento (entrada/saída de um veículo)
@Entity(tableName = "parking")
public class ParkingEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    public String plate;       // Matrícula do veículo

    public long entryTime;     // Timestamp de entrada
    @Nullable
    public Long exitTime;      // Timestamp de saída (null se ainda está dentro)
    public boolean isInside;   // true se o veículo está no parque

    public ParkingEntity(@NonNull String plate, long entryTime) {
        this.plate = plate;
        this.entryTime = entryTime;
        this.exitTime = null;
        this.isInside = true;
    }
}
