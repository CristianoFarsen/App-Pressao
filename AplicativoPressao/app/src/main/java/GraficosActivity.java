package com.exemplo.aplicativopressao;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;


import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.exemplo.aplicativopressao.PressaoModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GraficosActivity extends AppCompatActivity {

    private LineChart lineChart;
    private BancoDadosHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graficos);

        lineChart = findViewById(R.id.line_chart);
        dbHelper = new BancoDadosHelper(this);

        loadPressureDataAndDisplayGraph();
    }

    private void loadPressureDataAndDisplayGraph() {
        List<PressaoModel> listaMedicoes = dbHelper.getTodosRegistrosPressao();

        Collections.sort(listaMedicoes, (m1, m2) -> {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
            try {
                Date d1 = sdf.parse(m1.getDataHora());
                Date d2 = sdf.parse(m2.getDataHora());
                return d1.compareTo(d2);
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        });

        List<Entry> entriesSistolica = new ArrayList<>();
        List<Entry> entriesDiastolica = new ArrayList<>();
        List<String> xLabels = new ArrayList<>();

        for (int i = 0; i < listaMedicoes.size(); i++) {
            PressaoModel medicao = listaMedicoes.get(i);
            entriesSistolica.add(new Entry(i, medicao.getSistolica()));
            entriesDiastolica.add(new Entry(i, medicao.getDiastolica()));
            xLabels.add(medicao.getDataHora());
        }

        if (listaMedicoes.isEmpty()) {
            TextView title = findViewById(R.id.tv_titulo_grafico);
            title.setText("Nenhum registro de pressão encontrado para exibir no gráfico.");
            lineChart.setVisibility(View.GONE);
            return;
        }

        LineDataSet dataSetSistolica = new LineDataSet(entriesSistolica, "Sistólica");
        dataSetSistolica.setColor(Color.RED);
        dataSetSistolica.setCircleColor(Color.RED);
        dataSetSistolica.setLineWidth(2f);
        dataSetSistolica.setCircleRadius(3f);
        dataSetSistolica.setValueTextSize(10f);
        dataSetSistolica.setMode(LineDataSet.Mode.LINEAR);

        LineDataSet dataSetDiastolica = new LineDataSet(entriesDiastolica, "Diastólica");
        dataSetDiastolica.setColor(Color.BLUE);
        dataSetDiastolica.setCircleColor(Color.BLUE);
        dataSetDiastolica.setLineWidth(2f);
        dataSetDiastolica.setCircleRadius(3f);
        dataSetDiastolica.setValueTextSize(10f);
        dataSetDiastolica.setMode(LineDataSet.Mode.LINEAR);

        LineData lineData = new LineData(dataSetSistolica, dataSetDiastolica);
        lineChart.setData(lineData);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xLabels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setLabelRotationAngle(-45);
        xAxis.setDrawGridLines(false);

        lineChart.getAxisLeft().setAxisMinimum(0f);
        lineChart.getAxisLeft().setAxisMaximum(200f);
        lineChart.getAxisLeft().setDrawGridLines(true);

        lineChart.getAxisRight().setEnabled(false);

        Description description = new Description();
        description.setText("Medições ao longo do tempo");
        description.setTextSize(12f);
        lineChart.setDescription(description);
        lineChart.setNoDataText("Nenhum dado de pressão para exibir.");
        lineChart.invalidate();
        lineChart.animateX(1500);
    }
}