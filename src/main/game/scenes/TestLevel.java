package main.game.scenes;

import main.game.LevelScene;
import main.models.Sprite;
import main.models.entities.Laser;
import main.models.environment.reflectors.mirrors.CircularMirror;

public class TestLevel extends LevelScene {

     public TestLevel(String levelName, int levelIndex, Sprite background) {
          super(levelName, levelIndex, background);
     }

     public TestLevel(String levelName, int levelIndex) {
          super(levelName, levelIndex);
     }

     @Override
     public void loadObjects() {
          add(new Laser(-100, -50));
          

          add(new CircularMirror(400, 20, 100f));
     }
}
