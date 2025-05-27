package PaooGame.Graphics;

import PaooGame.Camera;
import PaooGame.Player;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BlindOverlay {
    private Player target;
    BufferedImage overlay;

    public BlindOverlay(Player player){
        try {
            overlay = ImageIO.read(new File("res/textures/blindOverlay.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        target = player;
    }

    public void render(Graphics g, Camera camera){
        if(target.blindfolded){
            g.drawImage(overlay, target.getX() - 850 - camera.getX(), target.getY() - 450 - camera.getY(), 1700, 900, null);
        }

    }

}
