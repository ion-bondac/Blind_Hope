package PaooGame;

import PaooGame.GameWindow.GameWindow;
import PaooGame.Graphics.Assets;
import PaooGame.Tiles.Tile;
import PaooGame.Tiles.TileFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;



public class GameMap {
    private Tile[][] mapTiles;
    private TileFactory tileFactory;
    private BufferedImage background;
    private BufferedImage cloud1;
    private BufferedImage cloud2;
    private BufferedImage cloud3;
    private BufferedImage rocks;
    private BufferedImage rocks2;
    private BufferedImage rocks3;
    private int[] cloudsX;
    private int[] cloudsY;
    private int delay = 0;
    private int level;
    public SoundPlayer MapSound;

    public GameMap(String filename, TileFactory factory,int level) throws IOException {
        this.tileFactory = factory;
        loadMapFromFile(filename);

        try {
            if (level == 1) {
                background = ImageIO.read(new File("res/textures/Level1/lvl1-bg.png"));
                cloud1 = ImageIO.read(new File("res/textures/Level1/lvl1-cloud1.png"));
                cloud2 = ImageIO.read(new File("res/textures/Level1/lvl1-cloud2.png"));
                cloud3 = ImageIO.read(new File("res/textures/Level1/lvl1-cloud3.png"));
            } else if (level == 2) {
                background = ImageIO.read(new File("res/textures/Level2/LEVEL2BG.png"));
            }else if (level == 3) {
                background = ImageIO.read(new File("res/textures/Level3/LEVEL3BG.png"));

            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        cloudsX = new int[]{
                100,
                0,
                150,
                200,
                250,
                400,
                -300,
                480,
                500,
                -600,
                -700
        };

        cloudsY = new int[] {
                200,
                0,
                0,
                100,
                50,
                100,
                200,
                150,
                0,
                150,
                0
        };
        MapSound.stopBackgroundSound();
        if(level == 1){
            MapSound.playLoopingSound("/sounds/level1Music.wav", 0.8f);
        } else if (level == 2) {
            MapSound.playLoopingSound("/sounds/level2Music.wav", 0.8f);
        }
        else{
            MapSound.playLoopingSound("/sounds/level3Music.wav", 0.8f);
        }
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
                String tileId = tokens[x].trim();
                mapTiles[y][x] = tileFactory.getTile(tileId);            }
        }
    }

//    public void render(Graphics g) {
//        for (int y = 0; y < mapTiles.length; y++) {
//            for (int x = 0; x < mapTiles[y].length; x++) {
//                mapTiles[y][x].draw(g, x * 32, y * 32); // Assuming 32x32 tiles
//            }
//        }
//    }

    public void update(){
        delay++;
        if(delay % 15 == 0){
            for(int i=0; i<11; i++){
                cloudsX[i]++;
            }
        }
    }

public void render(Graphics g, Camera camera, Player target) {
    int tileSize = 32;
    int screenWidth = camera.getScreenWidth();  // Add this to Camera class
    int screenHeight = camera.getScreenHeight(); // Add this to Camera class



    if (background != null) {
        int bgX = 0;
        int bgY = 0;
        g.drawImage(background, -bgX, -bgY, null);
    }

    g.drawImage(cloud1, cloudsX[0]-camera.getX()/5, cloudsY[0]-camera.getY()/5, 600,320, null);
    g.drawImage(cloud2, cloudsX[1]-camera.getX()/5, cloudsY[1]-camera.getY()/5, null);
    g.drawImage(cloud3, cloudsX[2]-camera.getX()/5, cloudsY[2]-camera.getY()/5, 600,320,null);
    g.drawImage(cloud1, cloudsX[3]-camera.getX()/5, cloudsY[3]-camera.getY()/5, null);
    g.drawImage(cloud2, cloudsX[4]-camera.getX()/5, cloudsY[4]-camera.getY()/5, 600,320,null);
    g.drawImage(cloud3, cloudsX[5]-camera.getX()/5, cloudsY[5]-camera.getY()/5, null);
    g.drawImage(cloud1, cloudsX[6]-camera.getX()/5, cloudsY[6]-camera.getY()/5, 600,320,null);
    g.drawImage(cloud2, cloudsX[7]-camera.getX()/5, cloudsY[7]-camera.getY()/5, null);
    g.drawImage(cloud3, cloudsX[8]-camera.getX()/5, cloudsY[8]-camera.getY()/5, null);
    g.drawImage(cloud1, cloudsX[9]-camera.getX()/5, cloudsY[9]-camera.getY()/5, null);
    g.drawImage(cloud2, cloudsX[10]-camera.getX()/5, cloudsY[10]-camera.getY()/5, null);


//    g.drawImage(rocks,0, 50, null);
//    g.drawImage(rocks2, 0-camera.getX()/2, -camera.getY()/3, null);

    // Calculate visible tile range
    int startX = Math.max(0, camera.getX() / tileSize);
    int startY = Math.max(0, camera.getY() / tileSize);
    int endX = Math.min(mapTiles[0].length, (camera.getX() + screenWidth) / tileSize + 1);
    int endY = Math.min(mapTiles.length, (camera.getY() + screenHeight) / tileSize + 1);


    if(target.blindfolded){
        startX = Math.max(0, (target.getX() - 300) /tileSize);
        endX = Math.min(mapTiles[0].length,(target.getX() + 300) /tileSize);
        startY = Math.max(0,(target.getY() - 200) /tileSize);
        endY = Math.min(mapTiles.length,(target.getY() + 200) /tileSize);
    }

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
    public boolean isCactus(int x, int y) {
        return mapTiles[y][x].getType().equals("11"); // ID-ul pentru cactus
    }
}

