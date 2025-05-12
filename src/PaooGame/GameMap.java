package PaooGame;

import PaooGame.GameWindow.GameWindow;
import PaooGame.Graphics.Assets;
import PaooGame.Tiles.Tile;
import PaooGame.Tiles.TileFactory;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;



public class GameMap {
    private Tile[][] mapTiles;
    private TileFactory tileFactory;

    public GameMap(String filename, TileFactory factory) throws IOException {
        this.tileFactory = factory;
        loadMapFromFile(filename);
    }
    public boolean isWalkable(int x, int y){
        return mapTiles[y][x].walkable;
    }
    public boolean isFloor(int x, int y){
        return mapTiles[y][x].getType().equals("56");
    }

    private void loadMapFromFile(String filename) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filename));
        int rows = lines.size();
        int cols = lines.get(0).split(",").length;
        mapTiles = new Tile[rows][cols];

        for (int y = 0; y < rows; y++) {
            String[] tokens = lines.get(y).split(",");
            for (int x = 0; x < tokens.length; x++) {
                mapTiles[y][x] = tileFactory.getTile(tokens[x].trim());
            }
        }
    }

//    public void render(Graphics g) {
//        for (int y = 0; y < mapTiles.length; y++) {
//            for (int x = 0; x < mapTiles[y].length; x++) {
//                mapTiles[y][x].draw(g, x * 32, y * 32); // Assuming 32x32 tiles
//            }
//        }
//    }
public void render(Graphics g, Camera camera) {
    int tileSize = 32;
    int screenWidth = camera.getScreenWidth();  // Add this to Camera class
    int screenHeight = camera.getScreenHeight(); // Add this to Camera class

    // Calculate visible tile range
    int startX = Math.max(0, camera.getX() / tileSize);
    int startY = Math.max(0, camera.getY() / tileSize);
    int endX = Math.min(mapTiles[0].length, (camera.getX() + screenWidth) / tileSize + 1);
    int endY = Math.min(mapTiles.length, (camera.getY() + screenHeight) / tileSize + 1);

    // Render only visible tiles
    for (int y = startY; y < endY; y++) {
        for (int x = startX; x < endX; x++) {
            mapTiles[y][x].draw(g,
                    x * tileSize - camera.getX(),
                    y * tileSize - camera.getY()
            );
        }
    }
}

}

