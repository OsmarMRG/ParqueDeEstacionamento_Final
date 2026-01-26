package com.example.parquedeestacionamento.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.parquedeestacionamento.data.local.entities.UserEntity;

@Dao
public interface UserDao {

    @Query("SELECT COUNT(*) FROM users")
    int countUsers();

    @Insert
    void insert(UserEntity u);

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    UserEntity findByUsername(String username);
}
