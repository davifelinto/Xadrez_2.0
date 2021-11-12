package com.projetoPOO.logic;

public class Movimento {

    private Casa casaInicial;
    private Casa casaDestino;
    private Peca pecaMovimentada;
    private Peca pecaCapturada;


    public Movimento(Casa casaInicial, Casa casaDestino, Peca pecaMovimentada, Peca pecaCapturada){
        this.casaInicial = casaInicial;
        this.casaDestino = casaDestino;
        this.pecaMovimentada = pecaMovimentada;
        this.pecaCapturada = pecaCapturada;
    }
    public Movimento(Casa casaInicial, Casa casaDestino, Peca pecaMovimentada){
        this.casaInicial = casaInicial;
        this.casaDestino = casaDestino;
        this.pecaMovimentada = pecaMovimentada;
        this.pecaCapturada = null;
    }
    public Peca getPecaCapturada() {
        return pecaCapturada;
    }
    public void setPecaCapturada(Peca pecaCapturada) {
        this.pecaCapturada = pecaCapturada;
    }
    public Peca getPecaMovimentada() {
        return pecaMovimentada;
    }
    public void setPecaMovimentada(Peca pecaMovimentada) {
        this.pecaMovimentada = pecaMovimentada;
    }
    public Casa getCasaDestino() {
        return casaDestino;
    }
    public void setCasaDestino(Casa casaDestino) {
        this.casaDestino = casaDestino;
    }
    public Casa getCasaInicial() {
        return casaInicial;
    }
    public void setCasaInicial(Casa casaInicial) {
        this.casaInicial = casaInicial;
    }
}
