package PaooGame;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SoundPlayer {
    private static final Map<String, Clip> activeClips = new HashMap<>();
    private static float volume = 1.0f; // Default volume (100%)

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

            // Apply volume control if supported
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

    public static void setVolume(float newVolume) {
        volume = newVolume; // newVolume should be between 0.0 (silent) and 1.0 (full volume)

        // Update volume for all active clips
        for (Clip clip : activeClips.values()) {
            if (clip != null && clip.isOpen() && clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                float range = gainControl.getMaximum() - gainControl.getMinimum();
                float gain = (range * volume) + gainControl.getMinimum();
                gainControl.setValue(gain);
            }
        }
    }

    public static float getVolume() {
        return volume;
    }
}