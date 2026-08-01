package main.game.scenes;

import main.game.LevelScene;
import main.models.Sprite;
import main.models.entities.Laser;
import main.models.environment.reflectors.mirrors.CircularMirror;
import main.models.environment.reflectors.mirrors.CurvedMirror;
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
          
          add(new PlaneMirror(400, 20, 200, 20, 90f));
          add(new CircularMirror(-400, 20, 100f));
          add(new CurvedMirror(400, -200, 150f, 120f, 20f));
     }
}
