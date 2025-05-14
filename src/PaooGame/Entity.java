package PaooGame;

import java.awt.*;

public abstract class Entity {
    protected int x, y;
    // Position
    protected int width, height;  // Size
    protected int speed;
    protected int health;
    protected boolean active = true;
    public abstract void update(GameMap gameMap);
    public abstract void render(Graphics g, Camera camera);
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
    public boolean isActive() {
        return active;
    }
}