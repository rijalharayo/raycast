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

     private int width;
     private int height;

     public Sprite(String path) {
          // Loads the sprite
          try {
               image = ImageIO.read(
                    new File(IMAGE_FOLDER + path)
               );

               this.width = image.getWidth();
               this.height = image.getHeight();
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

     public int getWidth() {
          return width;
     }

     public int getHeight() {
          return height;
     }

     // Draw / Render methods
     public void draw(Graphics2D g, float x, float y) {
          AffineTransform old = g.getTransform();

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

          // Rotates around the center of the sprite
          g.rotate(
               rotation,
               x,
               y
          );

          // Converts the center position to the top-left coordinate required for drawing
          int newX = (int) (x - width / 2);
          int newY = (int) (y - height / 2);

          g.drawImage(image, newX, newY, width, height, null);
          g.setTransform(old);
     }

     // Rotates the sprite (in radians)
     public void rotate(float angle) {
          this.rotation += angle;
     }

     // Sets the rotation (in radians)
     public void setRotation(float angle) {
          this.rotation = angle;
     }

     // Scales the sprite
     public void scale(float sc) {
          this.scale *= sc;
          // Scales the width & height
          this.width *= sc;
          this.height *= sc;
     }

     // Sets the scale
     public void setScale(float sc) {
          this.scale = sc;
          // Scales the width & height
          this.width *= scale;
          this.height *= scale;
          
     }

}
