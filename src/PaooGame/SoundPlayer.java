package PaooGame;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SoundPlayer {
    private static final Map<String, Clip> activeClips = new HashMap<>();
    private static float volume = 1.0f; // Default volume (100%)
    private static Clip backgroundClip;

    public static void playSound(String fileName) {
        try {
            URL soundURL = SoundPlayer.class.getResource(fileName);
            if (soundURL == null) {
                System.err.println("Could not find sound file: " + fileName);
                return;
            }
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundURL);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);

            // Apply volume control
            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                float range = gainControl.getMaximum() - gainControl.getMinimum();
                float gain = (range * volume) + gainControl.getMinimum();
                gainControl.setValue(gain);
            }

            clip.start();
            activeClips.put(fileName, clip);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public static void playLoopingSound(String fileName) {
        try {
            URL soundURL = SoundPlayer.class.getResource(fileName);
            if (soundURL == null) {
                System.err.println("Could not find sound file: " + fileName);
                return;
            }
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundURL);
            backgroundClip = AudioSystem.getClip();
            backgroundClip.open(audioStream);

            // Apply global volume control
            if (backgroundClip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl gainControl = (FloatControl) backgroundClip.getControl(FloatControl.Type.MASTER_GAIN);
                float range = gainControl.getMaximum() - gainControl.getMinimum();
                float gain = (range * volume) + gainControl.getMinimum();
                gainControl.setValue(gain);
            }

            backgroundClip.loop(Clip.LOOP_CONTINUOUSLY); // Loop forever
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public static void stopBackgroundSound() {
        if (backgroundClip != null && backgroundClip.isRunning()) {
            backgroundClip.stop();
            backgroundClip.close();
            backgroundClip = null; // Clear reference after stopping
        }
    }

    public static void setVolume(float newVolume) {
        volume = Math.max(0.0f, Math.min(newVolume, 1.0f)); // Clamp volume between 0.0 and 1.0

        // Update volume for all active clips
        for (Clip clip : activeClips.values()) {
            if (clip != null && clip.isOpen() && clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                float range = gainControl.getMaximum() - gainControl.getMinimum();
                float gain = (range * volume) + gainControl.getMinimum();
                gainControl.setValue(gain);
            }
        }

        // Update volume for background clip
        if (backgroundClip != null && backgroundClip.isOpen() && backgroundClip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl gainControl = (FloatControl) backgroundClip.getControl(FloatControl.Type.MASTER_GAIN);
            float range = gainControl.getMaximum() - gainControl.getMinimum();
            float gain = (range * volume) + gainControl.getMinimum();
            gainControl.setValue(gain);
        }
    }

public static float getVolume() {
    return volume;
}
}