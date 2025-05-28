package PaooGame.GameWindow;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class PauseMenu extends JPanel {
    private Rectangle settingsBounds;
    private boolean settingsHovered = false;
    private BufferedImage settingsIconImage;
    private final BufferedImage[] backgroundLayers = new BufferedImage[3];


    public PauseMenu(Runnable saveAction, Runnable loadAction, Runnable exitAction, Runnable resumeAction) {
        setLayout(new GridBagLayout());
        setBackground(new Color(20, 30, 50)); // Semi-transparent dark background
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 50, 10, 50);

        // Title
        JLabel titleLabel = new JLabel("Pause Menu", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        gbc.gridy = 0;
        add(titleLabel, gbc);

        // Buttons
        JButton saveButton = createMenuButton("Save Game");
        saveButton.addActionListener(e -> saveAction.run());
        JButton loadButton = createMenuButton("Load Game");
        loadButton.addActionListener(e -> loadAction.run());
        JButton exitButton = createMenuButton("Exit Game");
        exitButton.addActionListener(e -> exitAction.run());
        JButton resumeButton = createMenuButton("Resume");
        resumeButton.addActionListener(e -> resumeAction.run());

        // Add buttons
        gbc.weighty = 0.0;
        gbc.gridy = 1;
        add(saveButton, gbc);
        gbc.gridy = 2;
        add(loadButton, gbc);
        gbc.gridy = 3;
        add(exitButton, gbc);
        gbc.gridy = 4;
        add(resumeButton, gbc);

        // Add space below buttons
        gbc.weighty = 0.2;
        gbc.gridy = 5;
        add(Box.createVerticalGlue(), gbc);

        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Load settings icon
        try {
            settingsIconImage = ImageIO.read(getClass().getResource("/menu/settings.png"));
            System.out.println("Loaded settings icon for PauseMenu");
        } catch (IOException e) {
            System.err.println("Failed to load settings icon: " + e.getMessage());
            e.printStackTrace();
        }

        // Setup settings icon interaction
        setupSettingsInteraction();

        loadBackgroundImages();
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                Color topColor = new Color(40, 60, 100);
                Color bottomColor = new Color(20, 30, 50);
                if (getModel().isPressed()) {
                    topColor = new Color(30, 50, 90);
                    bottomColor = new Color(15, 25, 45);
                } else if (getModel().isRollover()) {
                    topColor = new Color(50, 80, 130);
                    bottomColor = new Color(30, 50, 90);
                }
                GradientPaint gp = new GradientPaint(
                        0, 0, topColor,
                        0, getHeight(), bottomColor
                );
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                // Draw border
                g2.setColor(new Color(80, 110, 160)); // Light blue border
                g2.setStroke(new BasicStroke(2f));
                g2.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 15, 15);

                // Draw text with shadow effect
                FontMetrics fm = g2.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                int textY = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();

                // Text shadow
                g2.setColor(new Color(0, 0, 0, 100));
                g2.drawString(getText(), textX+1, textY+1);

                // Main text
                g2.setColor(Color.WHITE);
                g2.drawString(getText(), textX, textY);

                g2.dispose();
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(200, 50);
            }
        };
        button.setFont(new Font("Arial", Font.BOLD, 22));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setForeground(Color.WHITE);
        return button;
    }

    private void loadBackgroundImages() {
        try {
            backgroundLayers[0] = ImageIO.read(getClass().getResource("/menu/1.png"));
            backgroundLayers[1] = ImageIO.read(getClass().getResource("/menu/3.png"));
            backgroundLayers[2] = ImageIO.read(getClass().getResource("/menu/4.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openSettingsWindow() {
        SwingUtilities.invokeLater(() -> {
            System.out.println("Opening Settings window from PauseMenu");
            Settings settingsWindow = new Settings();
            settingsWindow.setVisible(true);
        });
    }

    private void setupSettingsInteraction() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (settingsBounds.contains(e.getPoint())) {
                    openSettingsWindow();
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
        Graphics2D g2d = (Graphics2D) g;

        // Draw background layers
        for (BufferedImage layer : backgroundLayers) {
            if (layer != null) {
                g2d.drawImage(layer, 0, 0, getWidth(), getHeight(), null);
            }
        }

        // Draw settings icon in bottom right
        int buttonSize = 50;
        int margin = 30;
        settingsBounds = new Rectangle(
                getWidth() - buttonSize - margin,
                getHeight() - buttonSize - margin,
                buttonSize,
                buttonSize
        );

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Settings button with glass effect
        Color baseColor = new Color(70, 100, 150, 180); // Semi-transparent blue
        if (settingsHovered) {
            baseColor = new Color(90, 130, 190, 220); // Brighter when hovered
        }

        // Outer glow effect on hover
        if (settingsHovered) {
            g2d.setColor(new Color(100, 150, 220, 80));
            g2d.fillOval(
                    settingsBounds.x - 5,
                    settingsBounds.y - 5,
                    buttonSize + 10,
                    buttonSize + 10
            );
        }

        // Button body
        g2d.setColor(baseColor);
        g2d.fillRoundRect(
                settingsBounds.x,
                settingsBounds.y,
                buttonSize,
                buttonSize,
                25,
                25
        );

        // Draw icon
        if (settingsIconImage != null) {
            int iconSize = 30;
            int iconX = settingsBounds.x + (buttonSize - iconSize) / 2;
            int iconY = settingsBounds.y + (buttonSize - iconSize) / 2;
            g2d.drawImage(settingsIconImage, iconX, iconY, iconSize, iconSize, null);
        } else {
            // Fallback icon
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 24));
            FontMetrics fm = g2d.getFontMetrics();
            String icon = "⚙";
            int textX = settingsBounds.x + (buttonSize - fm.stringWidth(icon)) / 2;
            int textY = settingsBounds.y + ((buttonSize - fm.getHeight()) / 2) + fm.getAscent();
            g2d.drawString(icon, textX, textY);
        }
    }
}