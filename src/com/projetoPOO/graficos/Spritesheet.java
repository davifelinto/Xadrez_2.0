package com.projetoPOO.graficos;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Spritesheet {
    private static BufferedImage spritesheet;
    public static Image[] getSpriteSheetPieces2(int tam) throws IOException{
        spritesheet = ImageIO.read(new File("res/chessPiecesSprite.png"));
        Image img[] = new Image[12];
        int ind = 11;
        for(int y = 0; y < 128; y+=64){
            for(int x = 0; x < 384; x += 64){
                img[ind] = spritesheet.getSubimage(x, y, 64, 64).getScaledInstance(tam, tam, BufferedImage.SCALE_SMOOTH);
                ind--;
            }
        }
        Image aux = img[1];
        img[1] = img[3];
        img[3] = aux;


        aux = img[7];
        img[7] = img[9];
        img[9] = aux;
        return img;
    }
    public static Image[] getSpriteSheetPieces(int tam) throws IOException{
        spritesheet = ImageIO.read(new File("res/ChessPieceSpriteSheetV2.png"));
        Image img[] = new Image[12];
        int ind = 0;
        for(int y = 0; y < 128; y+=64){
            for(int x = 0; x < 384; x += 64){
                img[ind] = spritesheet.getSubimage(x, y, 64, 64).getScaledInstance(tam, tam, BufferedImage.SCALE_SMOOTH);
                ind++;
            }
        }
        return img;
    }
    public static Image[] getSelected(int tam) throws IOException{
        spritesheet = ImageIO.read(new File("res/ChessSelected.png"));
        Image[] chessSelected = new Image[2];
        chessSelected[0] = spritesheet.getSubimage(0, 0, 64, 64).getScaledInstance(tam, tam, BufferedImage.SCALE_SMOOTH);
        chessSelected[1] = spritesheet.getSubimage(64, 0, 64, 64).getScaledInstance(tam, tam, BufferedImage.SCALE_SMOOTH);

        return chessSelected;
    }
    public Spritesheet(String path){
        try {
            spritesheet = ImageIO.read(getClass().getResource(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Metodo para pegar uma parte da imagem, como uma peca na imagem das pecas.
    public BufferedImage getSprite(int x, int y, int width, int height){
        return spritesheet.getSubimage(x, y, width, height);
    }
}