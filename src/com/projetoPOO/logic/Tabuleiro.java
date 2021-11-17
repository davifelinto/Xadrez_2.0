package com.projetoPOO.logic;

import com.projetoPOO.logic.Pecas.*;
import com.projetoPOO.main.ControlaJogo;
import com.projetoPOO.main.Xadrez2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.awt.*;

public class Tabuleiro {
    private static Casa[][] casa = new Casa[8][8];
    private static List<Peca> pecasBrancas = new ArrayList<>();
    private static List<Peca> pecasPretas = new ArrayList<>();

    //Getters and setters
    public static List<Peca> getSetPecas(boolean cor_Branca) {
        if(cor_Branca)
            return pecasBrancas;
        else
            return pecasPretas;
    }
    public static Casa getCasa(int lin, int col) {
        return casa[lin][col];
    }
    public static void setCasa(Casa peca, int lin, int col) {
        Tabuleiro.casa[lin][col] = peca;
    }

    //Metodos
    public static void carregaTabuleiro(String n){
        char notacao[] = n.toCharArray();
        boolean flag = true;
        int col = 0, lin = 0;

        for(char i : notacao){
            if(flag){
                if(Character.isLetter(i)){
                    Casa novaCasa = new Casa(lin, col, i);
                    casa[lin][col] = novaCasa;
                    casa[lin][col].setObj_peca(instanciaPeca(i, lin, col));
                    col++;
                }else if(i == '/'){
                    lin++;
                    col = 0;
                }else if(Character.isDigit(i)){
                    int max = col + Character.getNumericValue(i);
                    for(; col < max; col++){
                        Casa novaCasa = new Casa(lin, col, ' ');
                        casa[lin][col] = novaCasa;
                    }
                }else if(i == ' '){
                    flag = false;
                }
            }else{
                switch (i) {
                    case 'w':
                        ControlaJogo.setTurno_Branco(true);
                        break;
                    case 'b':
                        ControlaJogo.setTurno_Branco(false);
                        break;
                    case 'K':
                        ControlaJogo.setRoque_Rei_b(true);
                        break;
                    case 'Q':
                        ControlaJogo.setRoque_Dama_b(true);
                        break;
                    case 'k':
                        ControlaJogo.setRoque_Rei_p(true);
                        break;
                    case 'q':
                        ControlaJogo.setRoque_Dama_p(true);
                        break;
                    default:
                        break;
                }
            }
        }
        ControlaJogo.fen_Atual = n;
    }
    //Funcao para salvar a partida num arquivo txt
    public static void gravaTabuleiro(String s) throws IOException {
        File f = new File("partidaAnterior.txt");
        FileWriter w = new FileWriter("partidaAnterior.txt");

        try {
            f.createNewFile();
        } catch (Exception e) {
            System.out.println("Salvando no arquivo existente: partidaAnterior.bin");
        }
        try {
            w.write(s);
            w.close();
        } catch (Exception e) {
            System.out.println("Erro ao salvar no arquivo: partidaAnterior.bin");
            e.printStackTrace();
        }
    }
    //Funcao para ler a partida do arquivo txt
    public static String leTabuleiro() throws FileNotFoundException {
        String tab = "";
        try {
            File f = new File("partidaAnterior.txt");
            Scanner ler = new Scanner(f);
            if (ler.hasNextLine()) {
                tab = ler.nextLine();
            }
            ler.close();
            return tab;
        } catch (FileNotFoundException e) {
            System.out.println("Não existe arquivo de partida salva!");
            e.printStackTrace();
        }
        // Se não tiver arquivo retorna um jogo novo
        return "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    }
    // Funcao assessoria pra salvar em arquivo. Transforma tabuleiro em string FEN
    public static String tabuleiroParaString(boolean isTurno_Branco){
        String s = "";

        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if(Tabuleiro.getCasa(i, j).getPeca() != ' '){
                    s = s + Tabuleiro.getCasa(i, j).getPeca();
                }
                else if(Tabuleiro.getCasa(i, j).getPeca() == ' '){
                    s = s + '1';
                }
                Tabuleiro.getCasa(i, j).getPeca();
            }
            if (i<7)
                s = s + '/';
        }

