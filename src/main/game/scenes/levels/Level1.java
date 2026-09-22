package main.game.scenes.levels;

import main.game.scenes.LevelScene;
import main.models.entities.Laser;
import main.models.environment.TargetEnergyOrb;
import main.models.environment.absorbers.BlackAbsorber;
import main.models.environment.reflectors.mirrors.PlaneMirror;

public class Level1 extends LevelScene {
     public Level1() {
          super(
               "Level 1",
               1
          );
     }

     @Override
     public void loadObjects() {
          add(new Laser(100, -300));

          // Seperates laser and orb
          add(new BlackAbsorber(0, -150f, 20, 550, 0f));

          // Can be used to reflect across
          add(new PlaneMirror(0, 300f, 300, 20, 0f));

          add(new TargetEnergyOrb(-150f, -300f, 30f));
     }
}
