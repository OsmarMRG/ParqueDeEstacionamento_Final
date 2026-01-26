package com.example.parquedeestacionamento.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.example.parquedeestacionamento.R;
import com.example.parquedeestacionamento.utils.SessionManager;

/**
 * Activity de splash screen.
 * Exibe o logótipo da aplicação durante um breve período antes de
 * redirecionar para o login ou main activity conforme o estado da sessão.
 */
public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_DELAY_MS = 600;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        navigateAfterDelay();
    }

    /**
     * Navega para a próxima activity após o delay do splash.
     * Vai para MainActivity se houver sessão ativa, caso contrário para
     * LoginActivity.
     */
    private void navigateAfterDelay() {
        SessionManager sessionManager = new SessionManager(this);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Class<?> targetActivity = sessionManager.isLoggedIn()
                    ? MainActivity.class
                    : LoginActivity.class;

            Intent intent = new Intent(this, targetActivity);
            startActivity(intent);
            finish();
        }, SPLASH_DELAY_MS);
    }
}
