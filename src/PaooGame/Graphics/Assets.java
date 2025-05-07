package PaooGame.Graphics;

import java.awt.image.BufferedImage;
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
//    public static BufferedImage playerLeft;
//    public static BufferedImage playerRight;
//    public static BufferedImage soil;
//    public static BufferedImage grass;
//    public static BufferedImage mountain;
//    public static BufferedImage townGrass;
//    public static BufferedImage townGrassDestroyed;
//    public static BufferedImage townSoil;
//    public static BufferedImage water;
//    public static BufferedImage rockUp;
//    public static BufferedImage rockDown;
//    public static BufferedImage rockLeft;
//    public static BufferedImage rockRight;
//    public static BufferedImage tree;

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
    public static BufferedImage empty;


    /*! \fn public static void Init()
        \brief Functia initializaza referintele catre elementele grafice utilizate.

        Aceasta functie poate fi rescrisa astfel incat elementele grafice incarcate/utilizate
        sa fie parametrizate. Din acest motiv referintele nu sunt finale.
     */
    public static void Init()
    {
            /// Se creaza temporar un obiect SpriteSheet initializat prin intermediul clasei ImageLoader
        SpriteSheet sheet = new SpriteSheet(ImageLoader.LoadImage("/textures/LEVEL1_MAP.png"));

            /// Se obtin subimaginile corespunzatoare elementelor necesare.
//        grass = sheet.crop(0, 0);
//        soil = sheet.crop(1, 0);
//        water = sheet.crop(2, 0);
//        mountain = sheet.crop(3, 0);
//        townGrass = sheet.crop(0, 1);
//        townGrassDestroyed = sheet.crop(1, 1);
//        townSoil = sheet.crop(2, 1);
//        tree = sheet.crop(3, 1);
//        playerLeft = sheet.crop(0, 2);
//        playerRight = sheet.crop(1, 2);
//        rockUp = sheet.crop(2, 2);
//        rockDown = sheet.crop(3, 2);
//        rockLeft = sheet.crop(0, 3);
//        rockRight = sheet.crop(1, 3);

//        sand = sheet.crop(1,6);
//        ground = sheet.crop(7,6);
//        cactus = sheet.crop(3,14);
//        grass = sheet.crop(6,15);
//        sky = sheet.crop(1,0);
//

        block = sheet.crop(0,0);
        sandTop = sheet.crop(1,0);
        sandBottom = sheet.crop(2,0);
        sandLeft = sheet.crop(3,0);
        sandRight = sheet.crop(4,0);
        sandLeftCorner = sheet.crop(5,0);
        sandRightCorner = sheet.crop(6,0);
        sand = sheet.crop(7,0);
        plant = sheet.crop(0,1);
        cactus = sheet.crop(1,1);
        grass = sheet.crop(2,1);
        empty = sheet.crop(3,1);


//
//        tileMap.put("grass", grass);
//        tileMap.put("soil", soil);
//        tileMap.put("water", water);
//        tileMap.put("mountain", mountain);
//        tileMap.put("townGrass", townGrass);
//        tileMap.put("townGrassDestroyed", townGrassDestroyed);
//        tileMap.put("townSoil", townSoil);
//        tileMap.put("tree", tree);
//        tileMap.put("playerLeft", playerLeft);
//        tileMap.put("playerRight", playerRight);
//        tileMap.put("rockUp", rockUp);
//        tileMap.put("rockDown", rockDown);
//        tileMap.put("rockLeft", rockLeft);
//        tileMap.put("rockRight", rockRight);

        tileMap.put("sandBlock", block);
        tileMap.put("sandTop", sandTop);
        tileMap.put("sandBottom", sandBottom);
        tileMap.put("sandLeft", sandLeft);
        tileMap.put("sandRight", sandRight);
        tileMap.put("sandLeftCorner", sandLeftCorner);
        tileMap.put("sandRightCorner", sandRightCorner);
        tileMap.put("sand", sand);
        tileMap.put("plant", plant);
        tileMap.put("cactus", cactus);
        tileMap.put("grass", grass);
        tileMap.put("empty", empty);

//        tileMap.put("ground", ground);
//        tileMap.put("cactus", cactus);
//        tileMap.put("grass", grass);
//        tileMap.put("sky", sky);
    }
}
