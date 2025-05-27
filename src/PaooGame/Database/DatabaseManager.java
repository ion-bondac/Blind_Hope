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
                "player_name VARCHAR(50) NOT NULL," +
                "player_x INTEGER NOT NULL," +
                "player_y INTEGER NOT NULL," +
                "level INTEGER NOT NULL," +
                "health INTEGER NOT NULL," +
                "save_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            // Check if player_name column exists and add it if missing
            DatabaseMetaData meta = conn.getMetaData();
            ResultSet columns = meta.getColumns(null, null, "game_sessions", "player_name");
            if (!columns.next()) {
                String alterSql = "ALTER TABLE game_sessions ADD COLUMN player_name VARCHAR(50) NOT NULL DEFAULT 'Unknown'";
                try (Statement alterStmt = conn.createStatement()) {
                    alterStmt.execute(alterSql);
                    System.out.println("Added player_name column to game_sessions table");
                }
            }
            System.out.println("Table game_sessions created or already exists");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create table: " + e.getMessage(), e);
        }
    }

    public void saveSession(String playerName, int playerX, int playerY, int level, int health) {
        String sql = "INSERT INTO game_sessions (player_name, player_x, player_y, level, health) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, playerName);
            pstmt.setInt(2, playerX);
            pstmt.setInt(3, playerY);
            pstmt.setInt(4, level);
            pstmt.setInt(5, health);
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Session saved successfully for " + playerName + ". Rows affected: " + rowsAffected);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save session: " + e.getMessage(), e);
        }
    }

    public List<GameSession> loadAllSessions() {
        List<GameSession> sessions = new ArrayList<>();
        String sql = "SELECT session_id, player_name, player_x, player_y, level, health, save_date FROM game_sessions ORDER BY save_date DESC";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                sessions.add(new GameSession(
                        rs.getInt("session_id"),
                        rs.getString("player_name"),
                        rs.getInt("player_x"),
                        rs.getInt("player_y"),
                        rs.getInt("level"),
                        rs.getInt("health"),
                        rs.getTimestamp("save_date")
                ));
            }
            System.out.println("Loaded " + sessions.size() + " sessions from database");
            return sessions;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load sessions: " + e.getMessage(), e);
        }
    }

    public void deleteSession(int sessionId) {
        String sql = "DELETE FROM game_sessions WHERE session_id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, sessionId);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Session " + sessionId + " deleted successfully. Rows affected: " + rowsAffected);
            } else {
                System.out.println("No session found with ID: " + sessionId);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete session: " + e.getMessage(), e);
        }
    }
}