package com.projetoPOO.entities;

import com.projetoPOO.main.Xadrez;

import java.awt.*;
import java.awt.image.BufferedImage;

// Classe de controle para qualquer entidade que vai aparecer no jogo (pecas, player, movimentos).
public class Entity {
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    private BufferedImage sprite;

//    public static BufferedImage peaoBranco = Xadrez.spritesheet3.getSprite(0, 0,64,64);
//    public static BufferedImage peaoPreto = Xadrez.spritesheet3.getSprite(0, 63,64,64);
//    public static BufferedImage torreBranca = Xadrez.spritesheet3.getSprite(63, 0,64,64);
//    public static BufferedImage torrePreta = Xadrez.spritesheet3.getSprite(63, 63,64,64);
//    public static BufferedImage cavaloBranco = Xadrez.spritesheet3.getSprite(127, 0,64,64);
//    public static BufferedImage cavaloPreto = Xadrez.spritesheet3.getSprite(127, 63,64,64);
//    public static BufferedImage bispoBranco = Xadrez.spritesheet3.getSprite(191, 0,64,64);
//    public static BufferedImage bispoPreto = Xadrez.spritesheet3.getSprite(191, 63,64,64);
//    public static BufferedImage rainhaBranca = Xadrez.spritesheet3.getSprite(255, 0,64,64);
//    public static BufferedImage rainhaPreta = Xadrez.spritesheet3.getSprite(255, 63,64,64);
//    public static BufferedImage reiBranco = Xadrez.spritesheet3.getSprite(319, 0,64,64);
//    public static BufferedImage reiPreto = Xadrez.spritesheet3.getSprite(319, 63,64,64);

    public Entity(int x, int y, int width, int height, BufferedImage sprite) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.sprite = sprite;
    }

    public int getX() {
        return this.x;
    }
    public int getY() {
        return this.y;
    }
    public int getWidth() {
        return this.width;
    }
    public int getHeight() {
        return this.height;
    }

    public void tick() {

    }

    public void render(Graphics g) {
        g.drawImage(sprite, this.getX(), this.getY(), null);
    }
}
