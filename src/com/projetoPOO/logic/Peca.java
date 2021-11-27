package com.projetoPOO.logic;

import com.projetoPOO.logic.Pecas.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Peca implements Serializable{

    private Casa posicao;
    private boolean cor_Branca;

    public Peca(boolean cor ,int lin, int col){
        setPosicao(Tabuleiro.getCasa(lin, col));
        this.cor_Branca = cor;
    }

    public boolean eInimigo(char atual, char outro){
        if(Character.isLowerCase(atual))
            if(Character.isUpperCase(outro))
                return true;
            else
                return false;
        else if(Character.isUpperCase(atual))
            if(Character.isLowerCase(outro))
                return true;
            else
                return false;
        return false;
    }
    //getters and setters
    public void setCor_Branca(boolean cor_Branca){
        this.cor_Branca = cor_Branca;
    }
    public boolean isCor_Branca() {
        return cor_Branca;
    }
    public void setPosicao(Casa posicao){
        this.posicao = posicao;
    }
    public Casa getPosicao() {
        return posicao;
    }
    public List<Movimento> filtraLista(Peca rei){
        List<Movimento> listaOriginal = movimentosValidos();
        List<Movimento> listaFiltrada = new ArrayList<>();
        for(Movimento m : listaOriginal){
            char aux = m.getCasaDestino().getPeca();
            m.getCasaDestino().setPeca(m.getCasaInicial().getPeca());
            m.getCasaInicial().setPeca(' ');
            this.setPosicao(m.getCasaDestino());
            if(!(rei.verificaAtaque()))
                listaFiltrada.add(m);
            this.setPosicao(m.getCasaInicial());
            m.getCasaInicial().setPeca(m.getCasaDestino().getPeca());
            m.getCasaDestino().setPeca(aux);
        }
        return listaFiltrada;
    }
    public abstract List<Movimento> movimentosValidos();
    public boolean verificaAtaque(){
        if(verifica_bispo())
            return true;
        if(verifica_peao())
            return true;
        if(verifica_torre())
            return true;
        if(verifica_rei())
            return true;
        if(verifica_cavalo())
            return true;
        return false;
    }
    public boolean verifica_peao(){
        int col = this.getPosicao().getColuna();
        int lin = this.getPosicao().getLinha();
        int indice;
        if(this.isCor_Branca())
            indice = +1;
        else
            indice = -1;
        if(col + indice < 8 && col + indice >= 0 && lin - indice < 8 && lin - indice >= 0){
            if(Tabuleiro.getCasa(lin-indice, col+indice).getObj_peca() instanceof Peao && eInimigo(this.getPosicao().getPeca(), Tabuleiro.getCasa(lin-indice, col+indice).getPeca()))
                return true;
        }
        if(col - indice < 8 && col - indice >= 0 && lin - indice < 8 && lin - indice >= 0){
            if(Tabuleiro.getCasa(lin-indice, col-indice).getObj_peca() instanceof Peao && eInimigo(this.getPosicao().getPeca(), Tabuleiro.getCasa(lin-indice, col-indice).getPeca()))
                return true;
        }
        return false;
    }
    public boolean verifica_torre(){
        int col = this.getPosicao().getColuna();
        int lin = this.getPosicao().getLinha()+1;
        //percorre parte 1 da linha
        while(lin < 8 && Tabuleiro.getCasa(lin, col).getPeca() == ' '){
            lin++;
        }
        //se achar alguma peca que seja da cor oposta e seja uma torre ou dama, entao retorna true
        if(lin < 8 && eInimigo(this.getPosicao().getPeca(), Tabuleiro.getCasa(lin, col).getPeca())
                && (Tabuleiro.getCasa(lin, col).getObj_peca() instanceof Torre || Tabuleiro.getCasa(lin, col).getObj_peca() instanceof Dama)){
            return true;
        }
        //percorre a parte 1 da coluna
        lin = this.getPosicao().getLinha();
        col = this.getPosicao().getColuna()+1;
        while(col < 8 && Tabuleiro.getCasa(lin, col).getPeca() == ' '){
            col++;
        }
        if(col < 8 && eInimigo(this.getPosicao().getPeca(), Tabuleiro.getCasa(lin, col).getPeca())
                && (Tabuleiro.getCasa(lin, col).getObj_peca() instanceof Torre || Tabuleiro.getCasa(lin, col).getObj_peca() instanceof Dama)){
            return true;
        }
        //percorre a parte 2 da linha
        lin = this.getPosicao().getLinha()-1;
        col = this.getPosicao().getColuna();
        while(lin >= 0 && Tabuleiro.getCasa(lin, col).getPeca() == ' '){
            lin--;
        }
        if(lin >= 0 && eInimigo(this.getPosicao().getPeca(), Tabuleiro.getCasa(lin, col).getPeca())
                && (Tabuleiro.getCasa(lin, col).getObj_peca() instanceof Torre || Tabuleiro.getCasa(lin, col).getObj_peca() instanceof Dama)){
            return true;
        }
        //percorre a parte 2 da coluna
        lin = this.getPosicao().getLinha();
        col = this.getPosicao().getColuna()-1;
        while(col >= 0 && Tabuleiro.getCasa(lin, col).getPeca() == ' '){
            col--;
        }
        if(col >= 0 && eInimigo(this.getPosicao().getPeca(), Tabuleiro.getCasa(lin, col).getPeca())
                && (Tabuleiro.getCasa(lin, col).getObj_peca() instanceof Torre || Tabuleiro.getCasa(lin, col).getObj_peca() instanceof Dama)){
            return true;
        }
        return false;
    }
    public boolean verifica_rei(){
        int[] lin_k = {1, 1, 1, 0, 0, -1, -1, -1};
        int[] col_k = {-1, 0, 1, -1, 1, -1, 0, 1};
        for(int i = 0; i < 8; i++){
            int lin = this.getPosicao().getLinha() + lin_k[i];
            int col = this.getPosicao().getColuna() + col_k[i];
            if(col < 8 && col >= 0 && lin < 8 && lin >= 0){
                if(eInimigo(this.getPosicao().getPeca(), Tabuleiro.getCasa(lin, col).getPeca())
                        && (Tabuleiro.getCasa(lin, col).getObj_peca() instanceof Rei)){
                    return true;
                }
            }
        }
        return false;
    }
    public boolean verifica_cavalo(){
        int[] col_c = {2, 1, -1, -2,-2, -1, 1, 2};
        int[] lin_c = {1, 2, 2, 1, -1, -2,-2, -1};
        for(int i = 0; i < 8; i++){
            int lin = this.getPosicao().getLinha() + lin_c[i];
            int col = this.getPosicao().getColuna() + col_c[i];
            if(col < 8 && col >= 0 && lin < 8 && lin >= 0){
                if(eInimigo(this.getPosicao().getPeca(), Tabuleiro.getCasa(lin, col).getPeca())
                        && (Tabuleiro.getCasa(lin, col).getObj_peca() instanceof Cavalo)){
                    return true;
                }
            }
        }
        return false;
    }
    public boolean verifica_bispo(){
        int col = this.getPosicao().getColuna()+1;
        int lin = this.getPosicao().getLinha()+1;
        //percorre a diagonal 1 até achar uma peca ou ate achar a borda do tabuleiro
        while(lin < 8 && col < 8 && Tabuleiro.getCasa(lin, col).getPeca() == ' '){
            lin++;
            col++;;
        }
        //se achar alguma peca que seja da cor oposta e seja um bispo ou dama, retorna true
        if(lin < 8 && col < 8 && eInimigo(this.getPosicao().getPeca(), Tabuleiro.getCasa(lin, col).getPeca())
                && (Tabuleiro.getCasa(lin, col).getObj_peca() instanceof Bispo || Tabuleiro.getCasa(lin, col).getObj_peca() instanceof Dama)){
            return true;
        }
        //diagonal 2
        col = this.getPosicao().getColuna() - 1; lin = this.getPosicao().getLinha() - 1;
        while(lin >= 0 && col >= 0 && Tabuleiro.getCasa(lin, col).getPeca() == ' '){
            col--;
            lin--;
        }
        if(lin >= 0 && col >= 0 && eInimigo(this.getPosicao().getPeca(), Tabuleiro.getCasa(lin, col).getPeca())
                && (Tabuleiro.getCasa(lin, col).getObj_peca() instanceof Bispo || Tabuleiro.getCasa(lin, col).getObj_peca() instanceof Dama)){
            return true;
        }
        //diagonal 3
        col = this.getPosicao().getColuna() - 1; lin = this.getPosicao().getLinha() + 1;
        while(lin < 8 && col >= 0 && Tabuleiro.getCasa(lin, col).getPeca() == ' '){
            col--;
            lin++;
        }
        if(lin < 8 && col >= 0 && eInimigo(this.getPosicao().getPeca(), Tabuleiro.getCasa(lin, col).getPeca())
                && (Tabuleiro.getCasa(lin, col).getObj_peca() instanceof Bispo || Tabuleiro.getCasa(lin, col).getObj_peca() instanceof Dama)){
            return true;
        }
        //diagonal 4
        col = this.getPosicao().getColuna() + 1; lin = this.getPosicao().getLinha() - 1;
        while(lin >= 0 && col < 8 && Tabuleiro.getCasa(lin, col).getPeca() == ' '){
            col++;
            lin--;
        }
        if(lin >= 0 && col < 8 && eInimigo(this.getPosicao().getPeca(), Tabuleiro.getCasa(lin, col).getPeca())
                && (Tabuleiro.getCasa(lin, col).getObj_peca() instanceof Bispo || Tabuleiro.getCasa(lin, col).getObj_peca() instanceof Dama)){
            return true;
        }
        return false;
    }
}
