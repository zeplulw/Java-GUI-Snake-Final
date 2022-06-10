package snakegame;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
    static final int DELAY = 75;

    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyParts = 6;
    int applesEaten;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    boolean easy = false;
    boolean btnPressed = false;
    Timer timer;
    Random random;
    JButton easyMode = new JButton("Easy Mode");
    
    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        easyMode.setPreferredSize(new Dimension(125, 40));
        easyMode.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                easy = true;
                btnPressed = true;
                startGame();
            }
        });
        requestFocus();
    }

    public void startGame() {
        newApple();
        this.remove(easyMode);
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        
        if (!btnPressed) {
            this.add(easyMode);
            return;
        }

        if (running)
        {
            for (int i = 0; i < SCREEN_HEIGHT/UNIT_SIZE; i++)
            {
                g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
            }

            // Create new apple
            g.setColor(Color.RED);
            g.fillRect(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.GREEN);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
                else {
                    if (i % 2 == 0) {
                        g.setColor(new Color(45, 180, 0));
                        g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                    }
                    else {
                        g.setColor(new Color(45, 150, 0));
                        g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                    }
                }
            }

            // Draw score on screen in top left corner
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Score: " + applesEaten, 10, 20);
        }
        else {
            gameOver(g);
        }
    }

    public void newApple() {
        appleX = random.nextInt(SCREEN_WIDTH/UNIT_SIZE)*UNIT_SIZE;
        appleY = random.nextInt(SCREEN_HEIGHT/UNIT_SIZE)*UNIT_SIZE;
    }

    public void move() {
        
        for (int i = bodyParts; i > 0; i--)
        {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch (direction)
        {
            case 'U':
                y[0] -= UNIT_SIZE;
                break;
            case 'D':
                y[0] += UNIT_SIZE;
                break;
            case 'L':
                x[0] -= UNIT_SIZE;
                break;
            case 'R':
                x[0] += UNIT_SIZE;
                break;
        }

    }

    public void checkApple() {
        // check if head collides with apple and grow snake
        if (x[0] == appleX && y[0] == appleY) {
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }

    public void checkCollisions() {
        
        for (int i = bodyParts; i > 0; i--)
        {
            if (x[0] == x[i] && y[0] == y[i])
            {
                running = false;
            }
        }

        if (easy){
            // check if head hits border and teleport head to opposite side
            if (x[0] < 0) {
                x[0] = SCREEN_WIDTH - UNIT_SIZE;
            }
            if (x[0] > SCREEN_WIDTH - UNIT_SIZE) {
                x[0] = 0;
            }
            if (y[0] < 0) {
                y[0] = SCREEN_HEIGHT - UNIT_SIZE;
            }
            if (y[0] > SCREEN_HEIGHT - UNIT_SIZE) {
                y[0] = 0;
            }
        }
        else {
            if (x[0] < 0 || x[0] > SCREEN_WIDTH - UNIT_SIZE || y[0] < 0 || y[0] > SCREEN_HEIGHT - UNIT_SIZE) {
                running = false;
            }
        }
    }

    public void gameOver(Graphics g) {
        // Draw a game over screen centered horizontally and vertically
        if (btnPressed){
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString("Game Over", SCREEN_WIDTH/2 - 100, SCREEN_HEIGHT/2);
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            // check for pressed key and change direction
            switch (e.getKeyCode())
            {
                case KeyEvent.VK_UP:
                    if (direction != 'D')
                    {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U')
                    {
                        direction = 'D';
                    }
                    break;
                case KeyEvent.VK_LEFT:
                    if (direction != 'R')
                    {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L')
                    {
                        direction = 'R';
                    }
                    break;
            }
        }
    }
}