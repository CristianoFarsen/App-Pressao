package com.exemplo.aplicativopressao;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RegistroPressaoActivity extends AppCompatActivity {

    private EditText sistolicaEditText, diastolicaEditText, observacoesEditText;
    private Button salvarButton;
    private BancoDadosHelper bancoDadosHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_pressao);

        sistolicaEditText = findViewById(R.id.valorSistolica);
        diastolicaEditText = findViewById(R.id.valorDiastolica);
        observacoesEditText = findViewById(R.id.observacoes);
        salvarButton = findViewById(R.id.salvarButton);

        bancoDadosHelper = new BancoDadosHelper(this);

        salvarButton.setOnClickListener(v -> salvarRegistro());
    }

    private void salvarRegistro() {
        String sistolicaStr = sistolicaEditText.getText().toString().trim();
        String diastolicaStr = diastolicaEditText.getText().toString().trim();
        String observacoes = observacoesEditText.getText().toString().trim();

        // Validação: Verificar se sistólica e diastólica estão preenchidas
        if (TextUtils.isEmpty(sistolicaStr) || TextUtils.isEmpty(diastolicaStr)) {
            Toast.makeText(this, R.string.preencha_todos_os_campos, Toast.LENGTH_SHORT).show();
            return;
        }

        int sistolica;
        int diastolica;
        try {
            sistolica = Integer.parseInt(sistolicaStr);
            diastolica = Integer.parseInt(diastolicaStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, R.string.valores_pressao_invalidos, Toast.LENGTH_SHORT).show();
            return;
        }

        // Validação de faixa de valores (mantida como está)
        if (sistolica < 50 || sistolica > 250 || diastolica < 30 || diastolica > 150) {
            Toast.makeText(this, R.string.valores_pressao_fora_faixa, Toast.LENGTH_LONG).show();
        }

        // Pega a data e hora atual e formata como string para a coluna dataHora
        String dataHoraAtual = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());

        // Cria um objeto PressaoModel com os dados (sem o pulso)
        // O ID é -1 porque ele será auto-incrementado pelo banco de dados
        PressaoModel novoRegistro = new PressaoModel(-1, sistolica, diastolica, dataHoraAtual, observacoes);


        boolean sucesso = bancoDadosHelper.addRegistroPressao(novoRegistro);

        if (sucesso) {
            Toast.makeText(this, R.string.registro_pressao_salvo_sucesso, Toast.LENGTH_SHORT).show();
            // Limpa os campos após o sucesso
            sistolicaEditText.setText("");
            diastolicaEditText.setText("");
            observacoesEditText.setText("");
            finish(); // Volta para a tela anterior (Dashboard)
        } else {
            Toast.makeText(this, R.string.erro_salvar_registro_pressao, Toast.LENGTH_SHORT).show();
        }
    }
}