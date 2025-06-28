package com.exemplo.aplicativopressao;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;
import java.util.ArrayList; // Importar ArrayList

public class HistoricoPressaoActivity extends AppCompatActivity {

    private ListView historicoListView;
    private BancoDadosHelper bancoDadosHelper;
    private List<PressaoModel> listaRegistros;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico_pressao);

        historicoListView = findViewById(R.id.historicoListView);
        bancoDadosHelper = new BancoDadosHelper(this);

        carregarHistorico();
    }

    private void carregarHistorico() {
        // Obter todos os registros do banco de dados
        listaRegistros = bancoDadosHelper.getTodosRegistrosPressao();

        // Preparar os dados para exibição na ListView
        // Vamos criar uma lista de Strings formatadas para exibir
        List<String> dadosParaExibir = new ArrayList<>();
        if (listaRegistros != null && !listaRegistros.isEmpty()) {
            for (PressaoModel registro : listaRegistros) {
                String item = "Data/Hora: " + registro.getDataHora() +
                        "\nSistólica: " + registro.getSistolica() +
                        ", Diastólica: " + registro.getDiastolica();
                if (registro.getObservacoes() != null && !registro.getObservacoes().isEmpty()) {
                    item += "\nObs: " + registro.getObservacoes();
                }
                dadosParaExibir.add(item);
            }
        } else {
            dadosParaExibir.add("Nenhum registro de pressão encontrado.");
        }

        // Criar um ArrayAdapter para conectar a lista de Strings à ListView
        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1, // Layout padrão para um item de lista simples
                dadosParaExibir
        );

        // Definir o adaptador na ListView
        historicoListView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Recarrega o histórico sempre que a Activity volta para o primeiro plano
        carregarHistorico();
    }
}