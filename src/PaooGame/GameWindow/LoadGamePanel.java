package PaooGame.GameWindow;

import PaooGame.Database.GameSession;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

public class LoadGamePanel extends JPanel {
    private JTable sessionTable;
    private JScrollPane scrollPane;
    private JButton loadButton;
    private JButton cancelButton;
    private Consumer<GameSession> loadSessionCallback;
    private Menu mainMenu;
    private PauseMenu pauseMenu; // Add for pause menu

    public LoadGamePanel(List<GameSession> sessions, Consumer<GameSession> loadSessionCallback, Menu mainMenu, PauseMenu pauseMenu) {
        this.loadSessionCallback = loadSessionCallback;
        this.mainMenu = mainMenu;
        this.pauseMenu = pauseMenu;
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(20, 30, 50));

        String[] columns = {"Session ID", "X Position", "Y Position", "Save Date"};
        Object[][] data = new Object[sessions.size()][4];
        for (int i = 0; i < sessions.size(); i++) {
            GameSession session = sessions.get(i);
            data[i][0] = session.getSessionId();
            data[i][1] = session.getPlayerX();
            data[i][2] = session.getPlayerY();
            data[i][3] = session.getSaveDate().toString();
        }

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

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(20, 30, 50));

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