package main.game.scenes.levels;

import main.game.scenes.LevelScene;
import main.models.entities.Laser;

public class Level1 extends LevelScene {
     public Level1() {
          super(
               "Level 1",
               1
          );
     }

     @Override
     public void loadObjects() {
          add(new Laser(0, 0));
     }
}
