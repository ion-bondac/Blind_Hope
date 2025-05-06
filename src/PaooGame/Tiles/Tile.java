package PaooGame.Tiles;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Tile {
    private final String type;
    private final BufferedImage texture;
    public final boolean walkable;

    public Tile(String type, BufferedImage texture, boolean isWalkable) {
        this.type = type;
        this.texture = texture;
        this.walkable = isWalkable;
    }

    public void draw(Graphics g, int x, int y) {
        g.drawImage(texture, x, y, null);
    }

    public String getType() {
        return type;
    }
}
