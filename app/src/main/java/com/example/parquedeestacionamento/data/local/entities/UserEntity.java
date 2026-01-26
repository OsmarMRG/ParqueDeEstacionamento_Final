package com.example.parquedeestacionamento.data.local.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

/**
 * Entidade que representa um utilizador do sistema.
 * Armazena credenciais de autenticação.
 */
@Entity(tableName = "users")
public class UserEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @NonNull
    public String username;

    @NonNull
    public String password;

    /**
     * Cria um novo utilizador.
     *
     * @param username Nome de utilizador único
     * @param password Password do utilizador (em texto simples - para produção usar
     *                 hash)
     */
    public UserEntity(@NonNull String username, @NonNull String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserEntity that = (UserEntity) o;
        return id == that.id &&
                username.equals(that.username) &&
                password.equals(that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password);
    }
}