        if(isTurno_Branco)
            s = s + " w ";
        else
            s = s + " b ";

        if(ControlaJogo.isRoque_Rei_b())
            s = s + "K";
        if(ControlaJogo.isRoque_Dama_b())
            s = s + "Q";
        if(ControlaJogo.isRoque_Rei_p())
            s = s + "k";
        if(ControlaJogo.isRoque_Dama_b())
            s = s + "q";

        return s;
    }
    public static Peca instanciaPeca(char p, int lin, int col){
        boolean corB;
        Peca peca;
        if(Character.isLowerCase(p))
            corB = false;
        else
            corB = true;
        switch (Character.toLowerCase(p)){
            case 'p':
                peca = new Peao(corB, lin, col);
                break;
            case 'r':
                peca = new Torre(corB, lin, col);
                break;
            case 'n':
                peca = new Cavalo(corB, lin, col);
                break;
            case 'b':
                peca = new Bispo(corB, lin, col);
                break;
            case 'q':
                peca = new Dama(corB, lin, col);
                break;
            case 'k':
                peca = new Rei(corB, lin, col);
                break;
            default:
                peca = null;
                break;
        }
        adicionarPecaLista(peca);
        return peca;
    }
    public static void adicionarPecaLista(Peca p){
        if(p.isCor_Branca())
            getSetPecas(true).add(p);
        else
            getSetPecas(false).add(p);
    }
    public static void limpaTela (){
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    public static void limpaMovimentos(){
        for(int i = 0; i < 8; i++)
            for(int j = 0; j < 8; j++)
                if(Tabuleiro.getCasa(i, j).getPeca() == '.')
                    Tabuleiro.getCasa(i, j).setPeca(' ');
    }
    public static void imprimeTabuleiro(){
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if(Character.isUpperCase(Tabuleiro.getCasa(i, j).getPeca()))
                    System.out.print("\u001B[37m");
                else
                    System.out.print("\u001B[30m");
                System.out.print(Tabuleiro.getCasa(i, j).getPeca());
            }
            System.out.println();
        }
    }
    public static char retornaPeca(char peca){
        switch (peca) {
            case 'P':
                peca = '♙';
                break;
            case 'p':
                peca = '♟';
                break;
            case 'R':
                peca = '♖';
                break;
            case 'r':
                peca = '♜';
                break;
            case 'N':
                peca = '♘';
                break;
            case 'n':
                peca = '♞';
                break;
            case 'B':
                peca = '♗';
                break;
            case 'b':
                peca = '♝';
                break;
            case 'Q':
                peca = '♕';
                break;
            case 'q':
                peca = '♛';
                break;
            case 'K':
                peca = '♔';
                break;
            case 'k':
                peca = '♚';
            break;
        }
        return peca;
    }
    public static Image retornaPeca(char peca, int tam) throws IOException{
        Image img[] = Xadrez2.temaPecas.someMethod(tam);
        Image pecaImage = null;
        switch (peca) {
            case 'P':
                pecaImage = img[0];
                break;
            case 'p':
                pecaImage = img[6];
                break;
            case 'R':
                pecaImage = img[1];
                break;
            case 'r':
                pecaImage = img[7];
                break;
            case 'N':
                pecaImage = img[2];
                break;
            case 'n':
                pecaImage = img[8];
                break;
            case 'B':
                pecaImage = img[3];
                break;
            case 'b':
                pecaImage = img[9];
                break;
            case 'Q':
                pecaImage = img[4];
                break;
            case 'q':
                pecaImage = img[10];
                break;
            case 'K':
                pecaImage = img[5];
                break;
            case 'k':
                pecaImage = img[11];
                break;
        }
        return pecaImage;
    }

}
