package com.projetoPOO.logic.Pecas;

import com.projetoPOO.logic.Movimento;
import com.projetoPOO.logic.Peca;
import com.projetoPOO.logic.Tabuleiro;

import java.util.ArrayList;
import java.util.List;

public class Cavalo extends Peca {

    public Cavalo(boolean cor, int lin, int col) {
        super(cor, lin, col);
    }

    @Override
    public List<Movimento> movimentosValidos() {
        List<Movimento> moveValido = new ArrayList<>();
        int[] col_c = {2, 1, -1, -2,-2, -1, 1, 2};
        int[] lin_c = {1, 2, 2, 1, -1, -2,-2, -1};
        for(int i = 0; i < 8; i++){
            int lin = this.getPosicao().getLinha() + lin_c[i];
            int col = this.getPosicao().getColuna() + col_c[i];
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
        return moveValido;
    }
}