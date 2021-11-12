package com.projetoPOO.main;

import com.projetoPOO.entities.Entity;
import com.projetoPOO.entities.Player;
import com.projetoPOO.graficos.Spritesheet;
import com.projetoPOO.logic.Jogador;
import com.projetoPOO.logic.Jogo;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Xadrez extends Canvas implements KeyListener, MouseListener, Runnable {

    //Variáveis globais
    public static JFrame frame;
    private Thread thread;
    private boolean isRunning = true;
    public static final int width = 512;
    public static final int height = 512;
    private final int scale = 1;

    private Graphics g;
    private BufferedImage image;
    private BufferedImage tabuleiro;
    private Jogador jogador;

    public static Spritesheet spritesheet;
    public static Spritesheet spritesheet2;
    public static Spritesheet spritesheet3;
    public List<Entity> entities;

    //Metodos

    //Criacao e configuracao da janela do jogo.
    public void iniciaJanela(){
        frame = new JFrame("Checkers!");
        frame.add(this);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    // Metodo principal.
    public Xadrez() {
        // Para que os eventos do teclado e mouse funcionem.
        addKeyListener(this);
        addMouseListener(this);

        // Inicia a janela e define as dimensoes dela.
        setPreferredSize(new Dimension(width*scale, height*scale));
        iniciaJanela();

        //inicializando objetos.
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        entities = new ArrayList<Entity>();
        spritesheet = new Spritesheet("/spritesheet.png");

        jogador = new Jogador(384, 0, 64, 64, spritesheet.getSprite(384, 0, 64, 64));
        entities.add(jogador);

        try {
            tabuleiro = ImageIO.read(getClass().getResource("/ChessBoard.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Inicia a thread que comeca o jogo.
    public synchronized void start(){
        thread = new Thread(this);
        isRunning = true;
        thread.start();
    }

    // Finaliza a thread para fechar o jogo.
    public synchronized void stop(){
        isRunning = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Main, instancia e inicia o jogo.
    public static void main(String[] args) throws IOException {
        Xadrez xadrez = new Xadrez();
//        Jogo jogo = new Jogo();
//        jogo.game();
        xadrez.start();
    }
    //*********************************************************************************
    // O que acontece durante a atualizacao de tela ou onde fica a "logica do jogo".
    public void tick(){
        for(int i = 0; i < entities.size(); i++){
            Entity e = entities.get(i);
            if(e instanceof Jogador) {
                // tick do jogador
            }
            e.tick();
        }
    }
    //*********************************************************************************
    // Metodo que controla a parte grafica do jogo.
    public void render(){
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null){
            this.createBufferStrategy(3);
            return;
        }
        Graphics g = image.getGraphics();

        //Preenche a janela com uma cor (preto)
        g.setColor(new Color(0, 0, 0));
        g.fillRect(0,0, width, height);

        //mostra o tabuleiro e as entidades na array entities
        g.drawImage(tabuleiro, 0,0, null);
        for(int i = 0; i < entities.size(); i++){
            Entity e = entities.get(i);
            e.render(g);
        }

        // Escrever coisas na tela
        /*
        g.setFont(new Font("Arial", Font.BOLD, 15));
        g.setColor(Color.DARK_GRAY);
        g.drawString("GAME ON!!", 10, 30);
        */

        g.dispose();
        g = bs.getDrawGraphics();
        g.drawImage(image, 0,0, width*scale, height*scale, null);
        bs.show();
    }
    //*********************************************************************************

    @Override // Metodo que controla a taxa de atualizacao de quadros por segundo do jogo (FPS)
    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000/amountOfTicks;
        double delta = 0;
        int frames = 0;
        double timer = System.currentTimeMillis();

        while (isRunning){
            long now = System.nanoTime();
            delta += (now - lastTime)/ns;
            lastTime = now;
            if (delta>=1){
                tick();
                render();
                frames++;
                delta--;
            }
            if (System.currentTimeMillis() - timer >= 1000){
                //System.out.println("FPS: " + frames);
                frames = 0;
                timer += 1000;
            }
        }
        stop();
    }

    // Os  ultimos metodos sao parte do KeyListener e MouseListener, e controlam o recebimentos de comandos via teclado e mouse.

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D){
            //mover player para direita
            jogador.posX = 64;
        }else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A){
            //mover player para esquerda
            jogador.posX = -64;
        }

        if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W){
            //mover player para cima
            jogador.posY = -64;
        }else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S){
            //mover player para baixo
            jogador.posY = +64;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D){
            //mover player para direita
            jogador.posX = 0;
        }else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A){
            //mover player para esquerda
            jogador.posX = 0;
        }

        if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W){
            //mover player para cima
            jogador.posY = 0;
        }else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S){
            //mover player para baixo
            jogador.posY = 0;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}