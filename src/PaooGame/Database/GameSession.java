package PaooGame.Database;

import java.sql.Timestamp;

public class GameSession {
    private int sessionId;
    private int playerX;
    private int playerY;
    private int level;
    private int health;
    private Timestamp saveDate;

    public GameSession(int sessionId, int playerX, int playerY, int level, int health, Timestamp saveDate) {
        this.sessionId = sessionId;
        this.playerX = playerX;
        this.playerY = playerY;
        this.level = level;
        this.health = health;
        this.saveDate = saveDate;
    }

    public int getSessionId() { return sessionId; }
    public int getPlayerX() { return playerX; }
    public int getPlayerY() { return playerY; }
    public int getLevel() { return level; }
    public int getHealth() { return health; }
    public Timestamp getSaveDate() { return saveDate; }
}