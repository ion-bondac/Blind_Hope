package PaooGame.GameWindow;

import PaooGame.SoundPlayer;

import javax.swing.*;
import javax.sound.sampled.*;
import java.awt.*;

public class Settings extends JFrame {
    private final FloatControl masterGainControl; // Control for system volume
    private final JSlider musicVolumeSlider;
    private final JCheckBox skipTextCheckBox;
    private final JSlider textSpeedSlider;
    private static int textSpeed = 1; // Static variable to store text speed (default to 1)
    private static boolean skipText = false; // Static variable to store skip text state (default to false)

    public Settings() {
        setTitle("Game Settings");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        // Initialize system volume control
        masterGainControl = initializeSystemVolumeControl();

        // Main panel with dark background
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(35, 47, 69)); // #1C2526

        // Title
        JLabel titleLabel = new JLabel("Game Settings", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Settings content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayout(0, 1, 10, 10));
        contentPanel.setBackground(new Color(35, 47, 69)); // Match background

        // Music Volume control
        JPanel musicVolumePanel = new JPanel(new BorderLayout(10, 0));
        JLabel musicVolumeLabel = new JLabel("Music Volume");
        musicVolumeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        musicVolumeLabel.setForeground(Color.WHITE);
        musicVolumeSlider = new JSlider(0, 100, (int)(SoundPlayer.getVolume() * 100)); // Range 0-100%
        musicVolumeSlider.setBackground(new Color(35, 47, 69));
        musicVolumeSlider.setForeground(Color.WHITE);
        musicVolumeSlider.setMajorTickSpacing(25);
        musicVolumeSlider.setPaintTicks(true);
        musicVolumeSlider.setPaintLabels(true);

        // Adjust sound volume when slider changes
        musicVolumeSlider.addChangeListener(e -> {
            int sliderValue = musicVolumeSlider.getValue();
            float volume = sliderValue / 100.0f; // Convert to 0.0-1.0 range
            SoundPlayer.setVolume(volume);
        });

        // Set initial slider value based on current system volume
        if (masterGainControl != null) {
            float currentGain = masterGainControl.getValue();
            float minGain = masterGainControl.getMinimum();
            float maxGain = masterGainControl.getMaximum();
            int sliderValue = (int) (((currentGain - minGain) / (maxGain - minGain)) * 100);
            musicVolumeSlider.setValue(sliderValue);
        }

        // Adjust system volume when slider changes
        musicVolumeSlider.addChangeListener(e -> {
            if (masterGainControl != null) {
                int sliderValue = musicVolumeSlider.getValue();
                float minGain = masterGainControl.getMinimum();
                float maxGain = masterGainControl.getMaximum();
                float newGain = minGain + (sliderValue / 100.0f) * (maxGain - minGain);
                masterGainControl.setValue(newGain);
            }
        });
        musicVolumePanel.add(musicVolumeLabel, BorderLayout.WEST);
        musicVolumePanel.add(musicVolumeSlider, BorderLayout.CENTER);
        musicVolumePanel.setBackground(new Color(35, 47, 69));
        contentPanel.add(musicVolumePanel);

        // Skip Text checkbox
        JPanel skipTextPanel = new JPanel(new BorderLayout(10, 0));
        JLabel skipTextLabel = new JLabel("Skip Text");
        skipTextLabel.setFont(new Font("Arial", Font.BOLD, 16));
        skipTextLabel.setForeground(Color.WHITE);
        skipTextCheckBox = new JCheckBox();
        skipTextCheckBox.setBackground(new Color(35, 47, 69));
        skipTextCheckBox.setSelected(skipText); // Initialize with current skipText state
        skipTextPanel.add(skipTextLabel, BorderLayout.WEST);
        skipTextPanel.add(skipTextCheckBox, BorderLayout.CENTER);
        skipTextPanel.setBackground(new Color(35, 47, 69));
        contentPanel.add(skipTextPanel);

        // Text Speed control
        JPanel textSpeedPanel = new JPanel(new BorderLayout(10, 0));
        JLabel textSpeedLabel = new JLabel("Text Speed");
        textSpeedLabel.setFont(new Font("Arial", Font.BOLD, 16));
        textSpeedLabel.setForeground(Color.WHITE);
        textSpeedSlider = new JSlider(1, 4, textSpeed); // Range 1-4, initialized with current textSpeed
        textSpeedSlider.setBackground(new Color(35, 47, 69));
        textSpeedSlider.setForeground(Color.WHITE);
        textSpeedSlider.setMajorTickSpacing(1);
        textSpeedSlider.setPaintTicks(true);
        textSpeedSlider.setPaintLabels(true);
        textSpeedPanel.add(textSpeedLabel, BorderLayout.WEST);
        textSpeedPanel.add(textSpeedSlider, BorderLayout.CENTER);
        textSpeedPanel.setBackground(new Color(35, 47, 69));
        contentPanel.add(textSpeedPanel);

        // Save button
        JButton saveButton = new JButton("Save Settings");
        saveButton.setBackground(new Color(35, 47, 69));
        saveButton.setForeground(Color.WHITE);
        saveButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(20, 0, 0, 0), // 20px space above the button
                saveButton.getBorder()
        ));
        saveButton.addActionListener(e -> {
            // Save settings
            textSpeed = textSpeedSlider.getValue();
            skipText = skipTextCheckBox.isSelected();
            dispose();
        });

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(saveButton, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private FloatControl initializeSystemVolumeControl() {
        try {
            Mixer.Info[] mixers = AudioSystem.getMixerInfo();
            for (Mixer.Info mixerInfo : mixers) {
                Mixer mixer = AudioSystem.getMixer(mixerInfo);
                if (mixer.isLineSupported(Port.Info.SPEAKER)) {
                    Port port = (Port) mixer.getLine(Port.Info.SPEAKER);
                    port.open();
                    if (port.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                        return (FloatControl) port.getControl(FloatControl.Type.MASTER_GAIN);
                    }
                }
            }
        } catch (LineUnavailableException e) {
            System.err.println("Error accessing system volume control: " + e.getMessage());
        }
        return null;
    }

    // Getter for text speed
    public static int getTextSpeed() {
        return textSpeed;
    }

    // Getter for skip text
    public static boolean isSkipText() {
        return skipText;
    }
}