package com.example.parquedeestacionamento.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.parquedeestacionamento.data.local.entities.UserEntity;

// DAO para aceder à tabela de utilizadores
@Dao
public interface UserDao {

    // Conta o número total de utilizadores
    @Query("SELECT COUNT(*) FROM users")
    int countUsers();

    @Insert
    void insert(UserEntity user);

    // Procura um utilizador pelo nome
    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    UserEntity findByUsername(String username);
}
