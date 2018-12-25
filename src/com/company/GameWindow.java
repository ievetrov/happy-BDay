package com.company;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class GameWindow extends JFrame {

    private static GameWindow game_window;
    private static long last_frame_time;
    private static Image background;
    private static Image drop;
    private static Image game_over;
    private static float drop_left = 200;
    private static float drop_top = -100;
    private static float drop_v = 100;
    private static int score = 0;


    public static void main(String[] args) throws IOException, InterruptedException {

        Random rand = new Random();
        ArrayList<BufferedImage> clouds = new ArrayList();
        for (int i = 0; i <= 26; i++) {
            clouds.add(ImageIO.read(GameWindow.class.getResourceAsStream("gift" + i + ".png")));
        }

        background = ImageIO.read(GameWindow.class.getResourceAsStream("background.jpg"));
        game_over = ImageIO.read(GameWindow.class.getResourceAsStream("end.png"));
        drop = ImageIO.read(GameWindow.class.getResourceAsStream("gift0.png"));

        game_window = new GameWindow();
        game_window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        game_window.setLocation(200, 100);
        game_window.setSize(1288, 724);
        game_window.setResizable(false);
        last_frame_time = System.nanoTime();

        GameField game_field = new GameField();
        game_field.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                float drop_right = drop_left + drop.getWidth(null);
                float drop_bottom = drop_top + drop.getHeight(null);
                boolean is_drop = x >= drop_left && x <= drop_right && y >= drop_top && y <= drop_bottom;
                if (is_drop) {

                    drop_top = -100;
                    drop_left = (int) (Math.random() * (game_field.getWidth() - drop.getWidth(null)));
                    drop_v = drop_v + 10;
                    score++;
                    game_window.setTitle("Ты собрала поздравлений: " + score);
                    drop = clouds.get(rand.nextInt(26));
                }

                if(drop_top > game_window.getHeight()) {
                    drop_top = -100;
                    drop_v = 100;
                }
            }
        });

        game_window.add(game_field);
        game_window.setVisible(true);

    }

    private static void onRepaint(Graphics g) throws InterruptedException {

        long current_time = System.nanoTime();
        float delta_time = (current_time - last_frame_time) * 0.000000001f;
        last_frame_time = current_time;

        drop_top = drop_top + drop_v * delta_time;

        g.drawImage(background, 0, 0, null);
        g.drawImage(drop, (int) drop_left, (int) drop_top, null);

        if (drop_top > game_window.getHeight()) {
            g.drawImage(game_over, 0, 0, null);
        }

    }

    private static class GameField extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            try {
                onRepaint(g);
            } catch (InterruptedException e) {
            }
            repaint();
        }

    }

}
