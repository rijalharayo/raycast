package main.game;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

public class Window {
     private final JFrame frame;
     private final Canvas canvas;

     public Window(String title, int width, int height) {
          frame = new JFrame(title);
          canvas = new Canvas();

          frame.setUndecorated(true);
          frame.setResizable(false);

          frame.add(canvas);

          GraphicsDevice device = GraphicsEnvironment
               .getLocalGraphicsEnvironment()
               .getDefaultScreenDevice();

          device.setFullScreenWindow(frame);

          // Create a triple buffer for smooth rendering
          canvas.createBufferStrategy(3);
     }

     public void render(Scene scene) {
          // Get the current drawing buffer
          BufferStrategy bs = canvas.getBufferStrategy();
          Graphics2D g = (Graphics2D) bs.getDrawGraphics();

          // Clear the screen
          g.setColor(Color.BLACK);
          g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

          // Render the active scene
          scene.render(g);

          // Release graphics resources and display the frame
          g.dispose();
          bs.show();

          Toolkit.getDefaultToolkit().sync();
     }
}