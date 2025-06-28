package com.exemplo.aplicativopressao;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText emailEditText; // Declaração correta das variáveis
    private EditText senhaEditText;
    private Button loginButton;
    private Button cadastroButton;

    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "PressaoAppPrefs";
    private static final String KEY_LOGGED_IN = "loggedIn";
    private static final String KEY_EMAIL = "email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Layout XML para login/cadastro

        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        // Se já estiver logado, vai direto para Dashboard
        if (sharedPreferences.getBoolean(KEY_LOGGED_IN, false)) {
            startActivity(new Intent(MainActivity.this, DashboardActivity.class));
            finish();
        }


        emailEditText = findViewById(R.id.edit_email);
        senhaEditText = findViewById(R.id.edit_password);


        loginButton = findViewById(R.id.btn_login);
        cadastroButton = findViewById(R.id.btn_cadastro);

        // Configura o OnClickListener para o botão de login
        loginButton.setOnClickListener(v -> login());

        // Configura o OnClickListener para o botão de cadastro
        cadastroButton.setOnClickListener(v -> {
            // Certifique-se que CadastroActivity.class existe
            startActivity(new Intent(MainActivity.this, CadastroActivity.class));
        });
    }

    private void login() {
        // As variáveis emailEditText e senhaEditText agora não devem ser nulas
        String email = emailEditText.getText().toString().trim();
        String senha = senhaEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(senha)) {
            // R.string.preencha_todos_os_campos precisa estar no seu strings.xml
            Toast.makeText(this, R.string.preencha_todos_os_campos, Toast.LENGTH_SHORT).show();
            return;
        }

        // Obter os dados do usuário cadastrado (se houver)
        String userEmail = sharedPreferences.getString(KEY_EMAIL, null);
        String userSenha = sharedPreferences.getString("password", null); // "password" é a chave usada no SharedPreferences para a senha

        // Lógica de autenticação
        if (email.equals(userEmail) && senha.equals(userSenha)) {
            // Se o login for bem-sucedido, salva o estado de login
            sharedPreferences.edit().putBoolean(KEY_LOGGED_IN, true).apply();
            // R.string.login_efetuado_sucesso precisa estar no seu strings.xml
            Toast.makeText(this, R.string.login_efetuado_sucesso, Toast.LENGTH_SHORT).show();
            // Redireciona para a Dashboard
            startActivity(new Intent(MainActivity.this, DashboardActivity.class));
            finish(); // Finaliza a MainActivity para que o usuário não possa voltar para ela com o botão Voltar
        } else {
            // R.string.email_senha_incorretos precisa estar no seu strings.xml
            Toast.makeText(this, R.string.email_senha_incorretos, Toast.LENGTH_SHORT).show();
        }
    }
}