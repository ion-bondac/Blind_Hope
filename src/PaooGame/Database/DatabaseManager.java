package PaooGame.Database;

import PaooGame.Enemy;

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
        String gameSessionsSql = "CREATE TABLE IF NOT EXISTS game_sessions (" +
                "session_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "player_name VARCHAR(50) NOT NULL," +
                "player_x INTEGER NOT NULL," +
                "player_y INTEGER NOT NULL," +
                "level INTEGER NOT NULL," +
                "health INTEGER NOT NULL," +
                "score INTEGER NOT NULL DEFAULT 0," +
                "save_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")";

        String enemyStatesSql = "CREATE TABLE IF NOT EXISTS enemy_states (" +
                "enemy_state_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "session_id INTEGER NOT NULL," +
                "enemy_index INTEGER NOT NULL," +
                "is_active BOOLEAN NOT NULL," +
                "FOREIGN KEY (session_id) REFERENCES game_sessions(session_id)" +
                ")";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            // Create game_sessions table
            stmt.execute(gameSessionsSql);

            // Check and add score column if missing
            DatabaseMetaData meta = conn.getMetaData();
            ResultSet columns = meta.getColumns(null, null, "game_sessions", "score");
            if (!columns.next()) {
                String alterSql = "ALTER TABLE game_sessions ADD COLUMN score INTEGER NOT NULL DEFAULT 0";
                try (Statement alterStmt = conn.createStatement()) {
                    alterStmt.execute(alterSql);
                    System.out.println("Added score column to game_sessions table");
                }
            }

            // Create enemy_states table
            stmt.execute(enemyStatesSql);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create/alter table: " + e.getMessage(), e);
        }
    }

    public void saveSession(String playerName, int playerX, int playerY, int level, int health, int score, List<Enemy> enemyList) {
        String sessionSql = "INSERT INTO game_sessions (player_name, player_x, player_y, level, health, score) VALUES (?, ?, ?, ?, ?, ?)";
        String enemyStateSql = "INSERT INTO enemy_states (session_id, enemy_index, is_active) VALUES (?, ?, ?)";

        try (Connection conn = connect()) {
            // Disable auto-commit to ensure atomicity
            conn.setAutoCommit(false);

            // Save game session and get the generated session_id
            int sessionId;
            try (PreparedStatement pstmt = conn.prepareStatement(sessionSql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, playerName);
                pstmt.setInt(2, playerX);
                pstmt.setInt(3, playerY);
                pstmt.setInt(4, level);
                pstmt.setInt(5, health);
                pstmt.setInt(6, score);
                int rowsAffected = pstmt.executeUpdate();
                System.out.println("Session saved successfully for " + playerName + ". Rows affected: " + rowsAffected);

                // Get the generated session_id
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    sessionId = rs.getInt(1);
                } else {
                    throw new SQLException("Failed to retrieve session_id.");
                }
            }

            // Save enemy states
            try (PreparedStatement pstmt = conn.prepareStatement(enemyStateSql)) {
                for (int i = 0; i < enemyList.size(); i++) {
                    Enemy enemy = enemyList.get(i);
                    pstmt.setInt(1, sessionId);
                    pstmt.setInt(2, i); // Store the index of the enemy in the EnemyList
                    pstmt.setBoolean(3, enemy.isActive());
                    pstmt.executeUpdate();
                }
            }

            // Commit the transaction
            conn.commit();
            System.out.println("Enemy states saved successfully for session " + sessionId);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save session: " + e.getMessage(), e);
        }
    }

    public List<GameSession> loadAllSessions() {
        List<GameSession> sessions = new ArrayList<>();
        String sql = "SELECT session_id, player_name, player_x, player_y, level, health, score, save_date FROM game_sessions ORDER BY save_date DESC";
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
                        rs.getInt("score"),
                        rs.getTimestamp("save_date")
                ));
            }
            System.out.println("Loaded " + sessions.size() + " sessions from database");
            return sessions;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load sessions: " + e.getMessage(), e);
        }
    }

    public List<Boolean> loadEnemyStates(int sessionId) {
        List<Boolean> enemyStates = new ArrayList<>();
        String sql = "SELECT enemy_index, is_active FROM enemy_states WHERE session_id = ? ORDER BY enemy_index";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, sessionId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                boolean isActive = rs.getBoolean("is_active");
                enemyStates.add(isActive);
            }
            System.out.println("Loaded " + enemyStates.size() + " enemy states for session " + sessionId);
            return enemyStates;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load enemy states: " + e.getMessage(), e);
        }
    }

    public void deleteSession(int sessionId) {
        String deleteEnemiesSql = "DELETE FROM enemy_states WHERE session_id = ?";
        String deleteSessionSql = "DELETE FROM game_sessions WHERE session_id = ?";
        try (Connection conn = connect()) {
            conn.setAutoCommit(false);
            // Delete enemy states first
            try (PreparedStatement pstmt = conn.prepareStatement(deleteEnemiesSql)) {
                pstmt.setInt(1, sessionId);
                pstmt.executeUpdate();
            }
            // Delete session
            try (PreparedStatement pstmt = conn.prepareStatement(deleteSessionSql)) {
                pstmt.setInt(1, sessionId);
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Session " + sessionId + " deleted successfully. Rows affected: " + rowsAffected);
                } else {
                    System.out.println("No session found with ID: " + sessionId);
                }
            }
            conn.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete session: " + e.getMessage(), e);
        }
    }
}