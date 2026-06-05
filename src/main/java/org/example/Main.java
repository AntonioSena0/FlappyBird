package org.example;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        int larguraBorda = 360;
        int alturaBorda = 640;

        JFrame frame = new JFrame("Flappy Bird");
        frame.setSize(larguraBorda, alturaBorda);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        FlappyBird flappyBird = new FlappyBird();
        frame.setVisible(true);
        frame.add(flappyBird);
        frame.pack();

    }
}