package com.example.parquedeestacionamento.data.local.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.parquedeestacionamento.data.local.dao.ParkingDao;
import com.example.parquedeestacionamento.data.local.dao.UserDao;
import com.example.parquedeestacionamento.data.local.entities.ParkingEntity;
import com.example.parquedeestacionamento.data.local.entities.UserEntity;

/**
 * Base de dados Room da aplicação.
 * Implementa singleton pattern para garantir uma única instância.
 */
@Database(entities = { UserEntity.class, ParkingEntity.class }, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "parking_database";
    private static volatile AppDatabase INSTANCE;

    /**
     * Obtém o DAO de utilizadores.
     *
     * @return DAO para operações com utilizadores
     */
    public abstract UserDao userDao();

    /**
     * Obtém o DAO de estacionamento.
     *
     * @return DAO para operações com registos de estacionamento
     */
    public abstract ParkingDao parkingDao();

    /**
     * Obtém a instância única da base de dados.
     * Utiliza double-checked locking para thread safety.
     *
     * @param context Contexto da aplicação
     * @return Instância única da base de dados
     */
    @NonNull
    public static AppDatabase getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
