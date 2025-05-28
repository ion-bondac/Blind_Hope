package PaooGame.Database;

import java.sql.Timestamp;

public class GameSession {
    private int sessionId;
    private String playerName;
    private int playerX;
    private int playerY;
    private int level;
    private int health;
    private int score;
    private Timestamp saveDate;

    public GameSession(int sessionId, String playerName, int playerX, int playerY, int level, int health, int score, Timestamp saveDate) {
        this.sessionId = sessionId;
        this.playerName = playerName;
        this.playerX = playerX;
        this.playerY = playerY;
        this.level = level;
        this.health = health;
        this.score = score;
        this.saveDate = saveDate;
    }

    public int getSessionId() { return sessionId; }
    public String getPlayerName() { return playerName; }
    public int getPlayerX() { return playerX; }
    public int getPlayerY() { return playerY; }
    public int getLevel() { return level; }
    public int getHealth() { return health; }
    public int getScore() { return score; }
    public Timestamp getSaveDate() { return saveDate; }
}