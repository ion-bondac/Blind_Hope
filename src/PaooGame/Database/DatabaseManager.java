package PaooGame.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:./game_database.db";

    public DatabaseManager() {
        try {
            Class.forName("org.sqlite.JDBC");
            createTable();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("SQLite JDBC driver not found: " + e.getMessage(), e);
        }
    }

    private Connection connect() throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL);
        System.out.println("Connected to database: " + DB_URL);
        return conn;
    }

    private void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS game_sessions (" +
                "session_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "player_x INTEGER NOT NULL," +
                "player_y INTEGER NOT NULL," +
                "save_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table game_sessions created or already exists");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create table: " + e.getMessage(), e);
        }
    }

    public void saveSession(int playerX, int playerY) {
        String sql = "INSERT INTO game_sessions (player_x, player_y) VALUES (?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, playerX);
            pstmt.setInt(2, playerY);
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Session saved successfully. Rows affected: " + rowsAffected);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save session: " + e.getMessage(), e);
        }
    }

    public GameSession loadLatestSession() {
        String sql = "SELECT session_id, player_x, player_y, save_date FROM game_sessions " +
                "ORDER BY save_date DESC LIMIT 1";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return new GameSession(
                        rs.getInt("session_id"),
                        rs.getInt("player_x"),
                        rs.getInt("player_y"),
                        rs.getTimestamp("save_date")
                );
            }
            System.out.println("No sessions found in database");
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load session: " + e.getMessage(), e);
        }
    }

    // New method to fetch all sessions
    public List<GameSession> loadAllSessions() {
        List<GameSession> sessions = new ArrayList<>();
        String sql = "SELECT session_id, player_x, player_y, save_date FROM game_sessions ORDER BY save_date DESC";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                sessions.add(new GameSession(
                        rs.getInt("session_id"),
                        rs.getInt("player_x"),
                        rs.getInt("player_y"),
                        rs.getTimestamp("save_date")
                ));
            }
            System.out.println("Loaded " + sessions.size() + " sessions from database");
            return sessions;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load sessions: " + e.getMessage(), e);
        }
    }
}