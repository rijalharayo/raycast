package main.game;

import java.util.HashMap;
import java.util.Set;

import main.game.scenes.LevelScene;
import main.game.scenes.MenuScene;
import main.game.scenes.levels.Level1;
import main.game.scenes.levels.Level2;
import main.game.scenes.levels.Level3;
import main.game.scenes.levels.Level4;
import main.game.scenes.levels.Level5;
import main.game.scenes.levels.Level6;
import main.game.scenes.levels.Level7;

// Manages all the levels
public class LevelManager {
     // Maps level classes to indices
     private static final HashMap<Integer, Class<? extends LevelScene>> levels = new HashMap<>();
     private static int currentLevelIndex;
     private static LevelScene currentLevelScene;

     // Adds a level class to the list
     private static void addLevel(int levelIndex, Class<? extends LevelScene> levelClass) {
          if(levels.containsKey(levelIndex)) {
               throw new IllegalArgumentException("Level " + levelIndex + " already exists!");
          }

          levels.put(
               levelIndex,
               levelClass
          );
     }

     // Initializes the levels
     public static void initializeLevels() {
          // Sets the list of levels

          addLevel(1, Level1.class);
          addLevel(2, Level2.class);
          addLevel(3, Level3.class);
          addLevel(4, Level4.class);
          addLevel(5, Level5.class);
          addLevel(6, Level6.class);
          addLevel(7, Level7.class);
     }

     // Creates the level instance to be used as a scene
     private static LevelScene createLevel(int levelIndex) {
          Class<? extends LevelScene> levelClass = levels.get(levelIndex);

          try {
               return levelClass.getDeclaredConstructor().newInstance();
          }
          catch (Exception e) {
               throw new RuntimeException(
                    "Failed to create level " + levelIndex,
                    e
               );
          }
     }

     // Sets the current level
     public static void setCurrentLevel(int levelIndex) {
          if(!levels.containsKey(levelIndex)) {
               System.out.println("Level " + levelIndex + " doesn't exist!");
               SceneManager.setScene(MenuScene.LEVEL_MENU());
               return;
          }

          // Creates the level scene
          LevelScene level = createLevel(levelIndex);

          // Sets the level scene
          SceneManager.setScene(
               level
          );

          currentLevelIndex = levelIndex;
          currentLevelScene = level;
     }

     // Returns the current level
     public static LevelScene getLevel(int levelIndex) {
          return createLevel(levelIndex);
     }

     // Returns the current level
     public static LevelScene getCurrentLevel() {
          return currentLevelScene;
     }

     // Returns the current level index
     public static int getCurrentLevelIndex() {
          return currentLevelIndex;
     }

     // Returns the list of all levels' indices
     public static Set<Integer> getAllLevelIndices() {
          return levels.keySet();
     }
}