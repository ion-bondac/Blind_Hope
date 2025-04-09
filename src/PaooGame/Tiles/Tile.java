package PaooGame.Tiles;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Tile {
    private final String type;
    private final BufferedImage texture;

    public Tile(String type, BufferedImage texture) {
        this.type = type;
        this.texture = texture;
    }

    public void draw(Graphics g, int x, int y) {
        g.drawImage(texture, x, y, null);
    }

    public String getType() {
        return type;
    }
}
