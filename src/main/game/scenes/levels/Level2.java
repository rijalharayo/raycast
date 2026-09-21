package main.game.scenes.levels;

import main.game.scenes.LevelScene;
import main.models.entities.Laser;
import main.models.environment.TargetEnergyOrb;
import main.models.environment.absorbers.BlackAbsorber;
import main.models.environment.reflectors.mirrors.PlaneMirror;

public class Level2 extends LevelScene {
     public Level2() {
          super("Level 2", 2);
     }

     @Override
     public void loadObjects() {
          add(new Laser(500, 300));

          // Mirror 1
          PlaneMirror mirror1 = new PlaneMirror(-634.5f, 305f, 200, 20, 45f);
          mirror1.setDraggable(false);
          mirror1.setRotatable(false);
          add(mirror1);

          // Mirror 2 (movable)
          add(new PlaneMirror(100, 100, 200, 20, -45f));

          BlackAbsorber absorber = new BlackAbsorber(-200f, -150f, 20, 600, 0f);
          absorber.setDraggable(false);
          absorber.setRotatable(false);
          add(absorber);

          add(new TargetEnergyOrb(-300, -300, 30f));
     }
}
