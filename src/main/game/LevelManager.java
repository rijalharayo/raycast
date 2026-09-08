package main.game;

import java.util.Collection;
import java.util.HashMap;

import main.game.scenes.LevelScene;
import main.game.scenes.levels.Level1;
import main.game.scenes.levels.Level2;

// Manages all the levels
public class LevelManager {
     private static final HashMap<Integer, LevelScene> levels = new HashMap<>();
     private static int currentLevelIndex;

     // Adds a level to the list
     private static void addLevel(LevelScene levelScene) {
          int lvlIdx = levelScene.getLevelIndex();

          if(levels.containsKey(lvlIdx)) {
               throw new IllegalArgumentException("Level " + lvlIdx + " already exists!");
          }

          levels.put(
               lvlIdx,
               levelScene
          );
     }

     // Initializes the levels
     public static void initializeLevels() {
          // Sets the list of levels

          addLevel(new Level1());
          addLevel(new Level2());
     }

     // Sets the current level
     public static void setCurrentLevel(int levelIndex) {
          if(!levels.containsKey(levelIndex)) {
               throw new IllegalArgumentException("Level " + levelIndex + " doesn't exist!");
          }

          // Sets the level scene
          SceneManager.setScene(
               levels.get(levelIndex)
          );

          currentLevelIndex = levelIndex;
     }

     // Returns the current level
     public static LevelScene getLevel(int levelIndex) {
          return levels.get(levelIndex);
     }

     // Returns the current level
     public static LevelScene getCurrentLevel() {
          return levels.get(currentLevelIndex);
     }

     // Returns the current level index
     public static int getCurrentLevelIndex() {
          return currentLevelIndex;
     }

     // Returns the list of all levels
     public static Collection<LevelScene> getAllLevels() {
          return levels.values();
     }
}