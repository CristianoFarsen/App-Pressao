package com.exemplo.aplicativopressao; // Certifique-se de que este é o seu pacote correto

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class BancoDadosHelper extends SQLiteOpenHelper {

    // --- Constantes do Banco de Dados ---
    private static final String DATABASE_NAME = "pressaoapp.db";
    // ATENÇÃO: Incrementamos a versão NOVAMENTE para forçar o onUpgrade
    // Esta versão 4 é a que remove a coluna 'pulso'
    private static final int DATABASE_VERSION = 4;

    // --- Constantes da Tabela de Pressão ---
    public static final String TABELA_PRESSAO = "pressao";
    public static final String COLUNA_ID = "id";
    public static final String COLUNA_SISTOLICA = "sistolica";
    public static final String COLUNA_DIASTOLICA = "diastolica";
    // public static final String COLUNA_PULSO = "pulso"; // <-- REMOVIDO: Não mais usado no esquema
    public static final String COLUNA_DATA_HORA = "dataHora"; // Tipo alterado para TEXT
    public static final String COLUNA_OBSERVACOES = "observacoes";

    public BancoDadosHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL para criar a tabela de Pressão (AGORA SEM A COLUNA PULSO)
        String criarTabelaPressao = "CREATE TABLE " + TABELA_PRESSAO + " (" +
                COLUNA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUNA_SISTOLICA + " INTEGER NOT NULL," +
                COLUNA_DIASTOLICA + " INTEGER NOT NULL," +
                // COLUNA_PULSO + " INTEGER NOT NULL," + // <-- REMOVIDO: Não mais incluído na tabela
                COLUNA_DATA_HORA + " TEXT NOT NULL," +
                COLUNA_OBSERVACOES + " TEXT" +
                ");";
        db.execSQL(criarTabelaPressao);

        // A tabela de glicose foi mantida. Se você não a usa mais, pode remover este bloco.
        String criarTabelaGlicose = "CREATE TABLE IF NOT EXISTS glicose (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "valor INTEGER NOT NULL," +
                "observacoes TEXT," +
                "timestamp INTEGER" +
                ");";
        db.execSQL(criarTabelaGlicose);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


        if (oldVersion < 4) {

            db.execSQL("DROP TABLE IF EXISTS " + TABELA_PRESSAO);

            db.execSQL("DROP TABLE IF EXISTS glicose");


            onCreate(db);
        }

    }

    // --- Métodos de Operação no Banco de Dados ---

    // Método para adicionar um novo registro de pressão (AGORA SEM O PULSO)
    public boolean addRegistroPressao(PressaoModel pressao) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUNA_SISTOLICA, pressao.getSistolica());
        cv.put(COLUNA_DIASTOLICA, pressao.getDiastolica());
        // cv.put(COLUNA_PULSO, pressao.getPulso()); // <-- REMOVIDO: Não mais inserido
        cv.put(COLUNA_DATA_HORA, pressao.getDataHora());
        cv.put(COLUNA_OBSERVACOES, pressao.getObservacoes());

        long insert = db.insert(TABELA_PRESSAO, null, cv);
        db.close();

        return insert != -1; // Retorna true se a inserção foi bem-sucedida
    }

    // Método para obter todos os registros de pressão (AGORA SEM O PULSO)
    public List<PressaoModel> getTodosRegistrosPressao() {
        List<PressaoModel> listaRegistros = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] colunas = {
                COLUNA_ID,
                COLUNA_SISTOLICA,
                COLUNA_DIASTOLICA,
                COLUNA_DATA_HORA,
                COLUNA_OBSERVACOES
        };

        // Consulta o banco de dados, ordenando pela data/hora
        Cursor cursor = db.query(TABELA_PRESSAO, colunas, null, null, null, null, COLUNA_DATA_HORA + " ASC");

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUNA_ID));
                int sistolica = cursor.getInt(cursor.getColumnIndexOrThrow(COLUNA_SISTOLICA));
                int diastolica = cursor.getInt(cursor.getColumnIndexOrThrow(COLUNA_DIASTOLICA));
                // int pulso = cursor.getInt(cursor.getColumnIndexOrThrow(COLUNA_PULSO)); // <-- REMOVIDO: Não mais recuperado
                String dataHora = cursor.getString(cursor.getColumnIndexOrThrow(COLUNA_DATA_HORA));
                String observacoes = cursor.getString(cursor.getColumnIndexOrThrow(COLUNA_OBSERVACOES));

                // CORREÇÃO AQUI: Chame o construtor do PressaoModel que não inclui o pulso
                PressaoModel pressao = new PressaoModel(id, sistolica, diastolica, dataHora, observacoes); // <-- CORRIGIDO
                listaRegistros.add(pressao);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listaRegistros;
    }
}