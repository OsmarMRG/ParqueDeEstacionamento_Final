package com.example.parquedeestacionamento.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.example.parquedeestacionamento.R;
import com.example.parquedeestacionamento.utils.SessionManager;

// Splash screen: mostra o logo e vai para o login ou para a app
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Espera 600ms e depois decide para onde ir
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            SessionManager session = new SessionManager(this);
            Class<?> target = session.isLoggedIn() ? MainActivity.class : LoginActivity.class;
            startActivity(new Intent(this, target));
            finish();
        }, 600);
    }
}
