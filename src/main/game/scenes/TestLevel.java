package main.game.scenes;

import main.game.LevelScene;
import main.models.Sprite;
import main.models.entities.Laser;
import main.models.environment.refractors.fluids.WaterTank;
import main.models.environment.refractors.glasses.GlassBlock;
import main.models.environment.refractors.glasses.GlassDisc;
import main.models.environment.refractors.glasses.OpticalFibre;
import main.models.environment.refractors.glasses.Prism;
import main.models.environment.refractors.glasses.lens.ConvexLens;

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

          add(new WaterTank(100, 100, 200, 100, 0f));
          add(new GlassBlock(400, 200, 200, 100, 0f));
          add(new Prism(100, 300, 200, 200, 0f));
          add(new GlassDisc(500, -300, 100f));
          add(new OpticalFibre(-400, 200, 500, 100, 0f));

          add(new ConvexLens(0f, 0f, 200f, 90f, 0f));
     }
}