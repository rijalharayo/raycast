package main.game.scenes;

import java.awt.Graphics2D;

import main.game.Game;
import main.game.Scene;
import main.models.Sprite;

public abstract class LevelScene extends Scene {
     private String levelName;
     private int levelIndex;

     // Overloaded constructors
     public LevelScene(String levelName, int levelIndex) {
          this.levelName = levelName;
          this.levelIndex = levelIndex;
          this.background = Sprite.DEFAULT_BACKGROUND;

          loadObjects();
     }

     public LevelScene(String levelName, int levelIndex, Sprite backgroundImage) {
          this.levelName = levelName;
          this.levelIndex = levelIndex;
          this.background = backgroundImage;

          loadObjects();
     }

     // Getters
     public String getLevelName() {
          return levelName;
     }

     public int getLevelIndex() {
          return levelIndex;
     }

     public abstract void loadObjects();

     @Override
     public void render(Graphics2D g) {
          // Renders the background
          if(background != null) {
               background.draw(g, 0, 0, Game.WIDTH, Game.HEIGHT);
          }

          super.render(g);
     }

     @Override
     public String toString() {
          return levelName + "(" + levelIndex + ")";
     }
}