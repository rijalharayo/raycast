package main.game.scenes;

import java.awt.Graphics2D;

import main.game.Game;
import main.game.Scene;
import main.game.SceneManager;
import main.models.Sprite;
import main.ui.components.UIButton;

public abstract class LevelScene extends Scene {
     private String levelName;
     private int levelIndex;
     // Flag to check if the environment has been modified or not
     private boolean dirtyEnvironment = false;

     // Overloaded constructors
     public LevelScene(String levelName, int levelIndex) {
          this.levelName = levelName;
          this.levelIndex = levelIndex;
          this.background = Sprite.DEFAULT_BACKGROUND;

          loadObjects();
          loadUI();
     }

     public LevelScene(String levelName, int levelIndex, Sprite backgroundImage) {
          this.levelName = levelName;
          this.levelIndex = levelIndex;
          this.background = backgroundImage;

          loadObjects();
          loadUI();
     }

     // Getters
     public String getLevelName() {
          return levelName;
     }

     public int getLevelIndex() {
          return levelIndex;
     }

     public boolean isEnvironmentDirty() {
          return dirtyEnvironment;
     }

     // Setters
     public void setDirtyEnvironment(boolean isDirty) {
          this.dirtyEnvironment = isDirty;
     }

     public abstract void loadObjects();
     // Adding ui is optional for levels
     public void loadUI() {
          // A default back button
          UIButton backButton = new UIButton("Back", 150, 80);
          backButton.setTextSize(30f);
          backButton.setOnClick(() -> SceneManager.setScene(MenuScene.LEVEL_MENU()));
          backButton.setPosition(MenuScene.SCREEN_RIGHT() - 200f, MenuScene.SCREEN_TOP() - 80f);
          add(backButton);
     };

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