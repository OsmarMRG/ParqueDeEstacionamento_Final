package com.example.parquedeestacionamento.data.local.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.parquedeestacionamento.data.local.dao.UserDao;
import com.example.parquedeestacionamento.data.local.entities.ParkingEntity;
import com.example.parquedeestacionamento.data.local.entities.UserEntity;

@Database(entities = {UserEntity.class, ParkingEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract UserDao userDao();
    public abstract com.example.parquedeestacionamento.data.local.dao.ParkingDao parkingDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "pax_parking_db"
                    ).fallbackToDestructiveMigration().build();
                }
            }
        }
        return INSTANCE;
    }
}
