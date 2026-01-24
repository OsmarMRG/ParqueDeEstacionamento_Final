package com.example.parquedeestacionamento.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.parquedeestacionamento.R;
import com.example.parquedeestacionamento.data.local.AppDatabase;
import com.example.parquedeestacionamento.data.local.UserEntity;
import com.example.parquedeestacionamento.utils.AppExecutors;
import com.example.parquedeestacionamento.utils.SessionManager;

public class LoginPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SessionManager sm = new SessionManager(this);
        if (sm.getLoggedUser() != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_login);

        EditText etUser = findViewById(R.id.etUser);
        EditText etPass = findViewById(R.id.etPass);
        Button btnLogin = findViewById(R.id.btnLogin);

        AppDatabase db = AppDatabase.getInstance(this);

        btnLogin.setOnClickListener(v -> {
            String u = etUser.getText().toString().trim();
            String p = etPass.getText().toString().trim();

            if (u.isEmpty() || p.isEmpty()) {
                Toast.makeText(this, "Preenche utilizador e password", Toast.LENGTH_SHORT).show();
                return;
            }

            AppExecutors.io().execute(() -> {
                UserEntity user = db.userDao().getByUsername(u);

                if (user == null) {
                    db.userDao().insert(new UserEntity(u, p));
                    runOnUiThread(() -> {
                        sm.setLoggedUser(u);
                        Toast.makeText(this, "Conta criada e login feito", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, MainActivity.class));
                        finish();
                    });
                    return;
                }

                if (!user.password.equals(p)) {
                    runOnUiThread(() ->
                            Toast.makeText(this, "Password errada", Toast.LENGTH_SHORT).show()
                    );
                    return;
                }

                runOnUiThread(() -> {
                    sm.setLoggedUser(u);
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                });
            });
        });
    }
}
