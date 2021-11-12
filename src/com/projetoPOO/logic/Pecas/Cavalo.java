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
/*
function knight_movement(position_letter, position_number, color){
	tam = 0;
	//movimentos possiveis de se realizar indo pra col_c[i] e pra lin_c[i]
	col_c[0] = 2; col_c[1] = 1; col_c[2] = -1; col_c[3] = -2; col_c[4] = -2; col_c[5] = -1; col_c[6] = 1; col_c[7] = 2;
	lin_c[0] = 1; lin_c[1] = 2; lin_c[2] = 2; lin_c[3] = 1; lin_c[4] = -1; lin_c[5] = -2; lin_c[6] = -2; lin_c[7] = -1;
	for(i = 0; i < 8; i++){
		if(position_letter + col_c[i] < 9 && position_letter + col_c[i] > 0 && position_number + lin_c[i] < 9
		&& position_number + lin_c[i] > 0){
			if(pos[position_letter+col_c[i]][position_number+lin_c[i]].have_a_piece && pos[position_letter+col_c[i]][position_number+lin_c[i]].piece.cor != color){
				allow[tam] = pos[position_letter+col_c[i]][position_number + lin_c[i]];
				tam++;
			}else
			//capturar outra peca
			if(!pos[position_letter+col_c[i]][position_number+lin_c[i]].have_a_piece){
				allow[tam] = pos[position_letter+col_c[i]][position_number + lin_c[i]];
				tam++;
			}
		}
	}
}
*/