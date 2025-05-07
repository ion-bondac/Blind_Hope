package PaooGame.Tiles;

import PaooGame.Graphics.Assets;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TileFactory {
    private Map<String, Tile> tileCache = new HashMap<>();

    public Tile getTile(String type) {
        if (!tileCache.containsKey(type)) {
            BufferedImage texture = loadTexture(type);
            switch (type){
                case "sand":
                case "sandBlock":
                case "sandTop":
                case "sandBottom":
                case "sandLeft":
                case "sandRight":
                case "sandLeftCorner":
                case "sandRightCorner":
                    tileCache.put(type, new Tile(type, texture, false));
                    break;
                case "grass":
                case "cactus":
                case "plant":
                case "empty":
                    tileCache.put(type, new Tile(type, texture, true));
                    break;

            }

        }
        return tileCache.get(type);
    }

    private BufferedImage loadTexture(String type) {
//        try {
//            return Assets.grass;
//        } catch (IOException e) {
//            System.err.println("Failed to load texture: " + type);
//            return null;
//        }
        BufferedImage img = Assets.tileMap.get(type);
        if (img == null) {
            System.err.println("Unknown tile type: " + type);
        }
        return img;
    }
}
