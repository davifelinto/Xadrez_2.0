package com.projetoPOO.main;

import com.projetoPOO.entities.Entity;
import com.projetoPOO.entities.Player;
import com.projetoPOO.graficos.Spritesheet;

import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Xadrez extends Canvas implements KeyListener, Runnable {

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
    private Player player;

    public static Spritesheet spritesheet;
    public List<Entity> entities;

    //Metodos
    public void iniciaJanela(){ //Criacao e configuracao da janela do jogo.
        frame = new JFrame("Checkers!");
        frame.add(this);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public Xadrez() { // Metodo principal.
        addKeyListener(this); // para que os eventos do teclado funcionem
        setPreferredSize(new Dimension(width*scale, height*scale));
        iniciaJanela();
        //inicializando objetos
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        spritesheet = new Spritesheet("/ChessBoard.png");
        tabuleiro = spritesheet.getSprite(0,0,512,512);
        entities = new ArrayList<Entity>();

        spritesheet = new Spritesheet("/ChessSelected.png");
        player = new Player(0, 0, 64, 64, spritesheet.getSprite(0, 0, 64, 64));

        entities.add(player);
        spritesheet = new Spritesheet("/ChessPieceSpriteSheetV2.png");
        for (int i=0; i<128; i+=64){
                entities.add(new Entity(i,0,64,64, spritesheet.getSprite(i, 0,64,64)));
        }
    }

    public synchronized void start(){ // Inicia a thread que comeca o jogo.
        thread = new Thread(this);
        isRunning = true;
        thread.start();
    }

    public synchronized void stop(){ // Finaliza a thread para fechar o jogo.
        isRunning = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) { // Main, instancia e inicia o jogo.
        Xadrez game = new Xadrez();
        game.start();
    }

    public void tick(){ // O que acontece durante a atualizacao de tela ou onde fica a "logica do jogo".
        for(int i = 0; i < entities.size(); i++){
            Entity e = entities.get(i);
            if(e instanceof Player) {

            }
            e.tick();
        }
    }

    public void render(){ // Metodo que controla a parte grafica do jogo.
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null){
            this.createBufferStrategy(3);
            return;
        }
        Graphics g = image.getGraphics();

        //Desenha um retângulo
        g.setColor(new Color(18, 73, 27));
        g.fillRect(0,0, width, height);

        //mostra o tabuleiro e as entidades na array entities
        g.drawImage(tabuleiro, 0,0, null);
        for(int i = 0; i < entities.size(); i++){
            Entity e = entities.get(i);
            e.render(g);
        }

        // Escrever coisas na tela
        /*
        g.dispose();
        g.setFont(new Font("Arial", Font.BOLD, 15));
        g.setColor(Color.DARK_GRAY);
        g.drawString("GAME ON!!", 10, 30);
        */

        g = bs.getDrawGraphics();
        g.drawImage(image, 0,0, width*scale, height*scale, null);
        bs.show();
    }

    @Override
    public void run() { // Metodo que controla a taxa de atualizacao de quadros por segundo do jogo (FPS)
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
                System.out.println("FPS: " + frames);
                frames = 0;
                timer += 1000;
            }
        }
        stop();
    }

    // Os tres ultimos metodos sao parte do KeyListenner, e controlam o recebimentos de comandos via teclado.

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D){
            //mover player para direita
            player.posX = 64;
        }else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A){
            //mover player para esquerda
            player.posX = -64;
        }

        if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W){
            //mover player para cima
            player.posY = -64;
        }else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S){
            //mover player para baixo
            player.posY = +64;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D){
            //mover player para direita
            player.posX = 0;
        }else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A){
            //mover player para esquerda
            player.posX = 0;
        }

        if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W){
            //mover player para cima
            player.posY = 0;
        }else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S){
            //mover player para baixo
            player.posY = 0;
        }
    }
}