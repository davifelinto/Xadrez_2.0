package com.projetoPOO.logic.Pecas;

import com.projetoPOO.logic.Movimento;
import com.projetoPOO.logic.Peca;
import com.projetoPOO.logic.Tabuleiro;

import java.util.ArrayList;
import java.util.List;

public class Bispo extends Peca {


    public Bispo(boolean cor, int lin, int col) {
        super(cor, lin, col);
    }

    @Override
    public List<Movimento> movimentosValidos() {
        List<Movimento> moveValido = new ArrayList<>();
        //diagonal Nordeste
        int col = this.getPosicao().getColuna()+1;
        int lin = this.getPosicao().getLinha()+1;
        while(lin < 8 && col < 8 && Tabuleiro.getCasa(lin, col).getPeca() == ' '){
            Movimento move = new Movimento(this.getPosicao(), Tabuleiro.getCasa(lin, col), this);
            moveValido.add(move);
            lin++;
            col++;;
        }
        //captura
        if(lin < 8 && col < 8 && eInimigo(this.getPosicao().getPeca(), Tabuleiro.getCasa(lin, col).getPeca())){
            Movimento move = new Movimento(this.getPosicao(), Tabuleiro.getCasa(lin, col), this
                    ,Tabuleiro.getCasa(lin, col).getObj_peca());
            moveValido.add(move);
        }
        //diagonal Sudoeste
        col = this.getPosicao().getColuna() - 1; lin = this.getPosicao().getLinha() - 1;
        while(lin >= 0 && col >= 0 && Tabuleiro.getCasa(lin, col).getPeca() == ' '){
            Movimento move = new Movimento(this.getPosicao(), Tabuleiro.getCasa(lin, col), this);
            moveValido.add(move);
            col--;
            lin--;
        }
        //captura
        if(lin >= 0 && col >= 0 && eInimigo(this.getPosicao().getPeca(), Tabuleiro.getCasa(lin, col).getPeca())){
            Movimento move = new Movimento(this.getPosicao(), Tabuleiro.getCasa(lin, col), this
                    ,Tabuleiro.getCasa(lin, col).getObj_peca());
            moveValido.add(move);
        }
        //diagonal Noroeste
        col = this.getPosicao().getColuna() - 1; lin = this.getPosicao().getLinha() + 1;
        while(lin < 8 && col >= 0 && Tabuleiro.getCasa(lin, col).getPeca() == ' '){
            Movimento move = new Movimento(this.getPosicao(), Tabuleiro.getCasa(lin, col), this
                    ,Tabuleiro.getCasa(lin, col).getObj_peca());
            moveValido.add(move);
            col--;
            lin++;
        }
        //captura
        if(lin < 8 && col >= 0 && eInimigo(this.getPosicao().getPeca(), Tabuleiro.getCasa(lin, col).getPeca())){
            Movimento move = new Movimento(this.getPosicao(), Tabuleiro.getCasa(lin, col), this
                    ,Tabuleiro.getCasa(lin, col).getObj_peca());
            moveValido.add(move);
        }
        //diagonal Sudeste
        col = this.getPosicao().getColuna() + 1; lin = this.getPosicao().getLinha() - 1;
        while(lin >= 0 && col < 8 && Tabuleiro.getCasa(lin, col).getPeca() == ' '){
            Movimento move = new Movimento(this.getPosicao(), Tabuleiro.getCasa(lin, col), this);
            moveValido.add(move);
            col++;
            lin--;
        }
        //captura
        if(lin >= 0 && col < 8 && eInimigo(this.getPosicao().getPeca(), Tabuleiro.getCasa(lin, col).getPeca())){
            Movimento move = new Movimento(this.getPosicao(), Tabuleiro.getCasa(lin, col), this
                    ,Tabuleiro.getCasa(lin, col).getObj_peca());
            moveValido.add(move);
        }
        return moveValido;
    }
}
