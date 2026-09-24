package main.game.scenes.levels;

import main.game.scenes.LevelScene;
import main.models.entities.Laser;
import main.models.environment.TargetEnergyOrb;
import main.models.environment.absorbers.BlackAbsorber;
import main.models.environment.reflectors.mirrors.PlaneMirror;
import main.models.environment.refractors.fluids.WaterTank;
import main.models.environment.refractors.glasses.GlassBlock;
import main.models.environment.refractors.glasses.lens.ConcaveLens;
import main.models.environment.refractors.glasses.lens.ConvexLens;

public class Level5 extends LevelScene {
     public Level5() {
          super("Level 5", 5);
     }

     @Override
     public void loadObjects() {
          add(new Laser(679.5f, 64f));

          // Refractors
          add(new WaterTank(247.45557f, 238.28352f, 230, 50, 0f));
          add(new GlassBlock(-71.84265f, 323.33344f, 200, 70, 0f));
          add(new ConcaveLens(-222.95778f, -355.39508f, 130f, 50f, 150f, 0f));
          add(new ConvexLens(749.42944f, -301.82065f, 180f, 65f, 0f));

          // Reflectors
          PlaneMirror mirror1 = new PlaneMirror(69.21128f, -396.97006f, 230, 20, 0f);
          mirror1.setModifiable(false);
          add(mirror1);

          PlaneMirror mirror2 = new PlaneMirror(-60.70075f, 160.03911f, 220, 15, -21.85f);
          mirror2.setDraggable(false); // Can only be rotated
          add(mirror2);

          PlaneMirror mirror3 = new PlaneMirror(-270.79367f, -29.564718f, 170, 15, 0f);
          mirror3.setModifiable(false);
          add(mirror3);

          PlaneMirror mirror4 = new PlaneMirror(-642.793f, 351.43506f, 240, 15, 0f);
          mirror4.setModifiable(false);
          add(mirror4);

          // Absorbers
          add(new BlackAbsorber(387.02115f, 250.97481f, 20, 400, 0));
          add(new BlackAbsorber(387.02115f, -285.7095f, 20, 350, 0));
          add(new BlackAbsorber(98.00002f, 187.99995f, 20, 530, 0));
          add(new BlackAbsorber(13.402359f, -66.03425f, 150, 20, 0));
          add(new BlackAbsorber(-491.21042f, -214.99849f, 230, 20, 0));
          add(new BlackAbsorber(-269.21033f, -63.998447f, 250, 20, 0));
          add(new BlackAbsorber(-385.02103f, -271.57883f, 20, 400, 0));

          // Main target
          add(new TargetEnergyOrb(-457.5f, -321f, 40f));
     }
}
