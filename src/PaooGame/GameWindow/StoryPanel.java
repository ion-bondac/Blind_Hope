package PaooGame.GameWindow;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

public class StoryPanel extends JPanel {
    private BufferedImage[] backgroundLayers;
    private BufferedImage gnaritas;
    private JLabel storyLabel;
    private JButton continueButton;
    private List<String> sentences;
    private int currentSentenceIndex = 0;
    private Timer textTimer;
    private final Color BOX_COLOR = new Color(35, 47, 69, 200);
    private final int BOX_HEIGHT = 150;
    private final int TEXT_DELAY = 3000;
    private final int TEXT_INDENT = 30; // Indentare text

    public StoryPanel(Frame parent, ActionListener continueListener, List<String> levelSentences) {
        setOpaque(false);
        setLayout(new BorderLayout());

        this.sentences = levelSentences; // Folosim propozițiile primite ca parametru

        loadBackgroundImages();
        setupUI(continueListener);
        setPreferredSize(new Dimension(600, 400));
    }

    private void loadBackgroundImages() {
        backgroundLayers = new BufferedImage[4];
        try {
            backgroundLayers[0] = ImageIO.read(getClass().getResource("/menu/1.png"));
            backgroundLayers[1] = ImageIO.read(getClass().getResource("/menu/3.png"));
            backgroundLayers[2] = ImageIO.read(getClass().getResource("/menu/4.png"));
            gnaritas = ImageIO.read(getClass().getResource("/sprites/gnaritas.png"));
        } catch (IOException e) {
            e.printStackTrace();
            for (int i = 0; i < backgroundLayers.length; i++) {
                backgroundLayers[i] = null;
            }
        }
    }

    private void setupUI(ActionListener continueListener) {
        // Create the story box panel
        JPanel storyBoxPanel = new JPanel(new BorderLayout());
        storyBoxPanel.setOpaque(false);
        storyBoxPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Story label with right alignment and padding
        storyLabel = new JLabel("", SwingConstants.LEFT);
        storyLabel.setFont(new Font("Medula One", Font.PLAIN, 18));
        storyLabel.setForeground(Color.WHITE);
        storyLabel.setBorder(BorderFactory.createEmptyBorder(0, TEXT_INDENT, 0, 0)); // Indentare la stânga

        // Use HTML for proper text wrapping and alignment
        if (!sentences.isEmpty()) {
            storyLabel.setText("<html><div style='width:400px; padding-left:" + TEXT_INDENT + "px;'>" +
                    sentences.get(0) + "</div></html>");
        }

        storyBoxPanel.add(storyLabel, BorderLayout.CENTER);

        // Timer for displaying sentences
        textTimer = new Timer(TEXT_DELAY, e -> showNextSentence());
        textTimer.setRepeats(true);
        textTimer.start();

        // Continue button
        continueButton = createContinueButton(continueListener);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        buttonPanel.add(continueButton);
        storyBoxPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(storyBoxPanel, BorderLayout.SOUTH);
    }

    private JButton createContinueButton(ActionListener continueListener) {
        JButton button = new JButton("Continue");
        button.setFont(new Font("Medula One", Font.BOLD, 20));
        button.setForeground(new Color(195, 240, 168));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setEnabled(false);
        button.addActionListener(e -> {
            textTimer.stop();
            continueListener.actionPerformed(e);
        });
        return button;
    }

    private void showNextSentence() {
        currentSentenceIndex++;
        if (currentSentenceIndex < sentences.size()) {
            storyLabel.setText("<html><div style='width:400px; padding-left:" + TEXT_INDENT + "px;'>" +
                    sentences.get(currentSentenceIndex) + "</div></html>");
        } else {
            textTimer.stop();
            continueButton.setEnabled(true);
        }
    }

    public void setSentences(List<String> newSentences) {
        this.sentences = newSentences;
        this.currentSentenceIndex = 0;
        if (!sentences.isEmpty()) {
            storyLabel.setText("<html><div style='width:400px; padding-left:" + TEXT_INDENT + "px;'>" +
                    sentences.get(0) + "</div></html>");
        }
        continueButton.setEnabled(false);
        textTimer.restart();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw background layers
        for (BufferedImage layer : backgroundLayers) {
            if (layer != null) {
                g.drawImage(layer, 0, 0, getWidth(), getHeight(), this);
            }
        }

        g.drawImage(gnaritas,50,140, 128,128,null);

        // Draw story box at bottom
        Graphics2D g2 = (Graphics2D) g.create();
        int boxWidth = getWidth() - 100;
        int x = (getWidth() - boxWidth) / 2;
        int y = getHeight() - BOX_HEIGHT - 20;

        g2.setColor(BOX_COLOR);
        g2.fillRoundRect(x, y, boxWidth, BOX_HEIGHT, 20, 20);

        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(2f));
        g2.drawRoundRect(x, y, boxWidth, BOX_HEIGHT, 20, 20);

        g2.dispose();
    }
}