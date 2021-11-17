package com.projetoPOO.logic;

import com.projetoPOO.logic.Pecas.Peao;

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
    public String getNotacao(Movimento m){
        String notacao = "";
        if(m.getPecaCapturada() == null){//Movimento Simples
            if(!(m.getPecaMovimentada() instanceof Peao))
                notacao += Tabuleiro.retornaPeca(m.pecaMovimentada.getPosicao().getPeca());
            //evitar ambiguidade
            notacao += (char)(m.getCasaDestino().getColuna() + 97);
            notacao += converterLinha(m.getCasaDestino().getLinha());
        }else if(m.getPecaCapturada().isCor_Branca() == m.getPecaMovimentada().isCor_Branca()){//Roque
            notacao += "O-O";
            if(m.getCasaDestino().getColuna() == 2)
                notacao += "-O";
        }else{//Captura
            if(m.getPecaMovimentada() instanceof Peao)  notacao += (char)(m.getCasaInicial().getColuna() + 97);
            else notacao += Tabuleiro.retornaPeca(m.pecaMovimentada.getPosicao().getPeca());
            notacao += "x";
            //evitar ambiguidade
            notacao += (char)(m.getCasaDestino().getColuna() + 97);
            notacao += converterLinha(m.getCasaDestino().getLinha());;
        }
        return notacao;
    }
    private int converterLinha(int i) {
        return 8-i;
    }
}
/*if(m.pecaMovimentada instanceof Torre || m.pecaMovimentada instanceof Dama){
                Peca ambigua = null;
                for(int i = 0; i < 8; i++){
                    if(Tabuleiro.getCasa(m.getCasaDestino().getLinha(), i).getPeca() == m.pecaMovimentada.getPosicao().getPeca()
                    && Tabuleiro.getCasa(m.getCasaDestino().getLinha(), i).getObj_peca() != m.pecaMovimentada
                    ){
                        ambigua = Tabuleiro.getCasa(m.getCasaDestino().getLinha(), i).getObj_peca();
                        break;
                    }
                    if(Tabuleiro.getCasa(i, m.getCasaInicial().getColuna()).getPeca() == m.pecaMovimentada.getPosicao().getPeca()
                    && Tabuleiro.getCasa(m.getCasaDestino().getLinha(), i).getObj_peca() != m.pecaMovimentada){
                        ambigua = Tabuleiro.getCasa(i, m.getCasaInicial().getColuna()).getObj_peca();
                        break;
                    }
                }
                if(ambigua != null){
                    if(m.pecaMovimentada.getPosicao().getLinha() == ambigua.getPosicao().getLinha()) 
                        notacao += (char)m.getCasaInicial().getLinha() + 1;
                    else 
                        notacao += (char)m.getCasaInicial().getLinha() + 1;
                }  
            }  
            if(m.pecaMovimentada instanceof Bispo || m.pecaMovimentada instanceof Dama){
                Peca ambigua = null;
                int col = m.getCasaDestino().getColuna();
                int lin = m.getCasaDestino().getLinha();
                if(ambigua != null){
                    if(m.pecaMovimentada.getPosicao().getLinha() == ambigua.getPosicao().getLinha()) 
                        notacao += (char)m.getCasaInicial().getLinha() + 1;
                    else 
                        notacao += (char)m.getCasaInicial().getLinha() + 1;
                } 
            }*/