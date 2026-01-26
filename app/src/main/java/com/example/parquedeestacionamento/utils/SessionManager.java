package com.example.parquedeestacionamento.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Gestão de sessão do utilizador.
 * Armazena estado de login em SharedPreferences.
 */
public class SessionManager {

    private static final String PREF_NAME = "parking_session";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private static final String KEY_USERNAME = "username";

    private final SharedPreferences preferences;

    /**
     * Cria um novo SessionManager.
     *
     * @param context Contexto da aplicação
     */
    public SessionManager(@NonNull Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Regista o login do utilizador.
     *
     * @param username Nome do utilizador que fez login
     */
    public void login(@NonNull String username) {
        preferences.edit()
                .putBoolean(KEY_IS_LOGGED_IN, true)
                .putString(KEY_USERNAME, username)
                .apply();
    }

    /**
     * Verifica se existe um utilizador com sessão ativa.
     *
     * @return true se existe sessão ativa
     */
    public boolean isLoggedIn() {
        return preferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    /**
     * Obtém o nome do utilizador com sessão ativa.
     *
     * @return Nome do utilizador ou null se não há sessão
     */
    @Nullable
    public String getUsername() {
        return preferences.getString(KEY_USERNAME, null);
    }

    /**
     * Termina a sessão do utilizador atual.
     * Remove todos os dados de sessão.
     */
    public void logout() {
        preferences.edit().clear().apply();
    }
}
