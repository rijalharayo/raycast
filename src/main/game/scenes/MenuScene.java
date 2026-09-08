package main.game.scenes;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import main.game.Game;
import main.game.Scene;
import main.game.scenes.menus.LevelSelectionMenu;
import main.game.scenes.menus.MainMenu;
import main.models.Sprite;
import main.ui.UIComponent;
import main.ui.UILayout;

// Represents general ui menu scenes
public abstract class MenuScene extends Scene {
     // Two default menu's
     private static MainMenu MAIN_MENU;
     private static LevelSelectionMenu LEVEL_MENU;

     private String menuName;
     private final List<UILayout> layouts = new ArrayList<>();

     // Overloaded constructors
     public MenuScene(String menuName) {
          this.menuName = menuName;
          this.background = Sprite.DEFAULT_BACKGROUND;
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

     public static void initailize() {
          MenuScene.LEVEL_MENU = new LevelSelectionMenu();
          MenuScene.MAIN_MENU = new MainMenu();
     }

     @Override
     public String toString() {
          return menuName;
     }

     // Help in positioning components relative to screen borders

     public static float SCREEN_LEFT() {
          return -Game.WIDTH / 2f;
     }

     public static float SCREEN_RIGHT() {
          return Game.WIDTH / 2f;
     }

     public static float SCREEN_TOP() {
          return Game.HEIGHT / 2f;
     }

     public static float SCREEN_BOTTOM() {
          return -Game.HEIGHT / 2f;
     }

     public static MainMenu MAIN_MENU() {
          return MAIN_MENU;
     }

     public static LevelSelectionMenu LEVEL_MENU() {
          return LEVEL_MENU;
     }
}