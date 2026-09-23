package main.game.scenes.levels;

import main.game.scenes.LevelScene;
import main.models.entities.Laser;
import main.models.environment.TargetEnergyOrb;
import main.models.environment.absorbers.BlackAbsorber;
import main.models.environment.reflectors.mirrors.PlaneMirror;
import main.models.environment.refractors.glasses.Prism;
import main.models.environment.refractors.glasses.lens.ConvexLens;

public class Level4 extends LevelScene {
     public Level4() {
          super("Level 4", 4);
     }

     @Override
     public void loadObjects() {
          // Main laser
          add(new Laser(600.5f, 244f));

          // Reflectors
          PlaneMirror mirror1 = new PlaneMirror(-375.2652f, 365.5745f, 200, 15, 70.3f);
          mirror1.setModifiable(false);
          add(mirror1);

          PlaneMirror mirror2 = new PlaneMirror(150.30951f, -409.69873f, 200, 15, 0f);
          mirror2.setModifiable(false);
          add(mirror2);

          PlaneMirror mirror3 = new PlaneMirror(304.9454f, 231.00458f, 200, 15, 90f);
          mirror3.setModifiable(false);
          add(mirror3);

          // Refractors
          add(new ConvexLens(591.9694f, -303.3947f, 170f, 70f, 0f));
          add(new Prism(-612.7462f, -282.12222f, 200f, 200f, -90f));

          // Absorbers
          add(new BlackAbsorber(331.1407f, 244.98329f, 20, 450, 0f));
          add(new BlackAbsorber(-425.86456f, 244.98329f, 20, 450, 0f));

          // Target
          add(new TargetEnergyOrb(-612.5f, 354f, 30f));
     }
}
