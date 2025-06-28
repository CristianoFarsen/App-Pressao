package com.example.appdiabetes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText emailEditText, senhaEditText;
    private Button loginButton, cadastroButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Verifica se o usuário já está logado
        SharedPreferences preferences = getSharedPreferences("usuario", MODE_PRIVATE);
        boolean isLoggedIn = preferences.getBoolean("loggedIn", false);

        if (isLoggedIn) {
            // Se sim, vai direto para o Dashboard
            startActivity(new Intent(this, DashboardActivity.class));
            finish(); // Finaliza esta activity
            return;
        }

        // Carrega layout normalmente
        setContentView(R.layout.activity_main);

        // Mapeamento dos elementos
        emailEditText = findViewById(R.id.email);
        senhaEditText = findViewById(R.id.senha);
        loginButton = findViewById(R.id.loginButton);
        cadastroButton = findViewById(R.id.cadastroButton);

        // Ação do botão de login
        loginButton.setOnClickListener(v -> {
            String emailDigitado = emailEditText.getText().toString();
            String senhaDigitada = senhaEditText.getText().toString();

            // Busca o email e senha cadastrados no SharedPreferences
            String emailCadastrado = preferences.getString("email", "");
            String senhaCadastrada = preferences.getString("senha", "");

            if (emailDigitado.equals(emailCadastrado) && senhaDigitada.equals(senhaCadastrada)) {
                preferences.edit().putBoolean("loggedIn", true).apply();

                startActivity(new Intent(MainActivity.this, DashboardActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Credenciais inválidas", Toast.LENGTH_SHORT).show();
            }
        });

        // Ação do botão de cadastro
        cadastroButton.setOnClickListener(v -> {
            startActivity(new Intent(this, CadastroActivity.class));
        });
    }
}
