package com.exemplo.aplicativopressao;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {

    private Button btnRegistroPressao, btnHistorico, btnGraficos, btnLembretes, btnSair;
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "PressaoAppPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        btnRegistroPressao = findViewById(R.id.btnRegistroPressao);
        btnHistorico = findViewById(R.id.historicoButton);
        btnGraficos = findViewById(R.id.btnGraficos);
        btnLembretes = findViewById(R.id.btnLembretes);
        btnSair = findViewById(R.id.btnSair);

        btnRegistroPressao.setOnClickListener(v -> {
            startActivity(new Intent(DashboardActivity.this, RegistroPressaoActivity.class));
        });

        btnHistorico.setOnClickListener(v -> {
            startActivity(new Intent(DashboardActivity.this, HistoricoPressaoActivity.class));
        });

        btnGraficos.setOnClickListener(v -> {
            startActivity(new Intent(DashboardActivity.this, GraficosActivity.class));
        });

        btnLembretes.setOnClickListener(v -> {
            startActivity(new Intent(DashboardActivity.this, LembretesActivity.class));
        });

        btnSair.setOnClickListener(v -> {
            sharedPreferences.edit().putBoolean("loggedIn", false).apply();
            startActivity(new Intent(DashboardActivity.this, MainActivity.class));
            finish();
        });
    }
}
