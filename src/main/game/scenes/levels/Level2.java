package main.game.scenes.levels;

import main.game.scenes.LevelScene;
import main.models.entities.Laser;

public class Level2 extends LevelScene {
     public Level2() {
          super("Level 2", 2);
     }

     @Override
     public void loadObjects() {
          add(new Laser(100f, 100f));
     }
}
