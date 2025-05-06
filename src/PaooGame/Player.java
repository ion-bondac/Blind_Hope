package PaooGame;
import java.awt.*;
import java.awt.event.KeyEvent;

public class Player {
    private int x, y; // position in tiles
    private final int size = 32;
    private Color color = Color.RED;

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void move(int dx, int dy, GameMap map) {

        int newX = x + dx;
        int newY = y + dy;
        //if walkable
        x=newX;
        y=newY;
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(x * size, y * size, size, size);
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getSize() { return size; }
}
