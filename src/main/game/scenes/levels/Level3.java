package main.game.scenes.levels;

import main.game.scenes.LevelScene;
import main.models.entities.Laser;
import main.models.environment.TargetEnergyOrb;
import main.models.environment.absorbers.BlackAbsorber;
import main.models.environment.reflectors.mirrors.CircularMirror;
import main.models.environment.reflectors.mirrors.CurvedMirror;
import main.models.environment.reflectors.mirrors.PlaneMirror;

public class Level3 extends LevelScene {
     public Level3() {
          super("Level 3", 3);
     }
     
     @Override
     public void loadObjects() {
          // Main laser
          add(new Laser(0, 0));

          // Absorbers
          add(new BlackAbsorber(-11.5f, -187f, 850, 20, 0f));
          add(new BlackAbsorber(-425.5f, 140f, 20, 650, 0f));
          add(new BlackAbsorber(403.00143f, 23.02714f, 20, 450, 0f));

          PlaneMirror immovableMirror = new PlaneMirror(530.8356f, 415.5591f, 250, 20, 0f);
          immovableMirror.setModifiable(false);
          add(immovableMirror);
          
          // Can be moved
          add(new CurvedMirror(508.09595f, -258.58383f, 170f, 100f, 35f, -45f));
          add(new CircularMirror(-313.5f, 348f, 80f));
          add(new PlaneMirror(-697.5f, -240f, 200, 20, 30f));

          add(new TargetEnergyOrb(-627.5f, 323f, 30f));
     }
}
