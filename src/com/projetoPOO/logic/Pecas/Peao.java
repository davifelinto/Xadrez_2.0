package com.projetoPOO.logic.Pecas;

import com.projetoPOO.logic.Movimento;
import com.projetoPOO.logic.Peca;
import com.projetoPOO.logic.Tabuleiro;
import com.projetoPOO.main.ControlaJogo;

import java.util.ArrayList;
import java.util.List;

public class Peao extends Peca {
    private int linhaPromocao;
    public Peao(boolean cor, int lin, int col) {
        super(cor, lin, col);
        if(this.isCor_Branca())
            linhaPromocao = 0;
        else
            linhaPromocao = 7;
    }
    public int getLinhaPromocao() {
        return linhaPromocao;
    }
    @Override
    public List<Movimento> movimentosValidos() {
        int indice;
        int pos_ini;
        List<Movimento> moveValido = new ArrayList<>();
        if(this.isCor_Branca()){
            indice = -1;
            pos_ini = 6;
        }
        else{
            indice = +1;
            pos_ini = 1;
        }
        int lin = this.getPosicao().getLinha();
        int col = this.getPosicao().getColuna();
        if(Tabuleiro.getCasa(lin+indice, col).getPeca() == ' '){
            Movimento move = new Movimento(this.getPosicao(), Tabuleiro.getCasa(lin+indice, col), this);
            moveValido.add(move);
            if(lin == pos_ini && Tabuleiro.getCasa(lin+(indice*2), col).getPeca() == ' '){
                move = new Movimento(this.getPosicao(), Tabuleiro.getCasa(lin+(indice*2), col), this);
                moveValido.add(move);
            }
        }
        //capturas
        if(col + indice < 8 && col + indice >= 0 && eInimigo(this.getPosicao().getPeca(), Tabuleiro.getCasa(lin+indice, col+indice).getPeca())){
            Movimento move = new Movimento(this.getPosicao(), Tabuleiro.getCasa(lin+indice, col+indice), this
                    ,Tabuleiro.getCasa(lin+indice, col+indice).getObj_peca());
            moveValido.add(move);
        }
        if(col - indice < 8 && col - indice >= 0 && eInimigo(this.getPosicao().getPeca(), Tabuleiro.getCasa(lin+indice, col-indice).getPeca())){
            Movimento move = new Movimento(this.getPosicao(), Tabuleiro.getCasa(lin+indice, col-indice), this
                    ,Tabuleiro.getCasa(lin+indice, col-indice).getObj_peca());
            moveValido.add(move);
        }
        //en passant
        List<Movimento> listaMove = ControlaJogo.getPgn();
        if(col + indice < 8 && col + indice >= 0 && lin == pos_ini + (indice*3)
                && listaMove.get((listaMove.size()) - 1).getPecaMovimentada() instanceof Peao //verifica se o ultimo movimento foi feito por um peao
                && listaMove.get((listaMove.size()) - 1).getCasaInicial() == Tabuleiro.getCasa(lin+(indice*2), col+indice)
                && listaMove.get((listaMove.size()) - 1).getCasaDestino() == Tabuleiro.getCasa(lin, col+indice)// e se esse peao pulou duas casas
        ){
            Peca pecaCapturada = Tabuleiro.getCasa(lin, col+indice).getObj_peca();
            Movimento move = new Movimento(this.getPosicao(), Tabuleiro.getCasa(lin+indice, col+indice), this
                    ,pecaCapturada);
            moveValido.add(move);
        }
        if(col - indice < 8 && col - indice >= 0 && lin == pos_ini + (indice*3)
                && listaMove.get((listaMove.size()) - 1).getPecaMovimentada() instanceof Peao
                && listaMove.get((listaMove.size()) - 1).getCasaInicial() == Tabuleiro.getCasa(lin+(indice*2), col-indice)
                && listaMove.get((listaMove.size()) - 1).getCasaDestino() == Tabuleiro.getCasa(lin, col-indice)
        ){
            Movimento move = new Movimento(this.getPosicao(), Tabuleiro.getCasa(lin+indice, col-indice), this
                    ,Tabuleiro.getCasa(lin, col-indice).getObj_peca());
            moveValido.add(move);
        }
        return moveValido;
    }
}