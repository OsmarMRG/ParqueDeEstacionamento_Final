package com.example.parquedeestacionamento.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.parquedeestacionamento.R;
import com.example.parquedeestacionamento.data.local.db.AppDatabase;
import com.example.parquedeestacionamento.data.local.dao.UserDao;
import com.example.parquedeestacionamento.data.local.entities.UserEntity;
import com.example.parquedeestacionamento.utils.SessionManager;

import java.util.concurrent.Executors;

public class LoginPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ImageView imgLogo = findViewById(R.id.imgLogo);
        EditText etUser = findViewById(R.id.etUser);
        EditText etPass = findViewById(R.id.etPass);
        CheckBox cbShowPass = findViewById(R.id.cbShowPass);
        Button btnLogin = findViewById(R.id.btnLogin);

        // animação simples do logo (fade + zoom suave)
        imgLogo.setAlpha(0f);
        imgLogo.setScaleX(0.9f);
        imgLogo.setScaleY(0.9f);
        imgLogo.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(500)
                .start();

        cbShowPass.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                etPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            } else {
                etPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
            etPass.setSelection(etPass.getText().length());
        });

        AppDatabase db = AppDatabase.getInstance(this);
        UserDao userDao = db.userDao();
        SessionManager session = new SessionManager(this);

        // cria utilizador admin uma vez (para testes)
        Executors.newSingleThreadExecutor().execute(() -> {
            if (userDao.countUsers() == 0) {
                userDao.insert(new UserEntity("admin", "1234"));
            }
        });

        btnLogin.setOnClickListener(v -> {
            String user = etUser.getText().toString().trim();
            String pass = etPass.getText().toString().trim();

            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Preenche todos os campos", Toast.LENGTH_SHORT).show();
                return;
            }

            Executors.newSingleThreadExecutor().execute(() -> {
                UserEntity u = userDao.findByUsername(user);

                runOnUiThread(() -> {
                    if (u == null || !pass.equals(u.password)) {
                        Toast.makeText(this, "Utilizador ou password errados", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    session.login(user);
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                });
            });
        });
    }
}
