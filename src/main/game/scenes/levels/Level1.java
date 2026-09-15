package main.game.scenes.levels;

import main.game.scenes.LevelScene;
import main.models.entities.Laser;
import main.models.environment.supernatural.Portal;

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

          Portal portal1 = new Portal(100, 100, 200, 20, 0f);
          Portal portal2 = new Portal(200, 300, 200, 20, 90f);
          portal1.setLinkedPortal(portal2);

          add(portal1);
          add(portal2);
     }
}
