package com.example.parquedeestacionamento.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String PREF = "pax_session";
    private static final String KEY_LOGGED = "logged";
    private static final String KEY_USER = "user";

    private final SharedPreferences sp;

    public SessionManager(Context ctx) {
        sp = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE);
    }

    public void login(String username) {
        sp.edit()
                .putBoolean(KEY_LOGGED, true)
                .putString(KEY_USER, username)
                .apply();
    }

    public boolean isLoggedIn() {
        return sp.getBoolean(KEY_LOGGED, false);
    }

    public String getUser() {
        return sp.getString(KEY_USER, "");
    }

    public void logout() {
        sp.edit().clear().apply();
    }
}
