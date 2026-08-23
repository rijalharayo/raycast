package main.game;

import java.awt.Toolkit;

import main.input.KeyboardInput;
import main.input.MouseInput;
import main.math.algebra.Vector2;

// Class to handle game rendering and loop
public class Game implements Runnable {
     
     public static final int WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
     public static final int HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;
     public static final String TITLE = "Ray Shooter";

     public static final Vector2 WORLD_CENTER = new Vector2((float) WIDTH / 2, (float) HEIGHT / 2);

     private Window window;
     private boolean running;

     public void start() {

          // Create the game window
          window = new Window(TITLE, WIDTH, HEIGHT);
          running = true;

          // Run the game loop on a separate thread
          new Thread(this).start();
     }

     @Override
     public void run() {
          // Fixed update rate (60 FPS / UPS)
          final double FPS = 60.0;
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
}