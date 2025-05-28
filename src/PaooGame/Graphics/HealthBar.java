package PaooGame.Graphics;

import PaooGame.Camera;
import PaooGame.Player;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class HealthBar {

    BufferedImage healthbar;
    BufferedImage heart;
    BufferedImage sword;
    BufferedImage bow;
    BufferedImage blindfold;
    private Player target;
    private Font scoreFont;

    public HealthBar(Player player){
        try {
            heart = ImageIO.read(new File("res/menu/heart.png"));
            healthbar = ImageIO.read(new File("res/menu/healthbar.png"));
            sword = ImageIO.read(new File("res/menu/sword.png"));
            bow = ImageIO.read(new File("res/menu/bow.png"));
            blindfold = ImageIO.read(new File("res/menu/blindfold.png"));

            scoreFont = new Font("Arial", Font.BOLD, 20);
        } catch (IOException e) {
            e.printStackTrace();
        }
        target = player;
    }

    public void render(Graphics g, Camera camera){
        g.drawImage(healthbar, 20, 20, null);
        if(target.weapon.equals("bow")){
            g.drawImage(bow, 40, 45, null);
        }
        else{
            g.drawImage(sword, 40, 45, null);
        }
        g.drawImage(blindfold, 80, 45, null);
        for(int i=1; i<=target.getHealth()/100; i++){
            if(heart != null){
                g.drawImage(heart, i*40 + 100 , 45, null);
            }
        }
        // Draw score
        g.setFont(scoreFont);
        g.setColor(Color.WHITE);

        // Draw score at the top right (adjust position as needed)
        String scoreText = "Score: " + target.getScore();
        FontMetrics fm = g.getFontMetrics();
        int scoreX = camera.getScreenWidth() - fm.stringWidth(scoreText) - 20; // 20px from right edge
        g.drawString(scoreText, scoreX, 30);

        // Optional: Add a small background for better visibility
        g.setColor(new Color(0, 0, 0, 150)); // Semi-transparent black
        g.fillRect(scoreX - 5, 10, fm.stringWidth(scoreText) + 10, 25);

        // Redraw score text on top of the background
        g.setColor(Color.WHITE);
        g.drawString(scoreText, scoreX, 30);
    }
}
