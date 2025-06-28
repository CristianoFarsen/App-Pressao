package com.exemplo.aplicativopressao; // NOVO PACOTE

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CadastroActivity extends AppCompatActivity {

    private EditText nomeEditText, emailEditText, senhaEditText;
    private Button cadastrarButton;

    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "PressaoAppPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro); // Layout XML para cadastro

        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        nomeEditText = findViewById(R.id.nome);
        emailEditText = findViewById(R.id.email);
        senhaEditText = findViewById(R.id.senha);
        cadastrarButton = findViewById(R.id.cadastrarButton);

        cadastrarButton.setOnClickListener(v -> cadastrar());
    }

    private void cadastrar() {
        String nome = nomeEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String senha = senhaEditText.getText().toString().trim();

        if (TextUtils.isEmpty(nome) || TextUtils.isEmpty(email) || TextUtils.isEmpty(senha)) {
            Toast.makeText(this, R.string.preencha_todos_os_campos, Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("nome", nome);
        editor.putString("email", email);
        editor.putString("password", senha);
        editor.apply();

        Toast.makeText(this, R.string.usuario_cadastrado_sucesso, Toast.LENGTH_SHORT).show();

        startActivity(new Intent(CadastroActivity.this, MainActivity.class));
        finish();
    }
}