package snakegame;

// Signature: 68747470733a2f2f6769746875622e636f6d2f7a65706c756c77

import javax.swing.JFrame;

public class GameFrame extends JFrame{
    
    GameFrame() {
        
        this.add(new GamePanel());
        this.setTitle("Snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        
    }
    
}
