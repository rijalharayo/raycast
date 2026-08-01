package main.graphics;

import java.awt.image.BufferedImage;

// Creates custom masks for images
public class ImageMasker {
     // Masks the image as an annular sector (ring sector)
     public static BufferedImage maskAnnularSector(BufferedImage image, float radius, float angle, float thickness) {
          int width = image.getWidth();
          int height = image.getHeight();

          // Circle / Arc center
          float cx = width / 2f;
          float cy = height / 2f;

          // Inner & outer radii
          float outerRadius = radius;
          float innerRadius = radius - thickness;

          // Start angle will always be 45° or π/4
          float startAngle = (float) Math.PI / 4;
          float endAngle = startAngle + (float) Math.toRadians(angle);
          
          // Loops through each pixel
          for(int y = 0; y < height; y++) {
               for(int x = 0; x < width; x++) {
                    // Calculates the pixel position relative to the center
                    float dx = x - cx;
                    float dy = x - cy;

                    // Converts to polar coordinates
                    float r = (float) Math.sqrt((dx * dx) + (dy * dy));
                    float theta = (float) Math.atan2(dy, dx);

                    // Keeps θ strictly between 0 & 360 deg
                    if(theta < 0){
                         theta += 2 * Math.PI;
                    }

                    // Checks if the pixel lies within the annular arc
                    boolean insideRadius = 
                              r >= innerRadius &&
                              r <= outerRadius;

                    boolean betweenAngles;

                    if(startAngle <= endAngle){
                         betweenAngles =
                                   theta >= startAngle &&
                                   theta <= endAngle;
                    }
                    else{
                         betweenAngles =
                                   theta >= startAngle ||
                                   theta <= endAngle;
                    }

                    boolean inside = insideRadius && betweenAngles;

                    // If its not inside, make pixel transparent
                    if(!inside) {
                         image.setRGB(x, y, 0x00000000);
                    }
               }
          }
          
          return image;
     }
}