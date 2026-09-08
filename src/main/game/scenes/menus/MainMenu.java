package main.game.scenes.menus;

import java.awt.Color;

import main.game.Game;
import main.game.Scene;
import main.game.SceneManager;
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
          // Controls positions relative to top
          float topY = Game.HEIGHT / 2;

          UIText title = new UIText("RAYCAST", 100f);
          title.setPosition(0, topY - 120);
          title.setColor(Color.WHITE);
          add(title);

          UIButton playBtn = new UIButton("Play", 250, 100);
          playBtn.setPosition(0, topY - 300);
          playBtn.setTextSize(40f);
          // Goes to level selector
          playBtn.setOnClick(() -> SceneManager.setScene(Scene.LEVEL_MENU));
          add(playBtn);

          UIButton quitBtn = new UIButton("Quit", 250, 100);
          quitBtn.setPosition(0, topY - 430);
          quitBtn.setTextSize(40f);
          add(quitBtn);
     }
}
