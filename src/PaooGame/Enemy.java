package PaooGame;

import java.awt.*;

public class Enemy extends Entity {
    private Player target;
    public boolean isChasing = false;
    public int startX;
    private boolean movingRight = true;

    public Enemy(int x, int y, Player target) {
        this.x = x;
        this.y = y;
        this.speed = 4;
        this.health = 50;
        this.width = 32;
        this.height = 32;
        this.target = target;
        this.startX = x;
    }

    @Override
    public void update() {
        if(isChasing){
            // Simple chase logic
            if (target.getX() > x) x += speed;
            else if (target.getX() < x) x -= speed;

            if (target.getY() > y) y += speed;
            else if (target.getY() < y) y -= speed;
        }
        else {
                if (movingRight) {
                    x += speed;
                    if (x >= startX + 200) movingRight = false;
                } else {
                    x -= speed;
                    if (x <= startX - 200) movingRight = true;
                }
        }

    }

    @Override
    public void render(Graphics g, Camera camera) {
        g.setColor(Color.RED);
        g.fillRect(x- camera.getX(), y- camera.getY(), width, height);
    }
}