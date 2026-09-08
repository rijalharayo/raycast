package main.game.scenes.menus;

import main.game.LevelManager;
import main.game.Scene;
import main.game.SceneManager;
import main.game.scenes.LevelScene;
import main.game.scenes.MenuScene;
import main.ui.components.UIButton;
import main.ui.components.UIText;
import main.ui.layouts.UIWrapLayout;

// Main level selector
public class LevelSelectionMenu extends MenuScene {
     public LevelSelectionMenu() {
          super("Level selector");
     }

     @Override
     public void loadUI() {
          UIText title = new UIText("Level selector");
          title.setSize(80f);
          title.setPosition(SCREEN_LEFT + 400, SCREEN_TOP - 100);
          add(title);

          // Go back
          UIButton backButton = new UIButton("Back", 200, 100);
          backButton.setTextSize(40f);
          backButton.setPosition(SCREEN_RIGHT - 200, SCREEN_TOP - 100);
          backButton.setOnClick(() -> SceneManager.setScene(Scene.MAIN_MENU));
          add(backButton);

          UIWrapLayout levelLayout = new UIWrapLayout(900f, 20f);
          levelLayout.setPosition(0, SCREEN_TOP - 200);

          for (LevelScene level : LevelManager.getAllLevels()) {
               UIButton button = new UIButton(
                    String.valueOf(level.getLevelIndex()),
                    150,
                    100
               );

               button.setOnClick(() -> {
                    LevelManager.setCurrentLevel(level.getLevelIndex());
               });

               levelLayout.addUIComponent(button);
          }

          levelLayout.layout();
          addLayout(levelLayout);
     }
}
