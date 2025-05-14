package PaooGame.Graphics;

import PaooGame.Camera;
import PaooGame.Player;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class HealthBar {

    BufferedImage heart;
    private Player target;

    public HealthBar(Player player){
        try {
            heart = ImageIO.read(new File("res/textures/heart.png"));
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
        for(int i=1; i<=target.getHealth()/100; i++){
            if(heart != null){
                g.drawImage(heart, i*40 , 40, null);
            }
        }

    }
}
