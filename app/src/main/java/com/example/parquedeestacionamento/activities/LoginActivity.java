package com.example.parquedeestacionamento.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.parquedeestacionamento.R;
import com.example.parquedeestacionamento.data.local.dao.UserDao;
import com.example.parquedeestacionamento.data.local.db.AppDatabase;
import com.example.parquedeestacionamento.data.local.entities.UserEntity;
import com.example.parquedeestacionamento.utils.AppExecutors;
import com.example.parquedeestacionamento.utils.Constants;
import com.example.parquedeestacionamento.utils.SessionManager;

// Ecrã de login: valida utilizador e password na base de dados Room
public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private UserDao userDao;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userDao = AppDatabase.getInstance(this).userDao();
        sessionManager = new SessionManager(this);

        etUsername = findViewById(R.id.etUser);
        etPassword = findViewById(R.id.etPass);
        CheckBox cbShowPass = findViewById(R.id.cbShowPass);
        Button btnLogin = findViewById(R.id.btnLogin);

        // Mostrar/esconder password
        cbShowPass.setOnCheckedChangeListener((view, isChecked) -> {
            etPassword.setInputType(isChecked
                    ? InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    : InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        });

        btnLogin.setOnClickListener(v -> attemptLogin());

        // Criar utilizador admin na primeira execução
        AppExecutors.io().execute(() -> {
            if (userDao.countUsers() == 0) {
                userDao.insert(new UserEntity(Constants.DEFAULT_ADMIN_USERNAME, Constants.DEFAULT_ADMIN_PASSWORD));
            }
        });
    }

    // Tenta fazer login com as credenciais introduzidas
    private void attemptLogin() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, R.string.error_empty_fields, Toast.LENGTH_SHORT).show();
            return;
        }

        AppExecutors.io().execute(() -> {
            UserEntity user = userDao.findByUsername(username);
            AppExecutors.main(() -> {
                if (user == null || !password.equals(user.password)) {
                    Toast.makeText(this, R.string.error_invalid_credentials, Toast.LENGTH_SHORT).show();
                    return;
                }
                sessionManager.login(username);
                startActivity(new Intent(this, MainActivity.class));
                finish();
            });
        });
    }
}
