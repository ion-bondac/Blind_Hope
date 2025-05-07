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
    public int gravity = 0;
    private BufferedImage sprite;

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
        try{
            sprite = ImageIO.read(getClass().getResource("/sprites/Esperis_static_tile.png"));
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

    public void draw(Graphics g) {
        if (sprite != null) {
            g.drawImage(sprite, x, y, size, size, null);
        } else {
            g.setColor(color);
            g.fillRect(x, y, size, size); // fallback dacă imaginea nu e încărcată
        }
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getSize() { return size; }
}
