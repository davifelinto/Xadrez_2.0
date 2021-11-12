package com.projetoPOO.logic;

import com.projetoPOO.entities.Entity;
import com.projetoPOO.main.Xadrez;

import java.awt.image.BufferedImage;

public class Jogador extends Entity {

    private boolean cor_Branca, mousePressed;
    public int posX, posY, mX, mY;

    public Jogador(int x, int y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, sprite);
    }

    public boolean isCor_Branca() {
        return cor_Branca;
    }

    public void setCor_Branca(boolean cor_Branca) {
        this.cor_Branca = cor_Branca;
    }

    // Logica da classe jogador. Como ela se comporta com a mudanca de variaveis.
    // (colocar index do outro codigo como variavel de controle)
    @Override
    public void tick() {
        x += posX;
        y += posY;
        if (x+width > Xadrez.width)
            x = Xadrez.width - width;
        else if (x< 0)
            x = 0;
        if (y+height > Xadrez.height)
            y = Xadrez.height - height;
        else if (y < 0)
            y = 0;
    }
}
