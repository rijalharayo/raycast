package main.game.scenes;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import main.game.Game;
import main.game.Scene;
import main.models.Sprite;
import main.ui.UIComponent;
import main.ui.UILayout;

// Represents general ui menu scenes
public abstract class MenuScene extends Scene {
     private String menuName;
     private final List<UILayout> layouts = new ArrayList<>();

     // Help in positioning components relative to screen borders

     public static final float SCREEN_LEFT = -Game.WIDTH / 2f;
     public static final float SCREEN_RIGHT = Game.WIDTH / 2f;

     public static final float SCREEN_TOP = Game.HEIGHT / 2f;
     public static final float SCREEN_BOTTOM = -Game.HEIGHT / 2f;

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

     // Adds an layout
     public void addLayout(UILayout layout) {
          layouts.add(layout);
     }

     // Getters
     public String getMenuName() {
          return menuName;
     }

     public abstract void loadUI();

     @Override
     public void update() {
          for(UIComponent uiComponent : uiComponents) {
               uiComponent.update();
          }

          // Update components inside layouts
          for(UILayout layout : layouts) {
               layout.update();
          }
     }

     @Override
     public void render(Graphics2D g) {
          // Renders the background
          if (background != null) {
               background.draw(g, 0, 0, Game.WIDTH, Game.HEIGHT);
          }

          for (UILayout layout : layouts) {
               layout.render(g);
          }

          super.render(g);
     }

     @Override
     public String toString() {
          return menuName;
     }
}