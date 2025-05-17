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
//    private boolean waitingToTurn = false;
//    private int turnDelay = 3; // număr de frame-uri de pauză (poți ajusta)
//    private int turnDelayCounter = 0;

    BufferedImage[] flyFrames = new BufferedImage[4];
    int flyFrameIndex;
    int flyFrameDelay = 5; // număr de update-uri între schimbările de frame
    int flyFrameTick = 0;


    public Enemy(int x, int y, Player target, String type, boolean right) {
        Random rand = new Random();
        this.x = x + rand.nextInt(80);
        this.y = y;
        this.speed = 2;
        this.health = 50;
        this.width = 32;
        this.height = 32;
        this.target = target;
        this.startX = x;
        this.type = type;
        this.movingRight = right;

        try{
            spriteSheet = ImageIO.read(Objects.requireNonNull(getClass().getResource("/sprites/EagleSpritesheet.png")));
            for(int i = 0; i < flyFrames.length; i++) {
                flyFrames[i] = spriteSheet.getSubimage(i * width, 0, width, height);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(GameMap gameMap) {

//        if (waitingToTurn) {
//            turnDelayCounter++;
//            if (turnDelayCounter >= turnDelay) {
//                waitingToTurn = false;
//                turnDelayCounter = 0;
//                movingRight = !movingRight;
//            }
//
//        }

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
                    if (x >= startX + 80){
                        movingRight = false;
                    }
                } else {
                    x -= speed;
                    if (x <= startX - 80)
                        movingRight = true;
                }
                flyFrameTick++;
                if (flyFrameTick >= flyFrameDelay) {
                    flyFrameTick = 0;
                    flyFrameIndex = (flyFrameIndex + 1) % flyFrames.length;
                }
        }


//        if(x > target.getX()-target.getSize()  && x < target.getX() + target.getSize()){
//            if(y > target.getY() + target.getSize() && y < target.getY() - target.getSize()){
//                target.Damage(20);
//            }
//        }
        int offset=0;
        if(movingRight){
            offset = -32;
        }
        if((x+offset)/32 == target.getX()/32 && y/32 == target.getY()/32){
            if(target.attacking){
                this.active = false;
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
        BufferedImage frameToDraw = flyFrames[0];
        if(type.equals("Eagle")){
            if(!isChasing){
                frameToDraw = flyFrames[flyFrameIndex];
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
        }
        else{
            g.setColor(Color.RED);
            g.fillRect(x- camera.getX(), y- camera.getY(), width, height);
        }

    }
}