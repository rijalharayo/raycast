package main.audio;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

// Represents different sound effects
public class SoundEffect {
     // Audio clip used to play the sound effect
     private final Clip clip;
     // Main audio folder
     private static final String AUDIO_FOLDER = "resources/sounds/";

     // Default sound effects
     public static final SoundEffect LEVEL_COMPLETE = new SoundEffect("level-complete.wav");

     // Loads the sound effect from the given resource path
     public SoundEffect(String path) {
          try {
               File audioFile = new File(AUDIO_FOLDER  + path);

               AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);

               clip = AudioSystem.getClip();
               clip.open(audioInputStream);

          } 
          catch (Exception e) {
               throw new RuntimeException("Failed to load sound: " + path, e);
          }
     }

     // Plays the sound effect from the beginning
     public void play() {
          clip.setFramePosition(0);
          clip.start();
     }

     // Stops the sound effect
     public void stop() {
          clip.stop();
     }
}