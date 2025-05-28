package PaooGame;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;

public class Enemy extends Entity {
    private final Player target;
    public boolean isChasing = false;
    public int startX;
    private boolean movingRight;
    private final String type;
    private BufferedImage spriteSheet;
    private int range;
    private int cooldown = 0;
    private int maxCooldownd = 50;
//    private boolean waitingToTurn = false;
//    private int turnDelay = 3; // număr de frame-uri de pauză (poți ajusta)
//    private int turnDelayCounter = 0;

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


    //LORD

    BufferedImage staticLord;

    private boolean attacking = false;
    BufferedImage[] attackFrames = new BufferedImage[8];
    int attackFrameIndex = 0;
    int attackFrameDelay = 3; // număr de update-uri între schimbările de frame
    int attackFrameTick = 0;

    private boolean running = false;

    BufferedImage[] runFrames = new BufferedImage[8];
    int runFrameIndex;
    int runFrameDelay = 7; // număr de update-uri între schimbările de frame
    int runFrameTick = 0;


    public Enemy(int x, int y, Player target, String type, boolean right) {
        Random rand = new Random();
        this.x = x + rand.nextInt(80);
        this.y = y;
        this.health = 100;
        this.target = target;
        this.startX = x;
        this.type = type;
        this.movingRight = right;

        if(type.equals("Eagle")){
            this.speed = 2;
            this.range = 80;
            this.width = 32;
            this.height = 32;
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
        else if (type.equals("Gnome")){
            this.range = 50;
            this.speed=2;
            this.width = 32;
            this.height = 32;
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
        else{
            this.range=80;
            this.speed=2;
            this.width = 64;
            this.height = 64;
            this.isChasing = true;
            try{
                spriteSheet = ImageIO.read(Objects.requireNonNull(getClass().getResource("/sprites/lordSpritesheet.png")));
                staticLord = spriteSheet.getSubimage(0, 0, width, height);
                for(int i = 0; i < attackFrames.length; i++) {
                    attackFrames[i] = spriteSheet.getSubimage(i * width, height, width, height);
                }
                for(int i = 0; i < runFrames.length; i++) {
                    runFrames[i] = spriteSheet.getSubimage(i * width, 2*height, width, height);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void update(GameMap gameMap, Enemy boss) {

//        if (waitingToTurn) {
//            turnDelayCounter++;
//            if (turnDelayCounter >= turnDelay) {
//                waitingToTurn = false;
//                turnDelayCounter = 0;
//                movingRight = !movingRight;
//            }
//
//        }

        if(cooldown > 0){
            cooldown--;
        }

        if(isChasing){
            // Simple chase logic
            if(running){
                runFrameTick++;
                if (runFrameTick >= runFrameDelay) {
                    runFrameTick = 0;
                    runFrameIndex = (runFrameIndex + 1) % runFrames.length;
                }
            }
            if (target.getX() >= x + 32){
                if(cooldown == 0) {
                    if (x < startX + range) {
                        x += speed;
                        running = true;
                    }
                    else{
                        running = false;
                    }
                }
                    movingRight = false;
                }
            else if (target.getX() < x - 32){
                if(cooldown == 0) {
                    if (x > startX - range) {
                        x -= speed;
                        running = true;
                    }
                    else{
                        running = false;
                    }
                }
                    movingRight = true;
                }
            else{
                running = false;
            }


//            if (target.getY() > y) y += speed;
//            else if (target.getY() < y) y -= speed;
//            flyFrameIndex= 0;
//            flyFrameTick = 0;
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


//        if(x > target.getX()-target.getSize()  && x < target.getX() + target.getSize()){
//            if(y > target.getY() + target.getSize() && y < target.getY() - target.getSize()){
//                target.Damage(20);
//            }
//        }
        if(type.equals("Lord")){
            int offset;
            if(movingRight){
                offset = -1;
            }
            else{
                offset = 1;
            }

            if((x/32 + offset == (target.getX())/32 || x/32 == (target.getX())/32 ) && (y/32 == target.getY()/32 || y/32 + 1 == target.getY()/32)){
                if(target.attacking){
                    isHurt = true;
                    health -=20;
                    if(health <30){
                        speed -=1;
                    }
                    if(target.facingRight){
                        x+=92;
                    }
                    else{
                        x-=92;
                    }
                    if(health == 0){
//                    isHurt = false;
                        this.active = false;
                    }
                }
                else{
                    if(!target.hurt){
                        attacking = true;
                        cooldown = maxCooldownd;
                        target.Damage(100);

                        if(attackFrameIndex >= 7){
                            target.attackCooldown = 0;
                            if(movingRight){
                                target.move(92, 0, gameMap);
                                target.gravity=-10;
                                target.move(0, target.gravity++, gameMap);
                                x = target.getX() - 192;
//                            movingRight = false;
                            }
                            else{
                                target.move(-92, 0, gameMap);
                                target.gravity=-10;
                                target.move(0, target.gravity++, gameMap);
                                x = target.getX() +192;
//                            movingRight = true;
                            }
                        }
                    }

                }
            }

        }
        else{
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
//                    isHurt = false;
                        target.addScore(100);
                        this.active = false;
                    }
                }
                else{
                    target.Damage(100);
                    target.attackCooldown = 0;
                    if(movingRight){
                        x-=60;
                        movingRight = true;
                        target.move(32, 0, gameMap);
                    }
                    else{
                        x+=60;
                        movingRight = false;
                        target.move(-32, 0, gameMap);
                    }
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
            if(attacking){
                attackFrameTick++;
                if (attackFrameTick >= attackFrameDelay) {
                    attackFrameTick = 0;
                    attackFrameIndex++;
                }
                if (attackFrameIndex >= attackFrames.length) {
                    attackFrameIndex = 0;
                    attacking = false;
                }
                frameToDraw = attackFrames[attackFrameIndex];
            }
            else if(running){
                frameToDraw = runFrames[runFrameIndex];
            }
            else{
                frameToDraw = staticLord;
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
//            g.setColor(Color.RED);
//            g.fillRect(x- camera.getX(), y- camera.getY(), width, height);
        }

    }

    public void kill(){
        this.active = false;
    }

    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }

    public void Damage(int x){
        health-=x;
        if(health <= 0){
            active = false;
        }
    }

}