package com.projetoPOO.logic.Pecas;

import com.projetoPOO.logic.Movimento;
import com.projetoPOO.logic.Peca;
import com.projetoPOO.logic.Tabuleiro;

import java.util.ArrayList;
import java.util.List;

public class Torre extends Peca {

    public Torre(boolean cor, int lin, int col) {
        super(cor, lin, col);
    }

    @Override
    public List<Movimento> movimentosValidos() {
        List<Movimento> moveValido = new ArrayList<>();
        int col = this.getPosicao().getColuna();
        int lin = this.getPosicao().getLinha()+1;
        //ate onde ele pode se movimentar verticalmente pra cima
        while(lin < 8 && Tabuleiro.getCasa(lin, col).getPeca() == ' '){
            Movimento move = new Movimento(this.getPosicao(), Tabuleiro.getCasa(lin, col), this);
            moveValido.add(move);
            lin++;
        }
        //captura
        if(lin < 8 && eInimigo(this.getPosicao().getPeca(), Tabuleiro.getCasa(lin, col).getPeca())){
            Movimento move = new Movimento(this.getPosicao(), Tabuleiro.getCasa(lin, col), this
                    ,Tabuleiro.getCasa(lin, col).getObj_peca());
            moveValido.add(move);
        }
        //ate onde ele pode se movimentar horizontalmente pra direita
        lin = this.getPosicao().getLinha();
        col = this.getPosicao().getColuna()+1;
        while(col < 8 && Tabuleiro.getCasa(lin, col).getPeca() == ' '){
            Movimento move = new Movimento(this.getPosicao(), Tabuleiro.getCasa(lin, col), this);
            moveValido.add(move);
            col++;
        }
        //captura
        if(col < 8 && eInimigo(this.getPosicao().getPeca(), Tabuleiro.getCasa(lin, col).getPeca())){
            Movimento move = new Movimento(this.getPosicao(), Tabuleiro.getCasa(lin, col), this
                    ,Tabuleiro.getCasa(lin, col).getObj_peca());
            moveValido.add(move);
        }
        //ate onde ele pode se movimentar verticalmente pra baixo
        lin = this.getPosicao().getLinha()-1;
        col = this.getPosicao().getColuna();
        while(lin >= 0 && Tabuleiro.getCasa(lin, col).getPeca() == ' '){
            Movimento move = new Movimento(this.getPosicao(), Tabuleiro.getCasa(lin, col), this);
            moveValido.add(move);
            lin--;
        }
        //captura
        if(lin >= 0 && eInimigo(this.getPosicao().getPeca(), Tabuleiro.getCasa(lin, col).getPeca())){
            Movimento move = new Movimento(this.getPosicao(), Tabuleiro.getCasa(lin, col), this
                    ,Tabuleiro.getCasa(lin, col).getObj_peca());
            moveValido.add(move);
        }

        //ate onde ele pode se movimentar horizontalmente pra esquerda
        lin = this.getPosicao().getLinha();
        col = this.getPosicao().getColuna()-1;
        while(col >= 0 && Tabuleiro.getCasa(lin, col).getPeca() == ' '){
            Movimento move = new Movimento(this.getPosicao(), Tabuleiro.getCasa(lin, col), this);
            moveValido.add(move);
            col--;
        }
        //captura
        if(col >= 0 && eInimigo(this.getPosicao().getPeca(), Tabuleiro.getCasa(lin, col).getPeca())){
            Movimento move = new Movimento(this.getPosicao(), Tabuleiro.getCasa(lin, col), this
                    ,Tabuleiro.getCasa(lin, col).getObj_peca());
            moveValido.add(move);
        }
        return moveValido;
    }
}
