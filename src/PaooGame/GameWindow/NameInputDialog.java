package PaooGame.GameWindow;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class NameInputDialog extends JPanel {
    private JTextField nameField;
    private JButton cancelButton;
    private JButton startButton;
    private BufferedImage[] backgroundLayers; // Array to hold the four background layers
    private final String PLACEHOLDER_TEXT = "Esperis"; // Placeholder text
    private final Color PLACEHOLDER_COLOR = new Color(255, 255, 255, 128); // Semi-transparent white
    private final Color NORMAL_COLOR = Color.WHITE; // Full-opacity white

    public NameInputDialog(Frame parent, ActionListener startListener, ActionListener cancelListener) {
        setOpaque(false); // Allow background images to show through
        setLayout(new GridBagLayout()); // Use GridBagLayout to position components inside the box
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(30, 5, 15, 5); // Reduced padding between components

        // Initialize the array for four background layers
        backgroundLayers = new BufferedImage[4];

        // Load the four background layers
        try {
            backgroundLayers[0] = ImageIO.read(getClass().getResource("/menu/1.png"));
            backgroundLayers[1] = ImageIO.read(getClass().getResource("/menu/3.png"));
            backgroundLayers[2] = ImageIO.read(getClass().getResource("/menu/4.png"));
        } catch (IOException e) {
            e.printStackTrace();
            for (int i = 0; i < backgroundLayers.length; i++) {
                backgroundLayers[i] = null;
            }
        }

        // Title
        JLabel titleLabel = new JLabel("Enter Your Name", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Medula one", Font.BOLD, 22)); // Reduced font size
        titleLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(titleLabel, gbc);

        // Name input field
        nameField = new JTextField();
        nameField.setFont(new Font("Medula One", Font.PLAIN, 20)); // Reduced font size
        nameField.setHorizontalAlignment(JTextField.CENTER);
        nameField.setPreferredSize(new Dimension(60, 40)); // Reduced size
        nameField.setBackground(new Color(27,32,42));

        // Set initial placeholder state
        nameField.setText(PLACEHOLDER_TEXT);
        nameField.setForeground(PLACEHOLDER_COLOR);

        // Add FocusListener to handle placeholder behavior
        nameField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (nameField.getText().equals(PLACEHOLDER_TEXT)) {
                    nameField.setText("");
                    nameField.setForeground(NORMAL_COLOR);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (nameField.getText().isEmpty()) {
                    nameField.setText(PLACEHOLDER_TEXT);
                    nameField.setForeground(PLACEHOLDER_COLOR);
                }
            }
        });

        gbc.gridy = 1;
        gbc.gridwidth = 2;
        add(nameField, gbc);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 2)); // Reduced spacing
        buttonPanel.setOpaque(false);

        // Cancel button (pink text)
        cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Medula One", Font.BOLD, 20)); // Reduced font size
        cancelButton.setForeground(new Color(242, 181, 175)); // Pink
        cancelButton.setContentAreaFilled(false);
        cancelButton.setBorderPainted(false);
        cancelButton.setFocusPainted(false);
        cancelButton.setPreferredSize(new Dimension(100, 25)); // Reduced size
        cancelButton.addActionListener(cancelListener);
        buttonPanel.add(cancelButton);

        // Start Game button (green text)
        startButton = new JButton("Start Game");
        startButton.setFont(new Font("Medula one", Font.BOLD, 20)); // Reduced font size
        startButton.setForeground(new Color(195, 240, 168)); // Green
        startButton.setContentAreaFilled(false);
        startButton.setBorderPainted(false);
        startButton.setFocusPainted(false);
        startButton.setPreferredSize(new Dimension(150, 25)); // Reduced size
        startButton.addActionListener(startListener);
        buttonPanel.add(startButton);

        gbc.gridy = 2;
        gbc.gridwidth = 2;
        add(buttonPanel, gbc);

        // Set dialog size
        setPreferredSize(new Dimension(180, 120));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw each background layer in order
        for (int i = 0; i < backgroundLayers.length; i++) {
            if (backgroundLayers[i] != null) {
                g.drawImage(backgroundLayers[i], 0, 0, getWidth(), getHeight(), this);
            }
        }

        // Draw the dialog box
        Graphics2D g2 = (Graphics2D) g.create();
        int boxWidth = getWidth() / 2;  // Half the panel width
        int boxHeight = getHeight() / 2; // Half the panel height
        int x = (getWidth() - boxWidth) / 2;
        int y = (getHeight() - boxHeight) / 2;

        g2.setColor(new Color(35, 47, 69, 255)); // Dark blue
        g2.fillRoundRect(x, y, boxWidth, boxHeight, 20, 20);

        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(2f));
        g2.drawRoundRect(x, y, boxWidth, boxHeight, 20, 20);

        g2.dispose();
    }

    public String getPlayerName() {
        return nameField.getText().trim();
    }
}