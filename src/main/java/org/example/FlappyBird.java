package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {

    int larguraBorda = 360;
    int alturaBorda = 640;

    Image birdImage;
    Image backgroundImage;
    Image bottomPipeImage;
    Image topPipeImage;

    int birdX = larguraBorda / 8;
    int birdY = alturaBorda / 2;
    int birdWidth = 24;
    int birdHeight = 24;

    class Bird {
        int x = birdX;
        int y = birdY;
        int width = birdWidth;
        int height = birdHeight;
        Image img;

        Bird (Image img){
            this.img = img;
        }

    }

    int pipeX = larguraBorda;
    int pipeY = 0;
    int pipeWidth = 64;
    int pipeHeight = 512;

    class Pipe {

        int x = pipeX;
        int y = pipeY;
        int width = pipeWidth;
        int heigth = pipeHeight;
        Image img;
        boolean passed = false;

        public Pipe(Image img) {
            this.img = img;
        }
    }

    Bird bird;
    int velocityX = -4;
    int velocityY = -8;
    int gravity = 1;

    ArrayList<Pipe> pipes;
    Random random = new Random();

    Timer gameLoop;
    Timer placePipesTimer;

    boolean gameOver = false;

    double counter = 0;

    public FlappyBird(){
        setPreferredSize(new Dimension(larguraBorda, alturaBorda));
        setFocusable(true);
        addKeyListener(this);

        backgroundImage = new ImageIcon(getClass().getResource("/assets/flappybirdbg.png")).getImage();
        birdImage = new ImageIcon(getClass().getResource("/assets/flappybird.png")).getImage();
        bottomPipeImage = new ImageIcon(getClass().getResource("/assets/bottompipe.png")).getImage();
        topPipeImage = new ImageIcon(getClass().getResource("/assets/toppipe.png")).getImage();

        bird = new Bird(birdImage);
        pipes = new ArrayList<>();

        placePipesTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placePipes();
            }
        });
        placePipesTimer.start();
        gameLoop = new Timer(1000/60, this);
        gameLoop.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void placePipes(){
        int pipeYRandom = (int) (pipeY - pipeHeight/4 - Math.random() * pipeHeight / 2);
        int openingSpace = alturaBorda / 4;
        Pipe topPipe = new Pipe(topPipeImage);
        topPipe.y = pipeYRandom;
        pipes.add(topPipe);

        Pipe bottomPipe = new Pipe(bottomPipeImage);
        bottomPipe.y = topPipe.y + pipeHeight + openingSpace;
        pipes.add(bottomPipe);
    }

    public void draw(Graphics g){

        g.drawImage(backgroundImage, 0, 0, larguraBorda, alturaBorda, null);
        g.drawImage(bird.img, bird.x, bird.y, bird.width, bird.height, null);

        for (Pipe pipe : pipes) {
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.heigth, null);
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("" + (int) counter, larguraBorda / 2, 30);

    }

    public void move(){
        velocityY += gravity;
        bird.y += velocityY;
        bird.y = Math.max(bird.y, 0);

        for (Pipe pipe : pipes) {
            pipe.x += velocityX;

            if(collision(bird, pipe)){
                gameOver = true;
            }

            if(!pipe.passed && bird.x > pipe.x + pipe.width){
                pipe.passed = true;
                counter += 0.5;
            }

        }

        if(bird.y > alturaBorda){
            gameOver = true;
        }

    }

    public boolean collision(Bird a, Pipe b) {

        return a.x < b.x + b.width &&
                a.x + a.width > b.x &&
                a.y < b.y + b.heigth &&
                a.y + a.height > b.y;

    }

    @Override
    public void actionPerformed(ActionEvent e){
        move();
        repaint();

        if(gameOver){
            placePipesTimer.stop();
            gameLoop.stop();
        }

    }

    @Override
    public void keyPressed(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_SPACE && !gameOver){
            velocityY = -8;
        }

        if(gameOver){
            gameOver = false;
            counter = 0;
            velocityY = -2;
            bird.y = birdY;
            bird.x = birdX;

            pipes.clear();

            gameLoop.start();
            placePipesTimer.start();
        }
    }

    @Override
    public void keyTyped(KeyEvent e){
    }

    @Override
    public void keyReleased(KeyEvent e){
    }

}
