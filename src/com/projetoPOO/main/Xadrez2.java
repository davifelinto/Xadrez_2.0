package com.projetoPOO.main;

import com.projetoPOO.graficos.Spritesheet;
import com.projetoPOO.logic.Casa;
import com.projetoPOO.logic.Peca;
import com.projetoPOO.logic.Tabuleiro;
import com.projetoPOO.logic.Pecas.*;
import com.projetoPOO.logic.Movimento;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseListener;
import java.io.IOException;

public class Xadrez2 extends Canvas implements Runnable {
    //Variáveis globais
    public static JFrame frame;
    public static JPanel mainPanel;
    public static JPanel boardAndPieces;
    public static JTextArea textoMovimentos;
    public static JCheckBoxMenuItem girarAutomaticamente;
    public static JLabel nomeJogadores;
    private static Thread thread;
    private boolean isRunning = false;
    private static final int width = 800;
    private static final int height = 600;
    protected static boolean gameStarted = false;
    private final int scale = 1;
    private Casa mouseAtual = null;
    private boolean iniciaMovimento = false;
    private int quadrado = 62;
    private Peca pecaSelecionada = null;
    private List<Casa> casasValidas = null;
    private List<Movimento> moveValido = null;
    private boolean isCheque = false;
    private List<Peca> setdePecas = Tabuleiro.getSetPecas(ControlaJogo.isTurno_Branco());
    private Peca reiAtual = procuraRei(setdePecas);
    private boolean girar = false;
    public static MyInterface temaPecas;
    private boolean mouseDown = false;
    private boolean isCalculando = false;
    private char escolha = 'P';
    @FunctionalInterface
    public interface MyInterface{
        Image[] someMethod(int tam);
    }
    //Inicia o programa
    public static void main(String[] args) throws IOException {
        temaPecas = tam -> {
            try {
                return Spritesheet.getSpriteSheetPieces2(tam);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        };
        Xadrez2 xadrez = new Xadrez2();
        xadrez.start();
    }
    public Xadrez2() {
        iniciaJanelaPrincipal();
    }
    // Inicia a thread que comeca o jogo.
    public synchronized void start(){
        thread = new Thread(this);
        isRunning = true;
        thread.start();
    }
    //Criacao e configuracao das janelas do jogo.
    public void iniciaJanelaJogador(){
        JDialog jdialog = new JDialog(frame, true);
        jdialog.setSize(300, 200);
        jdialog.setLayout(new FlowLayout());
        jdialog.setBounds(width/2, height/2 , 300, 200);
        jdialog.add(new JLabel("Nome do Jogador de Peças Brancas"));
        JTextField jogador1 = new JTextField("");
        jdialog.add(jogador1);
        jogador1.setPreferredSize(new Dimension(150,20));              
        jdialog.add(new JLabel("Nome do Jogador de Peças Negras"));
        JTextField jogador2 = new JTextField("");
        jdialog.add(jogador2);
        jogador2.setPreferredSize(new Dimension(150,20));
        JLabel warning = new JLabel("Não deixe o nome de nenhum jogador em branco");
        jdialog.add(warning);
        JButton confirmar = new JButton("Confirmar");
        confirmar.addActionListener(new ActionListener(){ 
                @Override
                public void actionPerformed(ActionEvent e){
                    String nome1 = jogador1.getText().replaceAll("\\s","");
                    String nome2 = jogador2.getText().replaceAll("\\s","");
                    if((nome1.equals("")) || (nome2.equals(""))){
                        warning.setForeground(Color.RED);
                        jdialog.validate();
                    }else{
                        Tabuleiro.setSetPecas(new ArrayList<>(), true);
                        Tabuleiro.setSetPecas(new ArrayList<>(), false); 
                        ControlaJogo.jogador[0] = new Jogador(true, jogador1.getText());
                        ControlaJogo.jogador[1] = new Jogador(false, jogador2.getText());
                        Tabuleiro.carregaTabuleiro("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
                        pecaSelecionada = null;
                        setdePecas = Tabuleiro.getSetPecas(ControlaJogo.isTurno_Branco());
                        reiAtual = procuraRei(setdePecas);
                        isCheque = false;
                        if(!gameStarted){
                            imprimeTabuleiro();
                            imprimeListaMovimento();
                            iniciaMouseListener();
                            frame.validate();
                            gameStarted = true;
                        }else{
                            nomeJogadores.setText(ControlaJogo.jogador[0].getNome() + "\t vs \t" + ControlaJogo.jogador[1].getNome());
                            textoMovimentos.setText("");
                            ControlaJogo.setPgn(new ArrayList<>());
                        }
                        jdialog.dispose();
                    }
                }
            }
        );
        jdialog.add(confirmar);
        jdialog.setResizable(false);
        jdialog.setVisible(true);
    }
    public void iniciaJanelaPrincipal(){
        frame = new JFrame("Checkers!");
        JMenuBar barraMenu = new JMenuBar();
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
        iniciaMenuBar(barraMenu);
        iniciaConfigBar(barraMenu);
        frame.add(mainPanel);
        frame.setJMenuBar(barraMenu);
        frame.setSize(width*scale, height*scale);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    private void iniciaMenuBar(JMenuBar menu) {
        JMenu fileMenu = new JMenu("Arquivo");
        //Botao de Iniciar
        JMenuItem startFEN = new JMenuItem("Novo Jogo");
        ActionListener acaoStart = new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                iniciaJanelaJogador();
            }
        };
        startFEN.addActionListener(acaoStart); 
        //Botao de Carregar
        JMenuItem openFEN = new JMenuItem("Carregar Jogo");
        ActionListener acaoOpen = new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                //Zera o set antigo de peças para não duplicar as peças
                Tabuleiro.setSetPecas(new ArrayList<>(), true);
                Tabuleiro.setSetPecas(new ArrayList<>(), false); 
                try {
                    Tabuleiro.carregaTabuleiro(Tabuleiro.leTabuleiro());
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
                setdePecas = Tabuleiro.getSetPecas(ControlaJogo.isTurno_Branco());
                reiAtual = procuraRei(setdePecas);
                isCheque = reiAtual.verificaAtaque();
                //Carregar também o nome dos jogadores e o pgn (lista de movimentos)
                System.out.println("Carregar");
            }
        };
        openFEN.addActionListener(acaoOpen); 
        //Botao de Salvar
        JMenuItem saveFEN = new JMenuItem("Salvar Jogo");
        ActionListener acaoSave = new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                try {
                    Tabuleiro.gravaTabuleiro(Tabuleiro.tabuleiroParaString(ControlaJogo.isTurno_Branco()),
                            ControlaJogo.jogador[0].getNome(), ControlaJogo.jogador[1].getNome(), textoMovimentos.getText());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                //Salvar também o nome dos jogadores e o pgn (lista de movimentos)
                System.out.println("implementado Salvar");
            }   
        };
        saveFEN.addActionListener(acaoSave); 
        //Botao de Sair
        JMenuItem exit = new JMenuItem("Sair");
        ActionListener acaoSair = new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                System.exit(0);;
            }
        };
        exit.addActionListener(acaoSair); 
        fileMenu.add(startFEN);
        fileMenu.add(openFEN);
        fileMenu.add(saveFEN);
        fileMenu.add(exit);
        menu.add(fileMenu);
    }
    private void iniciaConfigBar(JMenuBar menu){
        JMenu fileMenu = new JMenu("Configurações de Jogo");
        //Botao de Girar
        JMenuItem girarT = new JMenuItem("Girar Tabuleiro");
        ActionListener acaoGirar = new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                girar = !girar;
            }
        };
        //Checkbox de girar automaticamente
        girarAutomaticamente = new JCheckBoxMenuItem("Girar Tabuleiro Automaticamente");
        girarT.addActionListener(acaoGirar);

        JMenuItem trocaTema = new JMenuItem("Trocar tema das peças");
        //Funcao para trocar temas do tabuleiro
        ActionListener acaoTrocar = new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                MyInterface tema1;
                MyInterface tema2;
                tema2 = tam -> {
                try {
                        return Spritesheet.getSpriteSheetPieces2(tam);
                    } catch (IOException exc) {
                        exc.printStackTrace();
                    }
                    return null;
                };
                tema1 = tam -> {
                    try {
                        return Spritesheet.getSpriteSheetPieces(tam);
                    } catch (IOException exc) {
                        exc.printStackTrace();
                    }
                    return null;
                };
                if(temaPecas == tema1)
                    temaPecas = tema2;
                else
                    temaPecas = tema1;
            };
        };
        trocaTema.addActionListener(acaoTrocar);
        fileMenu.add(girarT);
        fileMenu.add(girarAutomaticamente);
        fileMenu.add(trocaTema);
        menu.add(fileMenu);
    }
    private void imprimeTabuleiro() {
        JPanel paneTabuleiro = new JPanel();
        nomeJogadores = new JLabel(ControlaJogo.jogador[0].getNome() + "\t vs \t" + ControlaJogo.jogador[1].getNome());
        paneTabuleiro.add(nomeJogadores);
        paneTabuleiro.setLayout(new BoxLayout(paneTabuleiro, BoxLayout.PAGE_AXIS));
        paneTabuleiro.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        boardAndPieces = new JPanel(){
            @Override
            public void paint(Graphics g){
                boolean white = true;
                int calc = 0;
                if(girarAutomaticamente.getState()){
                    if(ControlaJogo.isTurno_Branco()){
                        calc = 0;
                    }else{
                        calc = 7;
                    }
                }
                if(girar){
                    if(calc == 7) calc = 0; else calc = 7;
                }
                for(int lin = 0; lin < 8; lin++){
                    for(int col = 0; col < 8; col++){
                        int pos_col = Math.abs(calc-col)*quadrado;
                        int pos_lin = Math.abs(calc-lin)*quadrado;
                        //pinta o tabuleiro
                        if(white){
                            if(mouseAtual != null && mouseAtual.getLinha() == lin && mouseAtual.getColuna() == col)
                                g.setColor(Color.white.darker());
                            else
                                g.setColor(Color.white);
                        }
                        else{
                            if(mouseAtual != null && mouseAtual.getLinha() == lin && mouseAtual.getColuna() == col)
                                g.setColor(new Color(119, 148, 85).darker());
                            else
                                g.setColor(new Color(119, 148, 85));
                        }
                        g.fillRect(pos_col, pos_lin, quadrado, quadrado);
                        white = !white;
                        //Mostra os movimentos válidos
                        if(casasValidas != null && casasValidas.contains(Tabuleiro.getCasa(lin, col))){
                            try {
                                Image img[] = Spritesheet.getSelected(quadrado);
                                if(Tabuleiro.getCasa(lin, col).getPeca() != ' ')
                                    g.drawImage(img[0],pos_col, pos_lin, this);
                                else
                                    g.drawImage(img[1],pos_col, pos_lin, this);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        //Coloca as peças
                        if(Tabuleiro.getCasa(lin, col).getPeca() != ' '){
                            try {
                                Casa busca = Tabuleiro.getCasa(lin, col);
                                if(busca.getObj_peca() != pecaSelecionada){
                                    g.drawImage(Tabuleiro.retornaPeca(busca.getPeca(), quadrado), 
                                    pos_col, pos_lin, this);
                                }
                                else{
                                    g.drawImage(Tabuleiro.retornaPeca(busca.getPeca(), quadrado)
                                    ,pos_col, pos_lin, Color.gray, this);
                                }
                                if(isCheque && busca.getObj_peca() == reiAtual){
                                    g.drawImage(Tabuleiro.retornaPeca(busca.getPeca(), quadrado)
                                    ,pos_col, pos_lin, Color.red, this);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    white = !white;
                }
                //Desenha peça arrastada
                if(mouseDown && pecaSelecionada != null){
                    int x = (int) boardAndPieces.getMousePosition().getX()-32;
                    int y = (int) boardAndPieces.getMousePosition().getY()-32;    
                    try {
                        g.drawImage(Tabuleiro.retornaPeca(pecaSelecionada.getPosicao().getPeca(), quadrado), 
                        x, y, null);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } 
        };
        boardAndPieces.setMinimumSize(new Dimension(quadrado*8, quadrado*8));
        boardAndPieces.setPreferredSize(new Dimension(quadrado*8, quadrado*8));
        paneTabuleiro.add(boardAndPieces);
        mainPanel.add(paneTabuleiro);
    }
    private void imprimeListaMovimento(){
        JScrollPane listScroller = new JScrollPane();
        listScroller.setPreferredSize(new Dimension(250, 80));
        listScroller.setAlignmentX(LEFT_ALIGNMENT);
        JPanel listPane = new JPanel();
        listPane.setLayout(new BoxLayout(listPane, BoxLayout.PAGE_AXIS));
        JLabel label = new JLabel("Lista de Movimentos");
        listPane.add(label);
        listPane.add(Box.createRigidArea(new Dimension(0,5)));
        listPane.add(listScroller);
        listPane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        //Dentro da lista de Scroll
        textoMovimentos = new JTextArea();
        boolean white = true;
        for(Movimento movimentos : ControlaJogo.getPgn()){
            if(white){
                textoMovimentos.append(movimentos.getNotacao(movimentos) + "\t");
            }else{
                textoMovimentos.append(movimentos.getNotacao(movimentos) + "\n");
            }
            white = !white;
        }
        textoMovimentos.setEditable(false);
        listScroller.setViewportView(textoMovimentos);
        mainPanel.add(listPane);
    }
    private Casa getCasa(int x, int y){
        int calc = 0;
        if(girarAutomaticamente.getState()){
            if(ControlaJogo.isTurno_Branco()){
                calc = 0;
            }else{
                calc = 7;
            }
        }
        if(girar){
            if(calc == 7) calc = 0; else calc = 7;
        }
        int col = Math.abs(calc-(x/quadrado));
        int lin = Math.abs(calc-(y/quadrado));
        return Tabuleiro.getCasa(lin, col);
    }
    private void iniciaMouseListener(){
        boardAndPieces.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {                
                try {
                    mouseAtual = getCasa(e.getX(), e.getY());  
                } catch (Exception exc) {
                    mouseAtual = null;
                }
            }
            @Override
            public void mouseMoved(MouseEvent e) {
                try {
                    mouseAtual = getCasa(e.getX(), e.getY());  
                } catch (Exception exc) {
                    mouseAtual = null;
                }
            }
        });
        boardAndPieces.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(pecaSelecionada != null && pecaSelecionada == mouseAtual.getObj_peca()){
                    //desseleciona se clicar de novo na peça
                    pecaSelecionada = null;
                    moveValido = null;
                    casasValidas = null;
                }else if(casasValidas != null && mouseAtual != null && casasValidas.contains(mouseAtual)){
                    //inicia um Movimento se a casa for válida
                    iniciaMovimento = true;
                }
                else{
                    try{//tenta selecionar
                        pecaSelecionada = mouseAtual.getObj_peca();
                        if(pecaSelecionada == null){
                            pecaSelecionada = null;
                            moveValido = null;
                            casasValidas = null;
                        }
                    }catch(Exception exc){
                        pecaSelecionada = null;
                        moveValido = null;
                        casasValidas = null;
                    }
                }
            }
            @Override
            public void mousePressed(MouseEvent e) {
                if(pecaSelecionada != null && pecaSelecionada == mouseAtual.getObj_peca()){}
                else if(casasValidas != null && mouseAtual != null && casasValidas.contains(mouseAtual)){}
                else{
                    try{//tenta selecionar
                        pecaSelecionada = mouseAtual.getObj_peca();
                        if(pecaSelecionada == null){
                            pecaSelecionada = null;
                            moveValido = null;
                            casasValidas = null;
                        }
                    }catch(Exception exc){
                        pecaSelecionada = null;
                        moveValido = null;
                        casasValidas = null;
                    }
                }
                mouseDown = true;
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                if(pecaSelecionada != null && pecaSelecionada == mouseAtual.getObj_peca()){
                    //desseleciona se clicar de novo na peça
                    pecaSelecionada = null;
                    moveValido = null;
                    casasValidas = null;
                }else if(casasValidas != null && mouseAtual != null && casasValidas.contains(mouseAtual)){
                    //inicia um Movimento se a casa for válida
                    iniciaMovimento = true;
                }
                mouseDown = false;
            }
            @Override
            public void mouseEntered(MouseEvent e){}
            @Override
            public void mouseExited(MouseEvent e){}
        });
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
    // Metodo que controla a taxa de atualizacao de quadros por segundo do jogo (FPS)
    @Override 
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
                logic();
                if(!isCalculando)
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
    // Metodo que controla a parte grafica do jogo.
    public void render(){
        frame.repaint();//problema - quando o computador testa se a jogada é valida a peça aparece na casa testada;
    }
    // O que acontece durante a atualizacao de tela ou onde fica a "logica do jogo".
    public void logic(){
        if(pecaSelecionada != null && setdePecas.contains(pecaSelecionada)){
            isCalculando = true;
            moveValido = pecaSelecionada.filtraLista(reiAtual);
            casasValidas = new ArrayList<>();
            for(Movimento teste : moveValido){
                casasValidas.add(teste.getCasaDestino());    
            }
            isCalculando = false;
        }
        if(iniciaMovimento){
            String cheque = "";
            iniciaMovimento = false;
            for(Movimento teste : moveValido){
                if(teste.getCasaDestino() == mouseAtual){
                    movimenta(teste);
                    break;
                }     
            }
            pecaSelecionada = null;
            moveValido = null;
            casasValidas = null;
            //muda o rei e o set de pacas
            setdePecas = Tabuleiro.getSetPecas(ControlaJogo.isTurno_Branco());
            reiAtual = procuraRei(setdePecas);
            isCheque = reiAtual.verificaAtaque();
            if(isCheque){//Se o seu rei estiver em cheque verifica se está em mate
                for(Peca pecaVerifica : setdePecas){
                    moveValido = pecaVerifica.filtraLista(reiAtual);
                    if(!moveValido.isEmpty())
                        break;
                }
                if(moveValido.isEmpty()){
                    textoMovimentos.append("+");
                    janelaFimDeJogo(new JLabel("Vitória de: " + ControlaJogo.jogador[Boolean.valueOf(ControlaJogo.isTurno_Branco()).compareTo(false)].getNome()));
                }
                cheque += "+";
            }
            else {//Se o rei não estiver em cheque verifica se a jogada provoca empate
                //Empate por afogamento
                for(Peca pecaVerifica : setdePecas){
                    moveValido = pecaVerifica.filtraLista(reiAtual);
                    if(!moveValido.isEmpty())
                        break;
                }
                if(moveValido.isEmpty()){
                    janelaFimDeJogo(new JLabel("Empate"));
                }
                //Regra dos 50 movimentos
                if(ControlaJogo.getPgn().size() > 110){
                    int i = 0, atual = 0;
                    for(Movimento move : ControlaJogo.getPgn()){
                        if(move.getPecaCapturada() == null || //nao capturei nenhuma peca
                        move.getPecaCapturada().isCor_Branca() == move.getPecaMovimentada().isCor_Branca() ||
                        !(move.getPecaMovimentada() instanceof Peao) ){//e não movi nenhum peao
                            i++;
                        }if(i >= 100 || ControlaJogo.getPgn().size() - atual < 100){
                            break;
                        }else{
                            i = 0;
                        }
                        atual++;
                    }
                    if(i >= 100){
                        janelaFimDeJogo(new JLabel("Empate"));
                    }
    
                }
                //Empate por insuficiencia material
                if(ControlaJogo.jogador[0].getSuasPecas().size() < 3 && ControlaJogo.jogador[1].getSuasPecas().size() < 3){
                    int contaQualidade[] = {0, 0};
                    outerloop:
                    for(int i = 0; i<2; i++){
                        for(Peca peca : ControlaJogo.jogador[i].getSuasPecas()){
                            if(peca instanceof Dama || peca instanceof Torre || peca instanceof Peao){
                                contaQualidade[i] += 7;
                                break outerloop;
                            }
                            if(peca instanceof Bispo)
                                contaQualidade[i] += 3;
                            else if(peca instanceof Cavalo )
                                contaQualidade[i] += 2;
                        }
                    }
                    if(contaQualidade[0] < 5 && contaQualidade[1] < 5){
                        janelaFimDeJogo(new JLabel("Empate"));
                    }    
                }
            }
            if(escolha != 0){
                if(!ControlaJogo.isTurno_Branco()){
                    textoMovimentos.append(cheque + "\t");
                }else{
                    textoMovimentos.append(cheque + "\n");
                }
            }
        }
    }
    public Peca procuraRei(List<Peca> setdePecas){
        Peca rei = null;
        for(Peca procuraRei : setdePecas){
            if(procuraRei instanceof Rei){
                rei = procuraRei;
                break;
            }
        }
        return rei;
    }
    public void janelaFimDeJogo(JLabel comp){
        JDialog jdialog = new JDialog(frame, true);
        jdialog.setUndecorated(true);
        jdialog.setSize(300, 50);
        int x = (int) MouseInfo.getPointerInfo().getLocation().getX()-155;
        int y = (int) MouseInfo.getPointerInfo().getLocation().getY()-50;
        jdialog.setLocation(x, y);
        JPanel pane = new JPanel();
        pane.setLayout(new BoxLayout(pane, BoxLayout.PAGE_AXIS ));
        JButton confirmar = new JButton("OK");
        confirmar.addActionListener(new ActionListener(){ 
                @Override
                public void actionPerformed(ActionEvent e){
                    jdialog.dispose();
                }
            }
        );
        pane.add(comp);
        pane.add(confirmar);
        comp.setAlignmentX(CENTER_ALIGNMENT);
        confirmar.setAlignmentX(CENTER_ALIGNMENT);
        jdialog.add(pane);
        jdialog.setVisible(true);
    }
    public void movimenta(Movimento move){
        boolean promocao = false;
        Peca peca = pecaSelecionada;
        escolha = 'P';
        if(peca instanceof Peao && move.getCasaDestino().getLinha() == ((Peao) peca).getLinhaPromocao()){
            janelaEscolha();
            if(escolha == 0)
                return;
            promocao = true;
        }
        //Adiciona movimento na lista
        textoMovimentos.append(move.getNotacao(move));
        //Move
        peca.setPosicao(move.getCasaDestino());
        if(promocao){
            int lin = move.getCasaDestino().getLinha();
            int col = move.getCasaDestino().getColuna();
            if(!pecaSelecionada.isCor_Branca())
                escolha = Character.toLowerCase(escolha);
            switch(escolha){
                case 'Q':
                    peca = new Dama(pecaSelecionada.isCor_Branca(), lin, col);
                    break;
                case 'B':
                    peca = new Bispo(pecaSelecionada.isCor_Branca(), lin, col);
                    break;
                case 'N':
                    peca = new Cavalo(pecaSelecionada.isCor_Branca(), lin, col);
                    break;
                case 'R':
                    peca = new Torre(pecaSelecionada.isCor_Branca(), lin, col);
                    break;
            }
            Tabuleiro.getCasa(lin, col).setPeca(escolha);
            move.getCasaInicial().setPeca(escolha);
            setdePecas.remove(pecaSelecionada);
            setdePecas.add(peca);
            textoMovimentos.append("=" + Tabuleiro.retornaPeca(peca.getPosicao().getPeca()));
        }else
        if(peca instanceof Torre){
            if(peca.isCor_Branca()){
                if(move.getCasaInicial().getColuna() == 7)
                    ControlaJogo.setRoque_Rei_b(false);
                else if(move.getCasaInicial().getColuna() == 0)
                    ControlaJogo.setRoque_Dama_b(false);
            }else{
                if(move.getCasaInicial().getColuna() == 7)
                    ControlaJogo.setRoque_Rei_p(false);
                else if(move.getCasaInicial().getColuna() == 0)
                    ControlaJogo.setRoque_Dama_p(false);
            }
        }else
        if(peca instanceof Rei){
            if(peca.isCor_Branca()){
                ControlaJogo.setRoque_Rei_b(false);
                ControlaJogo.setRoque_Dama_b(false);
            }else{
                ControlaJogo.setRoque_Rei_p(false);
                ControlaJogo.setRoque_Dama_p(false);
            }
            //se o movimento feito for roque
            if(move.getPecaCapturada()!= null){
                if(move.getPecaCapturada().isCor_Branca() == move.getPecaMovimentada().isCor_Branca()){
                    //move a torre
                    if(move.getCasaDestino().getColuna() == 6){
                        Tabuleiro.getCasa(peca.getPosicao().getLinha(), 5).setPeca(move.getPecaCapturada().getPosicao().getPeca());
                        move.getPecaCapturada().getPosicao().setPeca(' ');
                        move.getPecaCapturada().getPosicao().setObj_peca(null);
                        move.getPecaCapturada().setPosicao(Tabuleiro.getCasa(peca.getPosicao().getLinha(), 5));
                        Tabuleiro.getCasa(peca.getPosicao().getLinha(), 5).setObj_peca(move.getPecaCapturada());
                    }else{
                        Tabuleiro.getCasa(peca.getPosicao().getLinha(), 3).setPeca(move.getPecaCapturada().getPosicao().getPeca());
                        move.getPecaCapturada().getPosicao().setPeca(' ');
                        move.getPecaCapturada().getPosicao().setObj_peca(null);
                        move.getPecaCapturada().setPosicao(Tabuleiro.getCasa(peca.getPosicao().getLinha(), 3));
                        Tabuleiro.getCasa(peca.getPosicao().getLinha(), 3).setObj_peca(move.getPecaCapturada());
                    }
                }
            }
        }
        if(move.getPecaCapturada()!= null){
            if(move.getPecaCapturada() instanceof Peao){
                move.getPecaCapturada().getPosicao().setPeca(' ');
                move.getPecaCapturada().getPosicao().setObj_peca(null);
            }
            Tabuleiro.getSetPecas(!(ControlaJogo.isTurno_Branco())).remove(move.getPecaCapturada());//remove a peca da lista adversaria
        }
        move.getCasaDestino().setObj_peca(peca);
        move.getCasaInicial().setObj_peca(null);
        move.getCasaDestino().setPeca(move.getCasaInicial().getPeca());
        move.getCasaInicial().setPeca(' ');
        ControlaJogo.adicionarMovimento(move);
        ControlaJogo.setTurno_Branco(!ControlaJogo.isTurno_Branco());
    }
    public void janelaEscolha(){
        JDialog jdialog = new JDialog(frame, true);
        jdialog.setSize(300, 100);
        jdialog.setLayout(new FlowLayout());
        jdialog.setBounds(new Rectangle(300, 110));
        int x = (int) MouseInfo.getPointerInfo().getLocation().getX()-155;
        int y = (int) MouseInfo.getPointerInfo().getLocation().getY()-50;
        jdialog.setLocation(x, y);
        Point pt = new Point(jdialog.getLocation());
        SwingUtilities.convertPointToScreen(pt, jdialog);
        ButtonGroup group = new ButtonGroup();
        JRadioButton dama = new JRadioButton("dama");
        JRadioButton torre = new JRadioButton("torre");
        JRadioButton bispo = new JRadioButton("bispo");
        JRadioButton cavalo = new JRadioButton("cavalo");
        group.add(dama);
        group.add(torre);
        group.add(bispo);
        group.add(cavalo);
        escolha = 0;
        JButton confirmar = new JButton("Confirmar");
        confirmar.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(!group.isSelected(null)){
                        if(dama.isSelected()){
                            escolha = 'Q';
                        }
                        if(torre.isSelected()){
                            escolha = 'R';
                        }
                        if(bispo.isSelected()){
                            escolha = 'B';
                        }
                        if(cavalo.isSelected()){
                            escolha = 'N';
                        }
                        jdialog.dispose();
                    }
                };
            }
        );
        jdialog.add(dama);
        jdialog.add(torre);
        jdialog.add(bispo);
        jdialog.add(cavalo);
        jdialog.add(confirmar);
        jdialog.setResizable(false);
        jdialog.setVisible(true);
    }
    public void tick(){}
}
