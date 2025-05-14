package PaooGame.Database;

import java.sql.Timestamp;

public class GameSession {
    private int sessionId;
    private int playerX;
    private int playerY;
    private Timestamp saveDate;
    public GameSession(int sessionId, int playerX, int playerY, Timestamp saveDate) {
        this.sessionId = sessionId;
        this.playerX = playerX;
        this.playerY = playerY;
        this.saveDate = saveDate;
    }
    public int getSessionId() { return sessionId; }
    public int getPlayerX() { return playerX; }
    public int getPlayerY() { return playerY; }
    public Timestamp getSaveDate() { return saveDate; }
}