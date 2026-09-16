package com.example.parquedeestacionamento.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

// Guarda a sessão do utilizador em SharedPreferences
public class SessionManager {

    private static final String PREF_NAME = "parking_session";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private static final String KEY_USERNAME = "username";

    private final SharedPreferences prefs;

    public SessionManager(@NonNull Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    // Regista o login
    public void login(@NonNull String username) {
        prefs.edit()
                .putBoolean(KEY_IS_LOGGED_IN, true)
                .putString(KEY_USERNAME, username)
                .apply();
    }

    // Verifica se há sessão ativa
    public boolean isLoggedIn() {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    // Termina a sessão
    public void logout() {
        prefs.edit().clear().apply();
    }
}
