package main.game.scenes.levels;

import main.game.scenes.LevelScene;
import main.models.entities.Laser;
import main.models.environment.TargetEnergyOrb;
import main.models.environment.absorbers.BlackAbsorber;
import main.models.environment.reflectors.mirrors.CurvedMirror;
import main.models.environment.reflectors.mirrors.PlaneMirror;
import main.models.environment.refractors.glasses.lens.ConcaveLens;
import main.models.environment.refractors.glasses.lens.ConvexLens;
import main.models.environment.supernatural.Portal;

public class Level6 extends LevelScene {
     public Level6() {
          super("Level 6", 6);
     }

     @Override
     public void loadObjects() {
          add(new Laser(-725.5f, 275f));

          // Reflectors
          PlaneMirror mirror1 = new PlaneMirror(207.5f, 388f, 180, 20, 20f);
          mirror1.setDraggable(false); // Can only be rotated
          add(mirror1);

          add(new CurvedMirror(-105.5f, 36f, 150f, 120f, 40f));

          // Refractors
          add(new ConcaveLens(222.5f, -325f, 130f, 85f, 150f, 20f));
          add(new ConvexLens(336.5f, 135f, 180f, 65f, 0f));

          // Portals

          // 1st pair
          Portal portalA = new Portal(723.5f, 140f, 200, 20, 90f);
          Portal portalB = new Portal(-473.5f, 39f, 200, 20, -45f);

          portalA.setModifiable(false);
          portalB.setModifiable(false);

          portalA.setLinkedPortal(portalB);
          add(portalA);
          add(portalB);

          // 2nd pair
          Portal portalC = new Portal(-179.5f, -191f, 123, 20, -52.96f);
          Portal portalD = new Portal(606.5f, -11f, 123, 20, 180f);

          portalC.setModifiable(false);
          portalD.setModifiable(false);

          portalC.setLinkedPortal(portalD);
          add(portalC);
          add(portalD);

          // Absorbers
          add(new BlackAbsorber(-601.9309f, 354.00122f, 400, 20, 0f));
          add(new BlackAbsorber(-601.9309f, 190.26018f, 400, 20, 0f));
          add(new BlackAbsorber(604.218f, 236.91214f, 420, 20, 0f));
          add(new BlackAbsorber(604.218f, 36.91214f, 420, 20, 0f));
          add(new BlackAbsorber(405.56918f, -216.91795f, 20, 500, 0f));
          add(new BlackAbsorber(47.5f, -107.5f, 20, 700, 0f));
          add(new BlackAbsorber(-180.5f, -128.5f, 782, 20, -54.34f));
          add(new BlackAbsorber(-361f, -112f, 323, 20, 0f));
          add(new BlackAbsorber(-450.5f, -183f, 20, 126, 0f));

          // Main target
          add(new TargetEnergyOrb(604.5f, -319f, 45f));
     }
}
