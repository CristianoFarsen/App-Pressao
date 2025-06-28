package com.exemplo.aplicativopressao;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.graphics.Color; // Import para cores
import android.widget.TextView; // Import para o TextView

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter; // Para formatar o eixo X

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections; // Para ordenar a lista
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GraficosActivity extends AppCompatActivity {

    private LineChart lineChart;
    private BancoDadosHelper dbHelper; // Instância do seu BancoDadosHelper

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graficos);

        lineChart = findViewById(R.id.line_chart);
        dbHelper = new BancoDadosHelper(this); // Inicializa o BancoDadosHelper

        loadPressureDataAndDisplayGraph();
    }

    private void loadPressureDataAndDisplayGraph() {
        // Lista para armazenar os dados do banco
        List<PressaoModel> listaMedicoes = dbHelper.getTodosRegistrosPressao();

        // Ordena a lista pela data (do mais antigo para o mais novo)
        // Isso é importante para que o gráfico faça sentido no eixo X
        Collections.sort(listaMedicoes, (m1, m2) -> {
            // Supondo que a data está em formato String "dd/MM/yyyy HH:mm:ss"
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
            try {
                Date d1 = sdf.parse(m1.getDataHora());
                Date d2 = sdf.parse(m2.getDataHora());
                return d1.compareTo(d2);
            } catch (Exception e) {
                e.printStackTrace();
                return 0; // Em caso de erro na data, não ordena
            }
        });


        // Listas para os valores sistólicos e diastólicos no gráfico
        List<Entry> entriesSistolica = new ArrayList<>();
        List<Entry> entriesDiastolica = new ArrayList<>();
        // Lista para os rótulos do eixo X (datas/horas)
        List<String> xLabels = new ArrayList<>();

        // Preenche as listas de Entry e os rótulos do eixo X
        for (int i = 0; i < listaMedicoes.size(); i++) {
            PressaoModel medicao = listaMedicoes.get(i);
            entriesSistolica.add(new Entry(i, medicao.getSistolica()));
            entriesDiastolica.add(new Entry(i, medicao.getDiastolica()));
            // Usa o índice 'i' como valor X e a data/hora como rótulo
            xLabels.add(medicao.getDataHora());
        }

        // Se não houver dados, exibe uma mensagem
        if (listaMedicoes.isEmpty()) {
            TextView title = findViewById(R.id.tv_titulo_grafico);
            title.setText("Nenhum registro de pressão encontrado para exibir no gráfico.");
            lineChart.setVisibility(View.GONE); // Esconde o gráfico
            return;
        }

        // Criar os DataSets (conjuntos de dados para as linhas do gráfico)
        LineDataSet dataSetSistolica = new LineDataSet(entriesSistolica, "Sistólica");
        dataSetSistolica.setColor(Color.RED); // Cor da linha sistólica
        dataSetSistolica.setCircleColor(Color.RED); // Cor dos pontos
        dataSetSistolica.setLineWidth(2f); // Espessura da linha
        dataSetSistolica.setCircleRadius(3f); // Tamanho dos pontos
        dataSetSistolica.setValueTextSize(10f); // Tamanho do texto dos valores
        dataSetSistolica.setMode(LineDataSet.Mode.LINEAR); // Linha reta entre os pontos

        LineDataSet dataSetDiastolica = new LineDataSet(entriesDiastolica, "Diastólica");
        dataSetDiastolica.setColor(Color.BLUE); // Cor da linha diastólica
        dataSetDiastolica.setCircleColor(Color.BLUE);
        dataSetDiastolica.setLineWidth(2f);
        dataSetDiastolica.setCircleRadius(3f);
        dataSetDiastolica.setValueTextSize(10f);
        dataSetDiastolica.setMode(LineDataSet.Mode.LINEAR);

        // Adicionar os DataSets ao objeto LineData
        LineData lineData = new LineData(dataSetSistolica, dataSetDiastolica);
        lineChart.setData(lineData);

        // Configurar o eixo X (horizontal)
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xLabels)); // Usa os rótulos de data/hora
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // Posição dos rótulos
        xAxis.setGranularity(1f); // Garante que todos os rótulos sejam exibidos
        xAxis.setLabelRotationAngle(-45); // Roda os rótulos para melhor visualização (se houver muitos)
        xAxis.setDrawGridLines(false); // Remove linhas de grade

        // Configurar o eixo Y (vertical) - Esquerda
        lineChart.getAxisLeft().setAxisMinimum(0f); // Começa em 0
        lineChart.getAxisLeft().setAxisMaximum(200f); // Valor máximo (ajuste conforme necessário)
        lineChart.getAxisLeft().setDrawGridLines(true);

        // Desabilitar o eixo Y (vertical) - Direita (geralmente não precisamos de dois)
        lineChart.getAxisRight().setEnabled(false);

        // Configurações gerais do gráfico
        Description description = new Description();
        description.setText("Medições ao longo do tempo");
        description.setTextSize(12f);
        lineChart.setDescription(description); // Descrição do gráfico
        lineChart.setNoDataText("Nenhum dado de pressão para exibir."); // Mensagem se não houver dados
        lineChart.invalidate(); // Atualiza o gráfico
        lineChart.animateX(1500); // Animação ao exibir o gráfico (opcional)
    }
}