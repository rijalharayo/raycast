package main.game.scenes;

import main.game.LevelScene;
import main.models.Sprite;
import main.models.entities.Laser;
import main.models.environment.reflectors.mirrors.CircularMirror;
import main.models.environment.reflectors.mirrors.CurvedMirror;
import main.models.environment.reflectors.mirrors.PlaneMirror;
import main.models.environment.refractors.fluids.WaterTank;
import main.models.environment.refractors.glasses.GlassBlock;
import main.models.environment.refractors.glasses.GlassDisc;
import main.models.environment.refractors.glasses.OpticalFibre;
import main.models.environment.refractors.glasses.Prism;

public class TestLevel extends LevelScene {

     public TestLevel(String levelName, int levelIndex, Sprite background) {
          super(levelName, levelIndex, background);
     }

     public TestLevel(String levelName, int levelIndex) {
          super(levelName, levelIndex);
     }

     @Override
     public void loadObjects() {
          add(new Laser(-100, -50));
          
          add(new PlaneMirror(400, 20, 200, 20, 40f));
          add(new CircularMirror(-400, 20, 100f));
          add(new CurvedMirror(400, -200, 150f, 120f, 50f, -60f));

          add(new WaterTank(100, 100, 200, 100, 0f));
          add(new OpticalFibre(400, 200, 500, 100, 0f));
          add(new Prism(100, 300, 200, 200, 0f));
          add(new GlassDisc(500, -300, 100f));
     }
}