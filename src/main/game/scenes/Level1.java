package main.game.scenes;

import main.game.LevelScene;
import main.models.Sprite;

// First level
public class Level1 extends LevelScene {
     public Level1(String levelname, int levelIndex) {
          super(levelname, levelIndex);
     }

     public Level1(String levelName, int levelIndex, Sprite backgroundImage) {
          super(levelName, levelIndex, backgroundImage);
     }
}
