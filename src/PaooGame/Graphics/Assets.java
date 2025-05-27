package PaooGame.Graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/*! \class public class Assets
    \brief Clasa incarca fiecare element grafic necesar jocului.

    Game assets include tot ce este folosit intr-un joc: imagini, sunete, harti etc.
 */
public class Assets
{

    public static Map<String, BufferedImage> tileMap = new HashMap<>();

    /// Referinte catre elementele grafice (dale) utilizate in joc.
    public static BufferedImage block;
    public static BufferedImage sandTop;
    public static BufferedImage sandBottom;
    public static BufferedImage sandLeft;
    public static BufferedImage sandRight;
    public static BufferedImage sandLeftCorner;
    public static BufferedImage sandRightCorner;
    public static BufferedImage sand;
    public static BufferedImage plant;
    public static BufferedImage cactus;
    public static BufferedImage grass;
    public static BufferedImage flower;
    public static BufferedImage empty;
    public static BufferedImage floor;
    public static BufferedImage ceiling1;
    public static BufferedImage ceiling2;
    public static SpriteSheet sheet = new SpriteSheet(ImageLoader.LoadImage("/textures/LEVEL1_MAP.png"));


    /*! \fn public static void Init()
        \brief Functia initializaza referintele catre elementele grafice utilizate.

        Aceasta functie poate fi rescrisa astfel incat elementele grafice incarcate/utilizate
        sa fie parametrizate. Din acest motiv referintele nu sunt finale.
     */
    public static void Init(int level)
    {
        tileMap.clear();
        System.out.println("Clearing tileMap for level: " + level);

        try {
            String spriteSheetPath = level == 2 ? "/textures/LEVEL2_MAP.png" : (level == 3 ? "/textures/LEVEL3_MAP.png" : "/textures/LEVEL1_MAP.png");
            BufferedImage spriteSheetImage = ImageLoader.LoadImage(spriteSheetPath);
            if (spriteSheetImage == null) {
                throw new IOException("Sprite sheet image is null for path: " + spriteSheetPath);
            }
            sheet = new SpriteSheet(spriteSheetImage);
            System.out.println("Loaded sprite sheet: " + spriteSheetPath + ", dimensions: " +
                    spriteSheetImage.getWidth() + "x" + spriteSheetImage.getHeight());
        } catch (Exception e) {
            System.err.println("Failed to load sprite sheet for level " + level + ": " + e.getMessage());
            e.printStackTrace();
            return;
        }

        try {
            block = sheet.crop(0, 0);
            sandTop = sheet.crop(1, 0);
            sandBottom = sheet.crop(2, 0);
            sandLeft = sheet.crop(3, 0);
            sandRight = sheet.crop(4, 0);
            sandLeftCorner = sheet.crop(5, 0);
            sandRightCorner = sheet.crop(6, 0);
            sand = sheet.crop(7, 0);
            plant = sheet.crop(0, 1);
            cactus = sheet.crop(1, 1);
            grass = sheet.crop(2, 1);
            flower = sheet.crop(3, 1);
            empty = sheet.crop(4, 1);
            floor = sheet.crop(5, 1);
            ceiling1 = sheet.crop(7, 0);
            ceiling2 = sheet.crop(2, 0);

            tileMap.put("0", block);
            tileMap.put("1", sandTop);
            tileMap.put("2", sandBottom);
            tileMap.put("3", sandLeft);
            tileMap.put("4", sandRight);
            tileMap.put("5", sandLeftCorner);
            tileMap.put("6", sandRightCorner);
            tileMap.put("7", sand);
            tileMap.put("8", plant);
            tileMap.put("9", cactus);
            tileMap.put("10", grass);
            tileMap.put("11", flower);
            tileMap.put("-1", empty);
            tileMap.put("56", floor);
            tileMap.put("14", ceiling1);
            tileMap.put("15", ceiling2);

            System.out.println("Populated tileMap with " + tileMap.size() + " tiles for level: " + level);
            for (String key : tileMap.keySet()) {
                BufferedImage img = tileMap.get(key);
                System.out.println("Tile ID: " + key + ", image size: " +
                        (img != null ? img.getWidth() + "x" + img.getHeight() : "null"));
            }
        } catch (Exception e) {
            System.err.println("Failed to crop or map tiles for level " + level + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}
