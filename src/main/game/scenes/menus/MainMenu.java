package main.game.scenes.menus;

import java.awt.Color;

import main.game.Game;
import main.game.scenes.MenuScene;
import main.ui.components.UIText;

// The main menu of the game
public class MainMenu extends MenuScene {

     public MainMenu() {
          super("Main menu");
     }

     @Override
     public void loadUI() {
          UIText title = new UIText("RAYCAST", 100f);
          title.setPosition(0, (Game.HEIGHT/ 2) - 200);
          title.setColor(Color.WHITE);

          add(title);
     }
}
