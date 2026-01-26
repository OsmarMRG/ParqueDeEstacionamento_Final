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
import com.example.parquedeestacionamento.data.local.dao.UserDao;
import com.example.parquedeestacionamento.data.local.db.AppDatabase;
import com.example.parquedeestacionamento.data.local.entities.UserEntity;
import com.example.parquedeestacionamento.utils.AppExecutors;
import com.example.parquedeestacionamento.utils.Constants;
import com.example.parquedeestacionamento.utils.SessionManager;

/**
 * Activity de login da aplicação.
 * Permite autenticação de utilizadores e cria utilizador admin na primeira
 * execução.
 */
public class LoginActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;
    private UserDao userDao;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeDatabase();
        initializeViews();
        animateLogo();
        createDefaultAdminIfNeeded();
    }

    /**
     * Inicializa referências à base de dados.
     */
    private void initializeDatabase() {
        AppDatabase database = AppDatabase.getInstance(this);
        userDao = database.userDao();
        sessionManager = new SessionManager(this);
    }

    /**
     * Inicializa referências aos views e configura listeners.
     */
    private void initializeViews() {
        ImageView imgLogo = findViewById(R.id.imgLogo);
        etUsername = findViewById(R.id.etUser);
        etPassword = findViewById(R.id.etPass);
        CheckBox cbShowPassword = findViewById(R.id.cbShowPass);
        Button btnLogin = findViewById(R.id.btnLogin);

        // Listener para mostrar/esconder password
        cbShowPassword.setOnCheckedChangeListener((buttonView, isChecked) -> {
            int inputType = isChecked
                    ? InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    : InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;
            etPassword.setInputType(inputType);
            etPassword.setSelection(etPassword.getText().length());
        });

        btnLogin.setOnClickListener(v -> attemptLogin());
    }

    /**
     * Executa animação de entrada do logótipo.
     */
    private void animateLogo() {
        ImageView imgLogo = findViewById(R.id.imgLogo);
        imgLogo.setAlpha(0f);
        imgLogo.setScaleX(0.9f);
        imgLogo.setScaleY(0.9f);
        imgLogo.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(Constants.ANIMATION_DURATION_MS)
                .start();
    }

    /**
     * Cria utilizador admin por defeito se não existirem utilizadores.
     */
    private void createDefaultAdminIfNeeded() {
        AppExecutors.io().execute(() -> {
            if (userDao.countUsers() == 0) {
                UserEntity admin = new UserEntity(
                        Constants.DEFAULT_ADMIN_USERNAME,
                        Constants.DEFAULT_ADMIN_PASSWORD);
                userDao.insert(admin);
            }
        });
    }

    /**
     * Tenta fazer login com as credenciais introduzidas.
     */
    private void attemptLogin() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validação de campos vazios
        if (username.isEmpty() || password.isEmpty()) {
            showToast(R.string.error_empty_fields);
            return;
        }

        // Verificar credenciais em background
        AppExecutors.io().execute(() -> {
            UserEntity user = userDao.findByUsername(username);

            AppExecutors.main().execute(() -> {
                if (user == null || !password.equals(user.password)) {
                    showToast(R.string.error_invalid_credentials);
                    return;
                }

                // Login bem sucedido
                sessionManager.login(username);
                navigateToMain();
            });
        });
    }

    /**
     * Navega para a MainActivity após login bem sucedido.
     */
    private void navigateToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Mostra uma mensagem Toast.
     *
     * @param messageResId ID do recurso string
     */
    private void showToast(int messageResId) {
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
    }
}
