package PaooGame;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;

public class Enemy extends Entity {
    private Player target;
    public boolean isChasing = false;
    public int startX;
    private boolean movingRight;
    private String type;
    private BufferedImage spriteSheet;
    private int range;

    BufferedImage[] flyFrames = new BufferedImage[4];
    int flyFrameIndex;
    int flyFrameDelay = 5; // număr de update-uri între schimbările de frame
    int flyFrameTick = 0;


    //GNOMES

    BufferedImage staticGnome;

    public boolean isHurt = false;
    public int hurtDelay= 0;
    BufferedImage[] hurtFrames = new BufferedImage[2];
    int hurtFrameIndex;
    int hurtFrameDelay = 5; // număr de update-uri între schimbările de frame
    int hurtFrameTick = 0;


    public Enemy(int x, int y, Player target, String type, boolean right) {
        Random rand = new Random();
        this.x = x + rand.nextInt(80);
        this.y = y;
        this.health = 100;
        this.width = 32;
        this.height = 32;
        this.target = target;
        this.startX = x;
        this.type = type;
        this.movingRight = right;

        if(type.equals("Eagle")){
            this.speed = 2;
            this.range = 80;
            try{
                spriteSheet = ImageIO.read(Objects.requireNonNull(getClass().getResource("/sprites/EagleSpritesheet.png")));
                for(int i = 0; i < flyFrames.length; i++) {
                    flyFrames[i] = spriteSheet.getSubimage(i * width, 0, width, height);
                }
                for(int i = 0; i < hurtFrames.length; i++) {
                    hurtFrames[i] = spriteSheet.getSubimage(i * width, height, width, height);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            this.range = 50;
            this.speed=2;
            try{
                spriteSheet = ImageIO.read(Objects.requireNonNull(getClass().getResource("/sprites/gnomespritesheet.png")));
                staticGnome = spriteSheet.getSubimage(0, 0, width, height);
                for(int i = 0; i < hurtFrames.length; i++) {
                    hurtFrames[i] = spriteSheet.getSubimage(i * width, height, width, height);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void update(GameMap gameMap) {

        if(isChasing){
            // Simple chase logic
            if (target.getX() > x) x += speed;
            else if (target.getX() < x) x -= speed;

            if (target.getY() > y) y += speed;
            else if (target.getY() < y) y -= speed;
            flyFrameIndex= 0;
            flyFrameTick = 0;
        }
        else {
                if (movingRight) {
                    x += speed;
                    if (x >= startX + range){
                        movingRight = false;
                    }
                } else {
                    x -= speed;
                    if (x <= startX - range)
                        movingRight = true;
                }
                ;
                if(isHurt){
                    hurtDelay++;
                    hurtFrameTick++;
                    if (hurtFrameTick >= hurtFrameDelay) {
                        hurtFrameTick = 0;
                        hurtFrameIndex = (hurtFrameIndex + 1) % hurtFrames.length;
                    }
                    if(hurtDelay>=10){
                        isHurt = false;
                        hurtDelay=0;
                    }
                }
                else{
                    flyFrameTick++;
                    if (flyFrameTick >= flyFrameDelay) {
                        flyFrameTick = 0;
                        flyFrameIndex = (flyFrameIndex + 1) % flyFrames.length;
                    }
                }

        }

        int offset=0;
        if(movingRight){
            offset = -32;
        }
        if((x+offset)/32 == target.getX()/32 && y/32 == target.getY()/32){
            if(target.attacking){
                isHurt = true;
                health -=50;
                speed -=1;
                if(target.facingRight){
                    x+=50;
                }
                else{
                    x-=50;
                }
                if(health <= 0){
                    target.addScore(100);
                    this.active = false;
                }
            }
            else{
                target.Damage(100);
                target.attackCooldown = 0;
                if(movingRight){
                    x-=60;
                    target.move(32, 0, gameMap);
                }
                else{
                    x+=60;
                    target.move(-32, 0, gameMap);
                }
            }
        }
    }

    @Override
    public void render(Graphics g, Camera camera) {
        BufferedImage frameToDraw = hurtFrames[1];
        if(type.equals("Eagle")){
            if(!isChasing){
                if(isHurt){
                    frameToDraw = hurtFrames[hurtFrameIndex];
                }
                else{
                    frameToDraw =flyFrames[flyFrameIndex];
                }
            }
            if (movingRight) {
                g.drawImage(frameToDraw,
                        x- camera.getX(), y- camera.getY(),
                        -width, height,
                        null
                );
            } else {
                g.drawImage(frameToDraw,
                        x- camera.getX(), y- camera.getY(),
                        width, height, //FLIP
                        null
                );
            }
        } else if (type.equals("Gnome")) {
            if(!isChasing){
                if(isHurt){
                    frameToDraw = hurtFrames[hurtFrameIndex];
                }
                else{
                    frameToDraw =staticGnome;
                }
            }
            if (movingRight) {
                g.drawImage(frameToDraw,
                        x- camera.getX(), y- camera.getY(),
                        -width, height,
                        null
                );
            } else {
                g.drawImage(frameToDraw,
                        x- camera.getX(), y- camera.getY(),
                        width, height, //FLIP
                        null
                );
            }
        } else{
            g.setColor(Color.RED);
            g.fillRect(x- camera.getX(), y- camera.getY(), width, height);
        }

    }

    public void kill(){
        this.active = false;
    }
}