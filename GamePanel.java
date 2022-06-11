package snakegame;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 50;
    static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
    static final int DELAY = 100;

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
            // Center everything to the middle of the screen
            easyMode.setLocation(SCREEN_WIDTH/2 - easyMode.getWidth()/2, SCREEN_HEIGHT/2 - easyMode.getHeight()/2);

            return;
        }

        if (running)
        {

            if (bodyParts >= GAME_UNITS)
            {
                gameConditionUpdate(g, true);
                running = false;
                timer.stop();
                return;
            }

            // Paint alternating pattern squares
            for (int i = 0; i < SCREEN_HEIGHT/UNIT_SIZE; i++)
            {
                for (int j = 0; j < SCREEN_WIDTH/UNIT_SIZE; j++)
                {
                    if ((i+j)%2==0) {
                        g.setColor(new Color(170,215,81));
                        g.fillRect(j*UNIT_SIZE, i*UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
                    }
                    else {
                        g.setColor(new Color(162,209,73));
                        g.fillRect(j*UNIT_SIZE, i*UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
                    }
                }
            }

            // Create new apple
            g.setColor(Color.RED);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
            // add a brown stem to the apple
            g.setColor(new Color(139,69,19));
            g.fillRect(appleX+(UNIT_SIZE/2-3), appleY-9, 6, 13);

            // Draw snake
            for (int i = bodyParts; i >= 0; i--) {
                if (i == 0) {
                    g.setColor(new Color(96, 163, 57));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                    g.setColor(Color.BLACK);
                    g.fillOval(x[i]+UNIT_SIZE/4, y[i]+UNIT_SIZE/4, UNIT_SIZE/2, UNIT_SIZE/2);
                }
                else {
                    if (i % 2 == 0) {
                        g.setColor(new Color(84, 143, 50));
                        g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                    }
                    else {
                        g.setColor(new Color(95, 163, 55));
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
            gameConditionUpdate(g, false);
        }
    }

    public void newApple() {
        appleX = random.nextInt(SCREEN_WIDTH/UNIT_SIZE)*UNIT_SIZE;
        appleY = random.nextInt(SCREEN_HEIGHT/UNIT_SIZE)*UNIT_SIZE;

        // Check if apple is on snake
        for (int i = 0; i < bodyParts; i++) {
            if (appleX == x[i] && appleY == y[i]) {
                newApple();
                return;
            }
        }
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

    public void gameConditionUpdate(Graphics g, boolean won) {
        // Draw a game over screen centered horizontally and vertically
        if (btnPressed) {
            if (won) {
                g.setColor(Color.GREEN);
                g.setFont(new Font("Arial", Font.BOLD, 40));
                g.drawString("You Win", SCREEN_WIDTH/2 - 100, SCREEN_HEIGHT/2);
            }
            else {
                g.setColor(Color.RED);
                g.setFont(new Font("Arial", Font.BOLD, 40));
                g.drawString("Game Over", SCREEN_WIDTH/2 - 100, SCREEN_HEIGHT/2);
            }
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