import game.Game;
import game.SceneManager;
import game.scenes.TestScene;

// Main app
public class App {
    public static void main(String[] args) {
        // Initializes the default scene
        SceneManager.setScene(new TestScene());

        // Runs the game
        new Game().start();;
    }
}
