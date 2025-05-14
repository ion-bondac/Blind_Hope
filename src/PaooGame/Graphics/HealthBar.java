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
    private Player target;

    public HealthBar(Player player){
        try {
            heart = ImageIO.read(new File("res/menu/heart.png"));
            healthbar = ImageIO.read(new File("res/menu/healthbar.png"));
            sword = ImageIO.read(new File("res/menu/sword.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        target = player;
    }

//    public void Update(){
//        for(int i=1; i<=target.getHealth()/100; i++){
//
//        }
//    }

    public void render(Graphics g, Camera camera){
        g.drawImage(healthbar, 20, 20, null);
        g.drawImage(sword, 40, 45, null);
        for(int i=1; i<=target.getHealth()/100; i++){
            if(heart != null){
                g.drawImage(heart, i*40 + 40 , 45, null);
            }
        }

    }
}
