package PaooGame;

public class Camera {
    private int x, y;
    private int screenWidth, screenHeight;

    public Camera(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    public void update(Player player) {
        this.x = player.getX() - screenWidth / 2 + 16;
        this.y = player.getY() - screenHeight / 2 + 16;
        if (x < 0) x = 0;
        if (y < 0) y = 0;
    }

    public int getX() { return x; }
    public int getY() { return y; }
}
