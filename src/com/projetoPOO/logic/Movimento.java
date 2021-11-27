package com.projetoPOO.logic;

import java.io.Serializable;

import com.projetoPOO.logic.Pecas.*;

public class Movimento implements Serializable{

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
            notacao += evitaAmb(m);
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
            notacao += evitaAmb(m);
            notacao += (char)(m.getCasaDestino().getColuna() + 97);
            notacao += converterLinha(m.getCasaDestino().getLinha());
        }
        return notacao;
    }
    private int converterLinha(int i) {
        return 8-i;
    }
    
public String evitaAmb(Movimento m){
    String coluna = "", linha = "";
    Peca[] ambigua = new Peca[9];
    int cont = 0;
    if(m.pecaMovimentada instanceof Cavalo){
        int[] col_c = {2, 1, -1, -2,-2, -1, 1, 2};
        int[] lin_c = {1, 2, 2, 1, -1, -2,-2, -1};
        for(int i = 0; i < 8; i++){
            int lin = m.getCasaDestino().getLinha() + lin_c[i];
            int col = m.getCasaDestino().getColuna() + col_c[i];
            if(col < 8 && col >= 0 && lin < 8 && lin >= 0){
                if(m.pecaMovimentada.getPosicao().getPeca() == Tabuleiro.getCasa(lin, col).getPeca()
                && m.pecaMovimentada != Tabuleiro.getCasa(lin, col).getObj_peca()){
                    ambigua[cont] = Tabuleiro.getCasa(lin, col).getObj_peca();
                    cont++;
                }
            }
        }
    }else
    if(m.pecaMovimentada instanceof Torre || m.pecaMovimentada instanceof Dama){
        int col = m.getCasaDestino().getColuna();
        int lin = m.getCasaDestino().getLinha()+1;
        while(lin < 8 && Tabuleiro.getCasa(lin, col).getPeca() == ' '){
            lin++;
        }
        if(lin < 8 && Tabuleiro.getCasa(lin, col).getPeca() == m.pecaMovimentada.getPosicao().getPeca()
        && Tabuleiro.getCasa(lin, col).getObj_peca() != m.pecaMovimentada){
            ambigua[cont] = Tabuleiro.getCasa(lin, col).getObj_peca();
            cont++;
        }
        col = m.getCasaDestino().getColuna()+1;
        lin = m.getCasaDestino().getLinha();
        while(col < 8 && Tabuleiro.getCasa(lin, col).getPeca() == ' '){
            col++;
        }
        if(col < 8 && Tabuleiro.getCasa(lin, col).getPeca() == m.pecaMovimentada.getPosicao().getPeca()
        && Tabuleiro.getCasa(lin, col).getObj_peca() != m.pecaMovimentada){
            ambigua[cont] = Tabuleiro.getCasa(lin, col).getObj_peca();
            cont++;
        }
        col = m.getCasaDestino().getColuna();
        lin = m.getCasaDestino().getLinha()-1;
        while(lin >= 0 && Tabuleiro.getCasa(lin, col).getPeca() == ' '){
            lin--;
        }
        if(lin >= 0 && Tabuleiro.getCasa(lin, col).getPeca() == m.pecaMovimentada.getPosicao().getPeca()
        && Tabuleiro.getCasa(lin, col).getObj_peca() != m.pecaMovimentada){
            ambigua[cont] = Tabuleiro.getCasa(lin, col).getObj_peca();
            cont++;
        }
        col = m.getCasaDestino().getColuna()-1;
        lin = m.getCasaDestino().getLinha();
        while(col >= 0 && Tabuleiro.getCasa(lin, col).getPeca() == ' '){
            col--;
        }
        if(col >= 0 && Tabuleiro.getCasa(lin, col).getPeca() == m.pecaMovimentada.getPosicao().getPeca()
        && Tabuleiro.getCasa(lin, col).getObj_peca() != m.pecaMovimentada){
            ambigua[cont] = Tabuleiro.getCasa(lin, col).getObj_peca();
            cont++;
        }
    }else
    if(m.pecaMovimentada instanceof Bispo || m.pecaMovimentada instanceof Dama){
        int col = m.getCasaDestino().getColuna()+1;
        int lin = m.getCasaDestino().getLinha()+1;
        while(lin < 8 && col < 8 && Tabuleiro.getCasa(lin, col).getPeca() == ' '){
            lin++;
            col++;
        }
        if(lin < 8 && col < 8 && Tabuleiro.getCasa(lin, col).getPeca() == m.pecaMovimentada.getPosicao().getPeca()
        && Tabuleiro.getCasa(lin, col).getObj_peca() != m.pecaMovimentada){
            ambigua[cont] = Tabuleiro.getCasa(lin, col).getObj_peca();
            cont++;
        }
        col = m.getCasaDestino().getColuna()  - 1; lin = m.getCasaDestino().getLinha() - 1;
        while(lin >= 0 && col >= 0 && Tabuleiro.getCasa(lin, col).getPeca() == ' '){
            col--;
            lin--;
        }
        if(lin >= 0 && col >= 0 && Tabuleiro.getCasa(lin, col).getPeca() == m.pecaMovimentada.getPosicao().getPeca()
        && Tabuleiro.getCasa(lin, col).getObj_peca() != m.pecaMovimentada){
            ambigua[cont] = Tabuleiro.getCasa(lin, col).getObj_peca();
            cont++;
        }
        col = m.getCasaDestino().getColuna()  - 1; lin = m.getCasaDestino().getLinha() + 1;
        while(lin < 8 && col >= 0 && Tabuleiro.getCasa(lin, col).getPeca() == ' '){
            col--;
            lin++;
        }
        if(lin < 8 && col >= 0 && Tabuleiro.getCasa(lin, col).getPeca() == m.pecaMovimentada.getPosicao().getPeca()
        && Tabuleiro.getCasa(lin, col).getObj_peca() != m.pecaMovimentada){
            ambigua[cont] = Tabuleiro.getCasa(lin, col).getObj_peca();
            cont++;
        }
        col = m.getCasaDestino().getColuna()  + 1; lin = m.getCasaDestino().getLinha() - 1;
        while(lin >= 0 && col < 8 && Tabuleiro.getCasa(lin, col).getPeca() == ' '){
            col++;
            lin--;
        }
        if(lin >= 0 && col < 8 && Tabuleiro.getCasa(lin, col).getPeca() == m.pecaMovimentada.getPosicao().getPeca()
        && Tabuleiro.getCasa(lin, col).getObj_peca() != m.pecaMovimentada){
            ambigua[cont] = Tabuleiro.getCasa(lin, col).getObj_peca();
            cont++;
        }
        
    }
    for(int i = 0; i < cont; i++){
        if(m.getCasaInicial().getLinha() == ambigua[i].getPosicao().getLinha())
            coluna = "" + (char)(m.getCasaInicial().getColuna() + 97);
        else if(m.getCasaInicial().getColuna() == ambigua[i].getPosicao().getColuna())
            linha = "" + converterLinha(m.getCasaInicial().getLinha());
        else 
            coluna = "" + (char)(m.getCasaInicial().getColuna() + 97);
    }
    return coluna+linha;
}            
}