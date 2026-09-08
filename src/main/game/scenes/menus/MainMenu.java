package main.game.scenes.menus;

import java.awt.Color;

import main.game.Game;
import main.game.scenes.MenuScene;
import main.ui.components.UIButton;
import main.ui.components.UIText;

// The main menu of the game
public class MainMenu extends MenuScene {

     public MainMenu() {
          super("Main menu");
     }

     @Override
     public void loadUI() {
          float topY = Game.HEIGHT / 2;

          UIText title = new UIText("RAYCAST", 100f);
          title.setPosition(0, topY - 120);
          title.setColor(Color.WHITE);
          add(title);

          UIButton playBtn = new UIButton("Play", 250, 100);
          playBtn.setPosition(0, topY - 300);
          playBtn.setTextSize(40f);
          add(playBtn);
     }
}
