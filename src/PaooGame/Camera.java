package PaooGame;

public class Camera {
    private int x, y;
    private int screenWidth, screenHeight;

    public Camera(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

//    public void update(Player player) {
//        this.x = player.getX() - screenWidth / 2 + 16;
//        this.y = player.getY() - screenHeight / 2 + 16;
//        if (x < 0) x = 0;
//        if (y < 0) y = 0;
//    }

    public void update(Player player) {
        int targetX = player.getX() - screenWidth / 2 + 16;
        int targetY = player.getY() - screenHeight / 2 + 16;

        // Smooth interpolation (easing)
        x += (targetX - x) * 0.1;
        y += (targetY - y) * 0.1;

        // Clamp values if needed
        if (x < 32) x = 32;
        if (y < 0) y = 0;
        if (y > 41*32 - 480) y = 41*32 - 480;
        if (x > 91*32 - 800) x = 91*32 - 800;  //map width - screen width
    }


    public int getX() { return x; }
    public int getY() { return y; }

    public int getScreenHeight() { return screenHeight; }

    public int getScreenWidth() { return screenWidth; }
}
