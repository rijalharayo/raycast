package main.game.scenes.menus;

import main.game.LevelManager;
import main.game.SceneManager;
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
          title.setPosition(-387.5f, 333f);
          add(title);

          // Go back
          UIButton backButton = new UIButton("Back", 200, 100);
          backButton.setTextSize(40f);
          backButton.setPosition(643.5f, 333f);
          backButton.setOnClick(() -> SceneManager.setScene(MenuScene.MAIN_MENU()));
          add(backButton);

          UIWrapLayout levelLayout = new UIWrapLayout(1200f, 20f);
          levelLayout.setPosition(0, 240f);

          for (int levelIndex : LevelManager.getAllLevelIndices()) {
               UIButton button = new UIButton(
                    String.valueOf(levelIndex),
                    150,
                    100
               );

               button.setOnClick(() -> {
                    LevelManager.setCurrentLevel(levelIndex);
               });

               levelLayout.addUIComponent(button);
          }

          levelLayout.layout();
          addLayout(levelLayout);
     }
}
