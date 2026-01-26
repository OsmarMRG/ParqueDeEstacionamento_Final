package com.example.parquedeestacionamento.data.local.entities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

/**
 * Entidade que representa um registo de estacionamento.
 * Cada registo contém a matrícula do veículo e os tempos de entrada/saída.
 */
@Entity(tableName = "parking")
public class ParkingEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    public String plate;

    public long entryTime;

    @Nullable
    public Long exitTime;

    public boolean isInside;

    /**
     * Cria um novo registo de estacionamento.
     *
     * @param plate     Matrícula do veículo (deve estar em maiúsculas)
     * @param entryTime Timestamp da entrada em milissegundos
     */
    public ParkingEntity(@NonNull String plate, long entryTime) {
        this.plate = plate;
        this.entryTime = entryTime;
        this.exitTime = null;
        this.isInside = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ParkingEntity that = (ParkingEntity) o;
        return id == that.id &&
                entryTime == that.entryTime &&
                isInside == that.isInside &&
                plate.equals(that.plate) &&
                Objects.equals(exitTime, that.exitTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, plate, entryTime, exitTime, isInside);
    }
}
