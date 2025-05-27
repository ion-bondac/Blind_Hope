package PaooGame.GameWindow;

import PaooGame.Database.DatabaseManager;
import PaooGame.Database.GameSession;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

public class LoadGamePanel extends JPanel {
    private JTable sessionTable;
    private JScrollPane scrollPane;
    private JButton loadButton;
    private JButton deleteButton;
    private JButton cancelButton;
    private Consumer<GameSession> loadSessionCallback;
    private Menu mainMenu;
    private PauseMenu pauseMenu;
    private DatabaseManager dbManager; // Add DatabaseManager reference
    private List<GameSession> sessions; // Store sessions for refreshing

    public LoadGamePanel(List<GameSession> sessions, Consumer<GameSession> loadSessionCallback, Menu mainMenu, PauseMenu pauseMenu, DatabaseManager dbManager) {
        this.sessions = sessions;
        this.loadSessionCallback = loadSessionCallback;
        this.mainMenu = mainMenu;
        this.pauseMenu = pauseMenu;
        this.dbManager = dbManager; // Initialize DatabaseManager
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(20, 30, 50));

        // Initialize table with session data
        updateSessionTable();

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(20, 30, 50));

        // Load button
        loadButton = new JButton("Load");
        styleButton(loadButton);
        loadButton.addActionListener(e -> {
            int selectedRow = sessionTable.getSelectedRow();
            if (selectedRow >= 0) {
                GameSession selectedSession = sessions.get(selectedRow);
                loadSessionCallback.accept(selectedSession);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a session to load!", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });
        buttonPanel.add(loadButton);

        // Delete button
        deleteButton = new JButton("Delete");
        styleButton(deleteButton);
        deleteButton.addActionListener(e -> {
            int selectedRow = sessionTable.getSelectedRow();
            if (selectedRow >= 0) {
                int sessionId = (int) sessionTable.getValueAt(selectedRow, 0);
                try {
                    dbManager.deleteSession(sessionId);
                    // Refresh session list and table
                    this.sessions.clear();
                    this.sessions.addAll(dbManager.loadAllSessions());
                    updateSessionTable();
                    JOptionPane.showMessageDialog(this, "Session deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (RuntimeException ex) {
                    JOptionPane.showMessageDialog(this, "Failed to delete session: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a session to delete!", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });
        buttonPanel.add(deleteButton);

        // Cancel button
        cancelButton = new JButton("Cancel");
        styleButton(cancelButton);
        cancelButton.addActionListener(e -> {
            Container parent = getParent();
            if (parent != null) {
                parent.remove(this);
                if (pauseMenu != null) {
                    parent.add(pauseMenu, BorderLayout.CENTER); // Return to pause menu
                    pauseMenu.setVisible(true);
                } else {
                    parent.add(mainMenu, BorderLayout.CENTER); // Fallback to main menu
                    mainMenu.setVisible(true);
                }
                parent.revalidate();
                parent.repaint();
                parent.requestFocusInWindow();
                System.out.println("Returned to " + (pauseMenu != null ? "pause menu" : "main menu"));
            }
        });
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }

    private void updateSessionTable() {
        // Remove existing scroll pane if present
        if (scrollPane != null) {
            remove(scrollPane);
        }

        // Create table data with only ID, Name, Level, and Save Date
        String[] columns = {"Session ID", "Name", "Level", "Save Date"};
        Object[][] data = new Object[sessions.size()][4];
        for (int i = 0; i < sessions.size(); i++) {
            GameSession session = sessions.get(i);
            data[i][0] = session.getSessionId();
            data[i][1] = session.getPlayerName();
            data[i][2] = session.getLevel();
            data[i][3] = session.getSaveDate().toString();
        }

        // Create and configure table
        sessionTable = new JTable(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        sessionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        sessionTable.setBackground(new Color(40, 60, 100));
        sessionTable.setForeground(Color.WHITE);
        sessionTable.setFont(new Font("Arial", Font.PLAIN, 14));
        sessionTable.setRowHeight(25);

        scrollPane = new JScrollPane(sessionTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(80, 110, 160), 2));
        add(scrollPane, BorderLayout.CENTER);

        // Refresh UI
        revalidate();
        repaint();
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(40, 60, 100));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(80, 110, 160), 2));
        button.setPreferredSize(new Dimension(120, 40));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(50, 80, 130));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(40, 60, 100));
            }
        });
    }
}