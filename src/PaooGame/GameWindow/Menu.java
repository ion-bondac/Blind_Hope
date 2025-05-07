package PaooGame.GameWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;


public class Menu extends JPanel {
    private boolean isVisible;
    private Rectangle settingsBounds;
    private boolean settingsHovered = false;
    private ActionListener settingsActionListener;

    public Menu() {
        setLayout(new GridBagLayout());
        setBackground(Color.BLACK);
        isVisible = true;
        settingsBounds = new Rectangle();

        setupMenuButtons();
        setupSettingsInteraction();
    }

    private void setupMenuButtons() {
        GridBagConstraints gbc = new GridBagConstraints();

        // Main menu buttons
        JButton newGameButton = createMenuButton("New Game");
        JButton loadGameButton = createMenuButton("Load Game");
        JButton exitButton = createMenuButton("Exit");

        // Constraints for main buttons
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 50, 10, 50);

        // Add space above buttons
        gbc.weighty = 1.0;
        gbc.gridy = 0;
        add(Box.createVerticalGlue(), gbc);

        // Add main buttons
        gbc.weighty = 0.0;
        gbc.gridy = 1;
        add(newGameButton, gbc);
        gbc.gridy = 2;
        add(loadGameButton, gbc);
        gbc.gridy = 3;
        add(exitButton, gbc);

        // Add space below buttons
        gbc.weighty = 0.2;
        gbc.gridy = 4;
        add(Box.createVerticalGlue(), gbc);
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 24));
        button.setBackground(Color.DARK_GRAY);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        return button;
    }

    private void setupSettingsInteraction() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (settingsBounds.contains(e.getPoint())) {
                    if (settingsActionListener != null) {
                        settingsActionListener.actionPerformed(
                                new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Settings"));
                    }
                }
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                boolean wasHovered = settingsHovered;
                settingsHovered = settingsBounds.contains(e.getPoint());
                if (wasHovered != settingsHovered) {
                    repaint(settingsBounds);
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Position settings button 30px from right edge and 10px from bottom
        int buttonSize = 30;
        settingsBounds = new Rectangle(
                getWidth() - buttonSize - 30,  // 30px from right edge
                getHeight() - buttonSize - 10, // 10px from bottom
                buttonSize,
                buttonSize
        );

        // Draw button
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Button background
        g2d.setColor(settingsHovered ? new Color(80, 80, 80) : new Color(60, 60, 60));
        g2d.fillOval(settingsBounds.x, settingsBounds.y, buttonSize, buttonSize);

        // Button border
        g2d.setColor(settingsHovered ? Color.WHITE : Color.LIGHT_GRAY);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawOval(settingsBounds.x, settingsBounds.y, buttonSize, buttonSize);

        // Draw asterisk
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        FontMetrics fm = g2d.getFontMetrics();
        int textX = settingsBounds.x + (buttonSize - fm.stringWidth("*"))/2;
        int textY = settingsBounds.y + ((buttonSize - fm.getHeight())/2) + fm.getAscent();
        g2d.drawString("*", textX, textY);
    }

    public void addActionListenerToButton(String buttonText, ActionListener listener) {
        for (Component comp : getComponents()) {
            if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                if (button.getText().equals(buttonText)) {
                    button.addActionListener(listener);
                }
            }
        }
    }

    public void addSettingsActionListener(ActionListener listener) {
        this.settingsActionListener = listener;
    }

    public boolean isMenuShowing() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

}