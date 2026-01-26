package com.example.parquedeestacionamento.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.example.parquedeestacionamento.R;
import com.example.parquedeestacionamento.utils.SessionManager;

public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_DELAY_MS = 600;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SessionManager session = new SessionManager(this);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(
                    this,
                    session.isLoggedIn() ? MainActivity.class : LoginPage.class
            );
            startActivity(intent);
            finish();
        }, SPLASH_DELAY_MS);
    }
}
