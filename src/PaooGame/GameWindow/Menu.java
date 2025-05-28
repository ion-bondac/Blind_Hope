package PaooGame.GameWindow;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;


public class Menu extends JPanel {
    private boolean isVisible;
    private Rectangle settingsBounds;
    private boolean settingsHovered = false;
    private ActionListener settingsActionListener;
    private final BufferedImage[] backgroundLayers = new BufferedImage[5];
    private BufferedImage settingsIconImage;

    public Menu() {
        setLayout(new GridBagLayout());
        setBackground(Color.BLACK);
        isVisible = true;
        settingsBounds = new Rectangle();

        loadBackgroundImages();
        setupMenuButtons();
        setupSettingsInteraction();
    }

    private void loadBackgroundImages() {
        try {
            backgroundLayers[0] = ImageIO.read(getClass().getResource("/menu/1.png"));
            backgroundLayers[1] = ImageIO.read(getClass().getResource("/menu/2.png"));
            backgroundLayers[2] = ImageIO.read(getClass().getResource("/menu/3.png"));
            backgroundLayers[3] = ImageIO.read(getClass().getResource("/menu/4.png"));
            backgroundLayers[4] = ImageIO.read(getClass().getResource("/menu/menu_logo.png"));
            settingsIconImage = ImageIO.read(getClass().getResource("/menu/settings.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();

                // Button background with gradient
                Color topColor = new Color(40, 60, 100); // Dark blue
                Color bottomColor = new Color(20, 30, 50); // Darker blue
                if (getModel().isPressed()) {
                    topColor = new Color(30, 50, 90);
                    bottomColor = new Color(15, 25, 45);
                } else if (getModel().isRollover()) {
                    topColor = new Color(50, 80, 130); // Lighter blue on hover
                    bottomColor = new Color(30, 50, 90);
                }

                // Draw gradient background
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
                return new Dimension(200, 50); // Uniform button size
            }
        };

        button.setFont(new Font("Arial", Font.BOLD, 22));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setForeground(Color.WHITE);

        return button;
    }

    private void setupSettingsInteraction() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (settingsBounds.contains(e.getPoint())) {
                    openSettingsWindow();
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

    private void openSettingsWindow() {
        SwingUtilities.invokeLater(() -> {
            Settings settingsWindow = new Settings();
            settingsWindow.setVisible(true);
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Draw background layers
        int index = 0;
        for (BufferedImage layer : backgroundLayers) {
            if (layer != null) {
                if(index == 1){
                    g2d.drawImage(layer, -100, 30, getWidth(), getHeight(), null);
                }
                else{
                    g2d.drawImage(layer, 0, 0, getWidth(), getHeight(), null);
                }
            }
            index++;
        }

        // Draw settings button in bottom right
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