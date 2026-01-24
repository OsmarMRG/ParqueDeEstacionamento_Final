package com.example.parquedeestacionamento.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.parquedeestacionamento.data.local.UserEntity;

@Dao
public interface UserDao {

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    UserEntity getByUsername(String username);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    long insert(UserEntity user);
}
