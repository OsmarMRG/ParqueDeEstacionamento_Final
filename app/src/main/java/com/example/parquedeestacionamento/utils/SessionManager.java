package com.example.parquedeestacionamento.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String PREF = "session_pref";
    private static final String KEY_USER = "logged_user";

    private final SharedPreferences sp;

    public SessionManager(Context ctx) {
        sp = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE);
    }

    public void setLoggedUser(String username) {
        sp.edit().putString(KEY_USER, username).apply();
    }

    public String getLoggedUser() {
        return sp.getString(KEY_USER, null);
    }

    public void logout() {
        sp.edit().remove(KEY_USER).apply();
    }
}
