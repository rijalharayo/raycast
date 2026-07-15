import main.game.Game;
import main.game.SceneManager;
import main.game.scenes.Level1;

// Main app
public class App {
    public static void main(String[] args) {
        // Initializes the default scene
        SceneManager.setScene(new Level1(
            "Level 1",
            0
        ));

        // Runs the game
        new Game().start();;
    }
}
