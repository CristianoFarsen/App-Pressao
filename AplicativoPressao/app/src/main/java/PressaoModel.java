package com.exemplo.aplicativopressao;

public class PressaoModel {
    private int id;
    private int sistolica;
    private int diastolica;
    // private int pulso; // <-- REMOVIDO: A variável 'pulso' não é mais necessária
    private String dataHora;
    private String observacoes;

    // Construtor vazio (opcional, mas boa prática para alguns frameworks)
    public PressaoModel() {
    }

    // Construtor completo (sem 'pulso')
    public PressaoModel(int id, int sistolica, int diastolica, String dataHora, String observacoes) {
        this.id = id;
        this.sistolica = sistolica;
        this.diastolica = diastolica;
        // this.pulso = pulso; // Removido
        this.dataHora = dataHora;
        this.observacoes = observacoes;
    }

    // Construtor sem ID (para novos registros onde o ID será gerado pelo banco, sem 'pulso')
    public PressaoModel(int sistolica, int diastolica, String dataHora, String observacoes) {
        this.sistolica = sistolica;
        this.diastolica = diastolica;
        // this.pulso = pulso; // Removido
        this.dataHora = dataHora;
        this.observacoes = observacoes;
    }

    // Métodos Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSistolica() {
        return sistolica;
    }

    public void setSistolica(int sistolica) {
        this.sistolica = sistolica;
    }

    public int getDiastolica() {
        return diastolica;
    }

    public void setDiastolica(int diastolica) {
        this.diastolica = diastolica;
    }

    // REMOVIDO: Métodos getPulso() e setPulso() não são mais necessários
    /*
    public int getPulso() {
        return pulso;
    }

    public void setPulso(int pulso) {
        this.pulso = pulso;
    }
    */

    public String getDataHora() {
        return dataHora;
    }

    public void setDataHora(String dataHora) {
        this.dataHora = dataHora;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    @Override
    public String toString() { // Útil para depuração e exibição simples
        return "Data: " + dataHora + ", Sistólica: " + sistolica + ", Diastólica: " + diastolica;
    }
}