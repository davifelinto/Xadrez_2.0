package com.projetoPOO.entities;

import com.projetoPOO.main.Xadrez;

import java.awt.image.BufferedImage;

public class Player extends Entity{

    public int posX, posY;
    public int speed = 64;

    public Player(int x, int y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, sprite);
    }

    // Logica da classe player. Como ela se comporta com a mudanca de variaveis.
    // (colocar index do outro codigo como variavel de controle)
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
