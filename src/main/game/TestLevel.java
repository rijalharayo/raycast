package main.game;

import main.models.Sprite;
import main.models.entities.Laser;

public class TestLevel extends LevelScene {

     public TestLevel(String levelName, int levelIndex, Sprite background) {
          super(levelName, levelIndex, background);
     }

     public TestLevel(String levelName, int levelIndex) {
          super(levelName, levelIndex);
     }

     @Override
     public void loadObjects() {
          add(new Laser(1000, 600));
     }
}
