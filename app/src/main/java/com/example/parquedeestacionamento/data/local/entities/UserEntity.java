package com.example.parquedeestacionamento.data.local.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

// Entidade que representa um utilizador (para login)
@Entity(tableName = "users")
public class UserEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @NonNull
    public String username;

    @NonNull
    public String password;

    public UserEntity(@NonNull String username, @NonNull String password) {
        this.username = username;
        this.password = password;
    }
}
