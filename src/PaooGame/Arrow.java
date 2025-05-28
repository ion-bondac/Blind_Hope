package PaooGame;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Arrow {
    public int x;
    public int y;
    public int startX;
    public boolean right;
    public BufferedImage arrowImg;
    public boolean active = true;

    public Arrow(int x, int y, boolean right){
        this.x = x;
        this.y = y;
        this.startX = x;
        this.right = right;
        try {
            arrowImg = ImageIO.read(new File("res/sprites/arrow.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update(Enemy boss) {
        if (right) {
            x += 4;
        } else {
            x -= 4;
        }

        if(x/32==boss.getX()/32 && (y/32 == boss.getY()/32 || y/32 == boss.getY()/32 +1 )){
            boss.Damage(10);
            active = false;
        }


        if (x < startX - 100 || x > startX + 100) {
            active = false;
        }
    }

    public void render(Graphics g, Camera camera){
        if (right) {
            g.drawImage(arrowImg,
                    x- camera.getX(), y- camera.getY(),
                    32, 32,
                    null
            );
        } else {
            g.drawImage(arrowImg,
                    x- camera.getX() + 32, y- camera.getY(),
                    -32, 32, //FLIP
                    null
            );
        }
    }
}
