package com.projetoPOO.main;

import java.util.ArrayList;
import java.util.List;

import com.projetoPOO.logic.Peca;
import com.projetoPOO.logic.Tabuleiro;

public class Jogador {
    private boolean cor_Branca;
    private String nome;
    private List<Peca> suasPecas = new ArrayList<>();
    private List<Peca> capturadas = new ArrayList<>();

    public Jogador(boolean isBranco, String nome){
        this.cor_Branca = isBranco;
        this.nome = nome;
        this.suasPecas = Tabuleiro.getSetPecas(isBranco);
        
    }

    public boolean isCor_Branca() {
        return cor_Branca;
    }
    public List<Peca> getSuasPecas() {
        return suasPecas;
    }
    public void setSuasPecas(List<Peca> suasPecas) {
        this.suasPecas = suasPecas;
    }
    public List<Peca> getCapturadas() {
        return capturadas;
    }
    public void setCapturadas(List<Peca> capturadas) {
        this.capturadas = capturadas;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public void setCor_Branca(boolean cor_Branca) {
        this.cor_Branca = cor_Branca;
    }
}