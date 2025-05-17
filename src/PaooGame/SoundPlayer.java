package PaooGame;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class SoundPlayer {
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
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}
