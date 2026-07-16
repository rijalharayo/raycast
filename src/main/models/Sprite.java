package main.models;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Sprite {
     private BufferedImage image;
     private static final String IMAGE_FOLDER = "resources/images/";

     private float rotation = 0;
     private float scale = 1f;

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

     // Getters
     public BufferedImage getImage() {
          return image;
     }

     public float getRotation() {
          return rotation;
     }
     
     public float getScale() {
          return scale;
     }

     // Draw / Render methods
     public void draw(Graphics2D g, float x, float y) {
          AffineTransform old = g.getTransform();

          int width = (int) (image.getWidth() * scale);
          int height = (int) (image.getHeight() * scale);

          // Rotates around the center of ths sprite
          g.rotate(
               rotation,
               x,
               y
          );

          // Converts the center position to the top-left coordinate required for drawing
          int newX = (int) (x - width/2);
          int newY = (int) (y - height/2);

          g.drawImage(image, newX, newY, width, height, null);
          g.setTransform(old);
     }

     public void draw(Graphics2D g, float x, float y, int width, int height) {
          AffineTransform old = g.getTransform();

          int scaledWidth = (int) (width * scale);
          int scaledHeight = (int) (height * scale);

          // Rotates around the center of the sprite
          g.rotate(
               rotation,
               x,
               y
          );

          // Converts the center position to the top-left coordinate required for drawing
          int newX = (int) (x - scaledWidth/2);
          int newY = (int) (y - scaledHeight / 2.0);

          g.drawImage(image, newX, newY, scaledWidth, scaledHeight, null);
          g.setTransform(old);
     }

     // Rotates the sprite (in radians)
     public void rotate(float angle) {
          rotation += angle;
     }

     // Sets the rotation (in radians)
     public void setRotation(float angle) {
          rotation = angle;
     }

     // Scales the sprite
     public void scale(float sc) {
          scale *= sc;
     }

     // Sets the scale
     public void setScale(float sc) {
          scale = sc;
     }

}
