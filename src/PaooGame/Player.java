package PaooGame;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity {
    private int x, y; // position in tiles
    private final int size = 32;
    private Color color = Color.RED;
    public boolean onGround = false;
    public boolean attacking = false;
    public boolean isMoving = false;
    public boolean facingRight = true;
    public int attackCooldown = 0;
    public int maxAttackCooldown = 40;
    public boolean hurt = false;
    public boolean dead = false;
    public boolean blindfolded = false;
    public boolean equipBlindfold = false;
    public boolean removeBlindfold = false;
    public int gravity = 0;
    private BufferedImage standing;
    private BufferedImage standingBlindfolded;
    private BufferedImage spriteSheet;

    BufferedImage[] walkFrames = new BufferedImage[6];
    int walkFrameIndex = 0;
    int walkFrameDelay = 5; // număr de update-uri între schimbările de frame
    int walkFrameTick = 0;

    BufferedImage[] jumpFrames = new BufferedImage[8];
    int jumpFrameIndex = 0;
    int jumpFrameDelay = 7; // număr de update-uri între schimbările de frame
    int jumpFrameTick = 0;

    BufferedImage[] attackFrames = new BufferedImage[4];
    int attackFrameIndex = 0;
    int attackFrameDelay = 7; // număr de update-uri între schimbările de frame
    int attackFrameTick = 0;

    BufferedImage[] hurtFrames = new BufferedImage[4];
    int hurtFrameIndex = 0;
    int hurtFrameDelay = 5; // număr de update-uri între schimbările de frame
    int hurtFrameTick = 0;

    BufferedImage[] deathFrames = new BufferedImage[8];
    int deathFrameIndex = 0;
    int deathFrameDelay = 5; // număr de update-uri între schimbările de frame
    int deathFrameTick = 0;


    // BLINDFOLD ////////////////////

    BufferedImage[] blindfoldFrames = new BufferedImage[6];
    int blindfoldFrameIndex = 0;
    int blindfoldFrameDelay = 5; // număr de update-uri între schimbările de frame
    int blindfoldFrameTick = 0;

    BufferedImage[] blindWalkFrames = new BufferedImage[6];
    int blindWalkFrameIndex = 0;
    int blindWalkFrameDelay = 5; // număr de update-uri între schimbările de frame
    int blindWalkFrameTick = 0;

    BufferedImage[] blindJumpFrames = new BufferedImage[8];
    int blindJumpFrameIndex = 0;
    int blindJumpFrameDelay = 7; // număr de update-uri între schimbările de frame
    int blindJumpFrameTick = 0;

    BufferedImage[] blindAttackFrames = new BufferedImage[4];
    int blindAttackFrameIndex = 0;
    int blindAttackFrameDelay = 7; // număr de update-uri între schimbările de frame
    int blindAttackFrameTick = 0;

    BufferedImage[] blindHurtFrames = new BufferedImage[4];
    int blindHurtFrameIndex = 0;
    int blindHurtFrameDelay = 5; // număr de update-uri între schimbările de frame
    int blindHurtFrameTick = 0;

    BufferedImage[] blindDeathFrames = new BufferedImage[8];
    int blindDeathFrameIndex = 0;
    int blindDeathFrameDelay = 5; // număr de update-uri între schimbările de frame
    int blindDeathFrameTick = 0;

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
        this.speed = 4;
        this.health = 300;
        this.width = 32;
        this.height = 32;
        try{
            spriteSheet = ImageIO.read(getClass().getResource("/sprites/Esperis_Spritesheet.png"));
            standing = spriteSheet.getSubimage(0,0,size,size);
            standingBlindfolded = spriteSheet.getSubimage(size,0,size,size);
            for(int i = 0; i < walkFrames.length; i++) {
                walkFrames[i] = spriteSheet.getSubimage(i * size, size, size, size);
                blindWalkFrames[i] = spriteSheet.getSubimage(i*size, 7*size, size, size);
            }
            for(int i = 0; i < jumpFrames.length; i++) {
                jumpFrames[i] = spriteSheet.getSubimage(i * size, 2*size, size, size);
                blindJumpFrames[i] = spriteSheet.getSubimage(i * size, 8*size, size, size);
            }
            for(int i = 0; i < attackFrames.length; i++) {
                attackFrames[i] = spriteSheet.getSubimage(i * size, 3*size, size, size);
                blindAttackFrames[i] = spriteSheet.getSubimage(i * size, 9*size, size, size);
            }
            for(int i = 0; i < hurtFrames.length; i++) {
                hurtFrames[i] = spriteSheet.getSubimage(i * size, 4*size, size, size);
                blindHurtFrames[i] = spriteSheet.getSubimage(i * size, 10*size, size, size);
            }
            for(int i = 0; i < deathFrames.length; i++) {
                deathFrames[i] = spriteSheet.getSubimage(i * size, 5*size, size, size);
                blindDeathFrames[i] = spriteSheet.getSubimage(i * size, 11*size, size, size);
            }
            for(int i = 0; i < blindfoldFrames.length; i++){
                blindfoldFrames[i] = spriteSheet.getSubimage(i*size, 6*size, size, size);
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
        }
//        if (map.isWalkable(x/size, y/size + 1)){
//            onGround = false;
//        }
//        else{
//            onGround = true;
//        }
    }

    public void updateWalkAnimation(boolean moving) {
        if (moving) {
            walkFrameTick++;
            if (walkFrameTick >= walkFrameDelay) {
                walkFrameTick = 0;
                walkFrameIndex = (walkFrameIndex + 1) % walkFrames.length;
                if(onGround){
                    SoundPlayer.playSound("/sounds/walk2.wav");
                }
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

    public void respawn(int NewX, int NewY){
        x=NewX;
        y=NewY;
    }
    public void NextLevelRespawn(int NewX, int NewY){
        x=NewX;
        y=NewY;
        this.health = 300;
    }

@Override
public void update(GameMap gameMap){
//        System.out.println(health/100 + "vieti");
    ;
    if (attackCooldown > 0) {
        attackCooldown--;
    }
}

@Override
public void render(Graphics g, Camera camera) {
        BufferedImage frameToDraw;
        if (spriteSheet != null) {
            if(dead){
                if(blindfolded){
                    blindDeathFrameTick++;
                    if (blindDeathFrameTick >= blindDeathFrameDelay) {
                        blindDeathFrameTick = 0;
                        blindDeathFrameIndex++;
                    }
                    if (blindDeathFrameIndex >= blindDeathFrames.length) {
                        blindDeathFrameIndex = 0;
                        blindfolded = false;
                        equipBlindfold = false;
                        dead = false;
                        respawn(200,100);
                        health = 300;
                    }
                    frameToDraw = blindDeathFrames[blindDeathFrameIndex];
                }
                else{
                    deathFrameTick++;
                    if (deathFrameTick >= deathFrameDelay) {
                        deathFrameTick = 0;
                        deathFrameIndex++;
                    }
                    if (deathFrameIndex >= deathFrames.length) {
                        deathFrameIndex = 0;
                        dead = false;
                        respawn(200,100);
                        health = 300;
                    }
                    frameToDraw = deathFrames[deathFrameIndex];
                }

            }
            else if(hurt){
                if(blindfolded){
                    blindHurtFrameTick++;
                    if (blindHurtFrameTick >= blindHurtFrameDelay) {
                        blindHurtFrameTick = 0;
                        blindHurtFrameIndex++;
                    }
                    if (blindHurtFrameIndex >= blindHurtFrames.length) {
                        blindHurtFrameIndex = 0;
                        hurt = false;
                    }
                    frameToDraw = blindHurtFrames[blindHurtFrameIndex];
                }
                else{
                    hurtFrameTick++;
                    if (hurtFrameTick >= hurtFrameDelay) {
                        hurtFrameTick = 0;
                        hurtFrameIndex++;
                    }
                    if (hurtFrameIndex >= hurtFrames.length) {
                        hurtFrameIndex = 0;
                        hurt = false;
                    }
                    frameToDraw = hurtFrames[hurtFrameIndex];
                }
            }
            else if (attacking) {
                if(blindfolded){
                    blindAttackFrameTick++;
                    if (blindAttackFrameTick >= blindAttackFrameDelay) {
                        blindAttackFrameTick = 0;
                        blindAttackFrameIndex++;
                    }
                    if (blindAttackFrameIndex >= blindAttackFrames.length) {
                        blindAttackFrameIndex = 0;
                        attacking = false;
                    }
                    frameToDraw = blindAttackFrames[blindAttackFrameIndex];
                }
                else{
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

            }
            else if(isMoving){
                if(blindfolded){
                    frameToDraw = blindWalkFrames[walkFrameIndex];
                }
                else{
                    frameToDraw = walkFrames[walkFrameIndex];
                }
            }
            else if (!onGround) {
                if(blindfolded){
                    frameToDraw = blindJumpFrames[jumpFrameIndex];
                }
                else{
                    frameToDraw = jumpFrames[jumpFrameIndex];
                }
            }
            else if (equipBlindfold) {
                blindfoldFrameTick++;
                if (blindfoldFrameTick >= blindfoldFrameDelay) {
                    blindfoldFrameTick = 0;
                    blindfoldFrameIndex++;
                }
                if (blindfoldFrameIndex >= blindfoldFrames.length) {
                    blindfoldFrameIndex = 5;
                    blindfolded = true;
                    equipBlindfold = false;
                }
                frameToDraw = blindfoldFrames[blindfoldFrameIndex];

            }else if (removeBlindfold) {
                blindfoldFrameTick++;
                if (blindfoldFrameTick >= blindfoldFrameDelay) {
                    blindfoldFrameTick = 0;
                    blindfoldFrameIndex++;
                }

                if (blindfoldFrameIndex >= blindfoldFrames.length) {
                    blindfoldFrameIndex = 5;
                    blindfolded = false;
                    removeBlindfold = false;
                }
                frameToDraw = blindfoldFrames[blindfoldFrames.length -  blindfoldFrameIndex - 1];

            } else{
                if(blindfolded){
                    frameToDraw = standingBlindfolded;
                }
                else{
                    frameToDraw = standing;
                }
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


    public void Damage(int d){
        this.health -= d;
        this.hurt = true;
        SoundPlayer.playSound("/sounds/hurt.wav");
    }



    public int getHealth(){
        return health;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getSize() { return size; }
}
