package PaooGame.GameWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class PauseMenu extends JPanel {
    public PauseMenu(Runnable saveAction, Runnable loadAction, Runnable exitAction, Runnable resumeAction) {
        setLayout(new GridBagLayout());
        setBackground(new Color(20, 30, 50, 200)); // Semi-transparent dark background
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
                g2.setColor(new Color(80, 110, 160));
                g2.setStroke(new BasicStroke(2f));
                g2.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 15, 15);
                FontMetrics fm = g2.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                int textY = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2.setColor(new Color(0, 0, 0, 100));
                g2.drawString(getText(), textX+1, textY+1);
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
}