package PaooGame;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player {
    private int x, y; // position in tiles
    private final int size = 32;
    private Color color = Color.RED;
    public boolean onGround = false;
    public boolean isMoving = false;
    public boolean facingRight = true;
    public int gravity = 0;
    private BufferedImage standing;
    private BufferedImage spriteSheet;

    BufferedImage[] walkFrames = new BufferedImage[6];
    int walkFrameIndex = 0;
    int walkFrameDelay = 5; // număr de update-uri între schimbările de frame
    int walkFrameTick = 0;

    BufferedImage[] jumpFrames = new BufferedImage[8];
    int jumpFrameIndex = 0;
    int jumpFrameDelay = 7; // număr de update-uri între schimbările de frame
    int jumpFrameTick = 0;



    public Player(int x, int y) {
        this.x = x;
        this.y = y;
        try{
//            sprite = ImageIO.read(getClass().getResource("/sprites/Esperis_static_tile.png"));
            spriteSheet = ImageIO.read(getClass().getResource("/sprites/Esperis_Spritesheet.png"));
            standing = spriteSheet.getSubimage(0,0,size,size);
            for(int i = 0; i < walkFrames.length; i++) {
                walkFrames[i] = spriteSheet.getSubimage(i * size, size, size, size);
            }
            for(int i = 0; i < jumpFrames.length; i++) {
                jumpFrames[i] = spriteSheet.getSubimage(i * size, 2*size, size, size);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void move(int dx, int dy, GameMap map) {

        int newX = x + dx;
        int newY = y + dy;
        int tileX = newX / size;
        int tileY = newY / size;

        if(dx > 0){
            tileX += 1;
        }
        if (map.isWalkable(tileX, tileY)) {
            x = newX;
            y = newY;
            if (map.isWalkable(x/size, y/size + 1)) {
                onGround = false;
            }
        }
    }

    public void updateWalkAnimation(boolean moving) {
        if (moving) {
            walkFrameTick++;
            if (walkFrameTick >= walkFrameDelay) {
                walkFrameTick = 0;
                walkFrameIndex = (walkFrameIndex + 1) % walkFrames.length;
            }
        } else {
            walkFrameIndex= 0;
            walkFrameTick = 0;
        }
    }

    public void updateJumpAnimation(boolean onGround) {
        if (!onGround) {
            jumpFrameTick++;
            if (jumpFrameTick >= jumpFrameDelay) {
                jumpFrameTick = 0;
                jumpFrameIndex = (jumpFrameIndex + 1) % jumpFrames.length;
            }
        } else {
            jumpFrameIndex= 0;
            jumpFrameTick = 0;
        }
    }

    public void draw(Graphics g, Camera camera) {
        BufferedImage frameToDraw;
        if (spriteSheet != null) {
            if(isMoving){
                frameToDraw = walkFrames[walkFrameIndex];
//                g.drawImage(walkFrames[walkFrameIndex], x- camera.getX(), y- camera.getY(), size, size, null);
            } else if (!onGround) {
                frameToDraw = jumpFrames[jumpFrameIndex];
//                g.drawImage(jumpFrames[jumpFrameIndex], x- camera.getX(), y- camera.getY(), size, size, null);
            } else{
                frameToDraw = standing;
//                g.drawImage(standing, x- camera.getX(), y- camera.getY(), size, size, null);
            }
            if (facingRight) {
                g.drawImage(frameToDraw,
                        x- camera.getX(), y- camera.getY(),
                        size, size,
                        null
                );
            } else {
                g.drawImage(frameToDraw,
                        x- camera.getX() + size, y- camera.getY(),
                        -size, size, //FLIP
                        null
                );
            }
        } else {
            g.setColor(color);
            g.fillRect(x- camera.getX(), y- camera.getY(), size, size); // fallback dacă imaginea nu e încărcată
        }
    }


    public int getX() { return x; }
    public int getY() { return y; }
    public int getSize() { return size; }
}
