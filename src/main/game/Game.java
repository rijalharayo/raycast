package main.game;

import java.awt.Toolkit;

import main.game.scenes.MenuScene;
import main.input.KeyboardInput;
import main.input.MouseInput;
import main.math.algebra.Vector2;

// Class to handle game rendering and loop
public class Game implements Runnable {
     
     public static final int WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
     public static final int HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;

     // All positions & scaling are based on this
     public static final int WORLD_WIDTH = 1603;
     public static final int WORLD_HEIGHT = 902;

     public static final String TITLE = "Ray Shooter";

     public static final Vector2 WORLD_CENTER = new Vector2((float) WORLD_WIDTH / 2, (float) WORLD_HEIGHT / 2);

     private Window window;
     private boolean running;

     private static Game currentGame;

     public void start() {
          // Create the game window
          window = new Window(TITLE, WIDTH, HEIGHT);
          running = true;

          // Initialize all levels
          LevelManager.initializeLevels();
          // Initalize the menu's
          MenuScene.initailize();

          currentGame = this;

          // Run the game loop on a separate thread
          new Thread(this).start();
     }

     @Override
     public void run() {
          // Fixed update rate (100 FPS / UPS)
          final double FPS = 100.0;
          final double nsPerUpdate = 1_000_000_000.0 / FPS;

          long lastTime = System.nanoTime();
          double delta = 0;

          while (running) {
               long now = System.nanoTime();

               // Accumulate elapsed time
               delta += (now - lastTime) / nsPerUpdate;
               lastTime = now;

               // Catch up if we're behind
               while (delta >= 1) {
                    update();
                    render();

                    delta--;
               }
          }
     }

     // Stops the game
     public void stop() {
          running = false;

          if (window != null) {
               window.close();
          }

          System.exit(0);
     }

     private void update() {
          // Update the active scene
          Scene scene = SceneManager.getCurrentScene();

          if (scene != null)
               scene.update();

          MouseInput.reset();
          KeyboardInput.reset();
     }

     private void render() {
          // Render the active scene
          Scene scene = SceneManager.getCurrentScene();

          if (scene != null)
               window.render(scene);
     }

     // Returns the current game
     public static Game CURRENT_GAME() {
          return currentGame;
     }

     // Scale of the world based on screen size
     public static float getWorldScale() {
          float scaleX = (float) WIDTH / WORLD_WIDTH;
          float scaleY = (float) HEIGHT / WORLD_HEIGHT;

          return Math.min(scaleX, scaleY);
     }

     public static float getWorldOffsetX() {
          float scale = getWorldScale();

          return (WIDTH - WORLD_WIDTH * scale) / 2f;
     }

     public static float getWorldOffsetY() {
          float scale = getWorldScale();

          return (HEIGHT - WORLD_HEIGHT * scale) / 2f;
     }
}