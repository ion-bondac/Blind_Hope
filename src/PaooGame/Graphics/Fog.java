package PaooGame.Graphics;

import PaooGame.Camera;
import PaooGame.GameMap;
import PaooGame.Player;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Fog {
    private Player target;
    private int x;
    private int y;
    BufferedImage fog;

    public Fog(Player player, int x , int y){
        try {
            fog = ImageIO.read(new File("res/textures/fog.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        target = player;
        this.x=32*x;
        this.y = 32*y;
    }

    public void update(GameMap gameMap){
        if(target.getX() > x-32 && target.getX() < x+160){
            if(target.getY() > y-32 && target.getY() < y+96){
                if(!target.blindfolded){
                    if(!target.dead){
                        target.Damage(300);
                    }
                }
            }
        }
    }

    public void render(Graphics g, Camera camera){
        g.drawImage(fog, x - camera.getX(), y - camera.getY(), 160, 96, null);
    }
}
