package main.game.scenes;

import main.game.LevelScene;
import main.models.Sprite;
import main.models.entities.Laser;
import main.models.environment.reflectors.mirrors.PlaneMirror;

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
          add(new PlaneMirror(300, 20, 150, 30, 2f));
          add(new PlaneMirror(100, 300, 150, 20, -1f));
          add(new PlaneMirror(-100, -200, 150, 20, 2f));
     }
}
