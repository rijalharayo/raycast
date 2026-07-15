package main.models;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Sprite {
     private BufferedImage image;
     private static final String IMAGE_FOLDER = "resources/images/";

     public Sprite(String path) {
          // Loads the sprite
          try {
               image = ImageIO.read(
                    new File(IMAGE_FOLDER + path)
               );
          }
          catch(IOException e) {
               throw new RuntimeException("Couldn't load sprite: " + path, e);
          } 
     }

     // Getter
     public BufferedImage getImage() {
          return image;
     }

     // Draw / Render methods
     public void draw(Graphics2D g, float x, float y) {
          g.drawImage(image, (int) x, (int) y, null);
     }

     public void draw(Graphics2D g, float x, float y, int width, int height) {
          g.drawImage(image, (int) x, (int) y, width, height, null);
     }
}
