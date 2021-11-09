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

    public static BufferedImage sprite = Xadrez.spritesheet.getSprite(0*64,0*64,64,64);
    public static BufferedImage peaoBranco = Xadrez.spritesheet.getSprite(0*64,0*64,64,64);;
    public static BufferedImage peaoPreto = Xadrez.spritesheet.getSprite(0*64,1*64,64,64);;
    public static BufferedImage torreBranca = Xadrez.spritesheet.getSprite(1*64,0*64,64,64);;
    public static BufferedImage torrePreta = Xadrez.spritesheet.getSprite(1*64,1*64,64,64);;
    public static BufferedImage cavaloBranco = Xadrez.spritesheet.getSprite(2*64,0*64,64,64);;
    public static BufferedImage cavaloPreto = Xadrez.spritesheet.getSprite(2*64,1*64,64,64);;
    public static BufferedImage bispoBranco = Xadrez.spritesheet.getSprite(3*64,0*64,64,64);;
    public static BufferedImage bispoPreto = Xadrez.spritesheet.getSprite(3*64,1*64,64,64);;
    public static BufferedImage rainhaBranca = Xadrez.spritesheet.getSprite(4*64,0*64,64,64);;
    public static BufferedImage rainhaPreta = Xadrez.spritesheet.getSprite(4*64,1*64,64,64);;
    public static BufferedImage reiBranco = Xadrez.spritesheet.getSprite(5*64,0*64,64,64);;
    public static BufferedImage reiPreto = Xadrez.spritesheet.getSprite(5*64,1*64,64,64);;

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
