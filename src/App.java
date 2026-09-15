import main.game.Game;
import main.game.SceneManager;
import main.game.scenes.levels.Level1;
// import main.game.scenes.menus.MainMenu;

// Main app
public class App {
    public static void main(String[] args) {
        // Initializes the default scene
        SceneManager.setScene(new Level1());

        // Runs the game
        new Game().start();;
    }
}
