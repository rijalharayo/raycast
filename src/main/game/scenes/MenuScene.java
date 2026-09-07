package main.game.scenes;

import java.awt.Graphics2D;

import main.game.Game;
import main.game.Scene;
import main.models.Sprite;

// Represents general ui menu scenes
public abstract class MenuScene extends Scene {
     private String menuName;

     // Overloaded constructors
     public MenuScene(String menuName) {
          this.menuName = menuName;
          this.background = new Sprite("background1.png");
          loadUI();
     }

     public MenuScene(String menuName, Sprite backgroundImage) {
          this.menuName = menuName;
          this.background = backgroundImage;
          loadUI();
     }

     // Getters
     public String getMenuName() {
          return menuName;
     }

     public abstract void loadUI();

     @Override
     public void render(Graphics2D g) {
          // Renders the background
          if (background != null) {
               background.draw(g, 0, 0, Game.WIDTH, Game.HEIGHT);
          }

          super.render(g);
     }

     @Override
     public String toString() {
          return menuName;
     }
}