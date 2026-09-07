import main.game.Game;
import main.game.SceneManager;
// import main.game.scenes.levels.TestLevel;
import main.game.scenes.menus.MainMenu;

// Main app
public class App {
    public static void main(String[] args) {
        // Initializes the default scene
        SceneManager.setScene(new MainMenu());

        // Runs the game
        new Game().start();;
    }
}
