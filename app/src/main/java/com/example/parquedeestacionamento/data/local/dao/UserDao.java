package com.example.parquedeestacionamento.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.parquedeestacionamento.data.local.entities.UserEntity;

/**
 * DAO para operações com utilizadores.
 * Define métodos de acesso à tabela de utilizadores.
 */
@Dao
public interface UserDao {

    /**
     * Conta o número total de utilizadores na base de dados.
     *
     * @return Número de utilizadores
     */
    @Query("SELECT COUNT(*) FROM users")
    int countUsers();

    /**
     * Insere um novo utilizador na base de dados.
     *
     * @param user Utilizador a inserir
     */
    @Insert
    void insert(UserEntity user);

    /**
     * Procura um utilizador pelo nome de utilizador.
     *
     * @param username Nome de utilizador a procurar
     * @return Utilizador encontrado ou null se não existir
     */
    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    UserEntity findByUsername(String username);
}
