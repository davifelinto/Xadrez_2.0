package com.projetoPOO.logic.Pecas;

import com.projetoPOO.logic.Movimento;
import com.projetoPOO.logic.Peca;
import com.projetoPOO.logic.Tabuleiro;
import com.projetoPOO.main.ControlaJogo;

import java.util.ArrayList;
import java.util.List;

public class Rei extends Peca {

    public Rei(boolean cor, int lin, int col) {
        super(cor, lin, col);
    }

    @Override
    public List<Movimento> movimentosValidos() {
        List<Movimento> moveValido = new ArrayList<>();
        int[] lin_k = {1, 1, 1, 0, 0, -1, -1, -1};
        int[] col_k = {-1, 0, 1, -1, 1, -1, 0, 1};
        boolean roqueR;
        boolean roqueD;
        int indice;
        if(this.isCor_Branca()){
            roqueR = ControlaJogo.isRoque_Rei_b();
            roqueD = ControlaJogo.isRoque_Dama_b();
            indice = 7;
        }
        else{
            roqueR = ControlaJogo.isRoque_Rei_p();
            roqueD = ControlaJogo.isRoque_Dama_p();
            indice = 0;
        }

        for(int i = 0; i < 8; i++){
            int lin = this.getPosicao().getLinha() + lin_k[i];
            int col = this.getPosicao().getColuna() + col_k[i];
            if(col < 8 && col >= 0 && lin < 8 && lin >= 0){
                if(eInimigo(this.getPosicao().getPeca(), Tabuleiro.getCasa(lin, col).getPeca())){
                    Movimento move = new Movimento(this.getPosicao(), Tabuleiro.getCasa(lin, col), this
                            ,Tabuleiro.getCasa(lin, col).getObj_peca());
                    moveValido.add(move);
                }else
                if(Tabuleiro.getCasa(lin, col).getPeca() == ' '){
                    Movimento move = new Movimento(this.getPosicao(), Tabuleiro.getCasa(lin, col), this);
                    moveValido.add(move);
                }
            }
        }
        int lin = this.getPosicao().getLinha();
        int col = this.getPosicao().getColuna();
        //Roque
        if(!verificaAtaque()){
            if(roqueR && Tabuleiro.getCasa(lin, col+1).getPeca() == ' ' && Tabuleiro.getCasa(lin, col+2).getPeca() == ' '
                    && Tabuleiro.getCasa(indice, 7).getObj_peca() instanceof Torre){
                Tabuleiro.getCasa(lin, col+1).setPeca(this.getPosicao().getPeca());
                Tabuleiro.getCasa(lin, col).setPeca(' ');
                this.setPosicao(Tabuleiro.getCasa(lin, col+1));
                if(!(verificaAtaque())){
                    Movimento move = new Movimento(Tabuleiro.getCasa(lin, col), Tabuleiro.getCasa(lin, col+2), this,
                            Tabuleiro.getCasa(indice, 7).getObj_peca());
                    moveValido.add(move);
                }
                this.setPosicao(Tabuleiro.getCasa(lin, col));
                Tabuleiro.getCasa(lin, col).setPeca(Tabuleiro.getCasa(lin, col+1).getPeca());
                Tabuleiro.getCasa(lin, col+1).setPeca(' ');
            }
            if(roqueD && Tabuleiro.getCasa(lin, col-1).getPeca() == ' ' && Tabuleiro.getCasa(lin, col-2).getPeca() == ' '
                    && Tabuleiro.getCasa(lin, col-3).getPeca() == ' ' && Tabuleiro.getCasa(indice, 0).getObj_peca() instanceof Torre){
                //nao esta verificando se leva cheque nesa posicao
                Tabuleiro.getCasa(lin, col-1).setPeca(this.getPosicao().getPeca());
                Tabuleiro.getCasa(lin, col).setPeca(' ');
                this.setPosicao(Tabuleiro.getCasa(lin, col-1));
                if(!(verificaAtaque())){
                    Movimento move = new Movimento(Tabuleiro.getCasa(lin, col), Tabuleiro.getCasa(lin, col-2), this,
                            Tabuleiro.getCasa(indice, 0).getObj_peca());
                    moveValido.add(move);
                }
                this.setPosicao(Tabuleiro.getCasa(lin, col));
                Tabuleiro.getCasa(lin, col).setPeca(Tabuleiro.getCasa(lin, col-1).getPeca());
                Tabuleiro.getCasa(lin, col-1).setPeca(' ');
            }
        }
        return moveValido;
    }

}