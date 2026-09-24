package main.game.scenes.levels;

import main.game.scenes.LevelScene;
import main.models.entities.Laser;
import main.models.environment.TargetEnergyOrb;
import main.models.environment.absorbers.BlackAbsorber;
import main.models.environment.reflectors.mirrors.PlaneMirror;
import main.models.environment.refractors.glasses.OpticalFibre;
import main.models.environment.refractors.glasses.lens.ConvexLens;
import main.models.environment.supernatural.Portal;

public class Level7 extends LevelScene {
     public Level7() {
          super("Level 7", 7);
     }
     
     @Override
     public void loadObjects() {
          add(new Laser(0f, 80f));

          // Reflectors
          PlaneMirror mirror1 = new PlaneMirror(495.5f, 297f, 208, 20, -45f);
          mirror1.setModifiable(false);
          add(mirror1);

          PlaneMirror mirror2 = new PlaneMirror(695.5f, 61.5f, 181, 20, 90f);
          mirror2.setDraggable(false); // Can be rotated
          add(mirror2);

          PlaneMirror mirror3 = new PlaneMirror(251.5f, 112.5f, 148, 20, -122.62f);
          mirror3.setDraggable(false);
          add(mirror3);

          // Refractors
          add(new ConvexLens(-480.5f, 154f, 100f, 30f, 0f));

          OpticalFibre fibre1 = new OpticalFibre(-625.5f, 100f, 338, 45, 90f);
          fibre1.setModifiable(false);
          add(fibre1);

          OpticalFibre fibre2 = new OpticalFibre(-220f, -303f, 985, 50, 0f);
          fibre2.setModifiable(false);
          add(fibre2);

          // Portals

          // 1st pair
          Portal portalA = new Portal(-299f, -40.5f, 206, 20, -45.40f);
          Portal portalB = new Portal(-339.5f, 338f, 206, 20, 90f);

          portalA.setModifiable(false);
          portalB.setModifiable(false);

          portalA.setLinkedPortal(portalB);
          add(portalA);
          add(portalB);

          // 2nd pair
          Portal portalC = new Portal(392.5f, 80f, 182, 20, -90f);
          Portal portalD = new Portal(518.5f, -100f, 182, 20, 0f);

          portalC.setModifiable(false);
          portalD.setModifiable(false);

          portalC.setLinkedPortal(portalD);
          add(portalC);
          add(portalD);

          // 3rd pair
          Portal portalE = new Portal(307.5f, 414f, 207, 20, 0f);
          Portal portalF = new Portal(-617.5f, 356f, 207, 20, -180f);

          portalE.setModifiable(false);
          portalF.setModifiable(false);

          portalE.setLinkedPortal(portalF);
          add(portalF);
          add(portalE);

          // 4th pair
          Portal portalG = new Portal(-621.5f, -110f, 200, 20, 0f);
          Portal portalH = new Portal(-762.5f, -309f, 200, 20, 90f);

          portalG.setModifiable(false);
          portalH.setModifiable(false);

          portalH.setLinkedPortal(portalG);
          add(portalH);
          add(portalG);

          // 5th pair
          Portal portalI = new Portal(530f, -346.5f, 256, 20, -36.10f);
          Portal portalJ = new Portal(319.5f, -305f, 256, 20, -90f);

          portalJ.setModifiable(false);
          portalI.setDraggable(false); // Can be rotated tho

          portalI.setLinkedPortal(portalJ);
          add(portalI);
          add(portalJ);

          // Absorbers
          add(new BlackAbsorber(-0.5f, -160f, 1602, 20, 0f));
          add(new BlackAbsorber(180.5f, 37f, 20, 396, 0f));
          add(new BlackAbsorber(-399.5f, 147.5f, 20, 607, 0f));
          add(new BlackAbsorber(376.5f, -309f, 20, 282, 0f));
          add(new BlackAbsorber(21f, 201f, 835, 20, 0f));
          add(new BlackAbsorber(173.5f, 422.5f, 20, 51, 0f));
     
          // Main target
          add(new TargetEnergyOrb(721.5f, -233f, 30f));
     }
}
