package com.example.parquedeestacionamento.data.local;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "users", indices = {@Index(value = "username", unique = true)})
public class UserEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    public String username;

    @NonNull
    public String password;

    public UserEntity(@NonNull String username, @NonNull String password) {
        this.username = username;
        this.password = password;
    }
}
