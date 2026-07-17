package main.models.entities;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import main.math.Vector2;
import main.models.GameObject;
import main.models.RayData;
import main.physics.rays.Ray;

// Class for the light ray
public class LightRay extends GameObject implements Ray {
     private Color rayColor = Color.GREEN;
     private static final int STROKE_WIDTH = 2;

     private RayData rayData;

     // Constructors
     public LightRay(Vector2 start, Vector2 end) {
          super("", start);
          rayData = new RayData(start, end);
     }

     public LightRay(float x1, float y1, float x2, float y2) {
          super("", new Vector2(x1, y1));
          rayData = new RayData(x1, y1, x2, y2);
     }

     public LightRay(Vector2 start, Vector2 end, Color color) {
          super("", start);
          rayData = new RayData(start, end);

          if(color != null) {
               this.rayColor = color;
          }

     }

     public LightRay(Vector2 start, float angle, float magnitude) {
          super("", start);
          rayData = new RayData(start, angle, magnitude);
     }

     public LightRay(RayData data) {
          super("", data.getStart());
          this.rayData = data;
     }

     // Getters
     public Color getRayColor() {
          return rayColor;
     }

     // Setters
     public void setRayColor(Color color) {
          if(color == null) throw new IllegalArgumentException("Color can't be null");
          this.rayColor = color;
     }

     @Override
     public void update() {
          // Code
     }

     @Override
     public void render(Graphics2D g) {
          // Create a 2nd graphics variable
          Graphics2D g2 = (Graphics2D) g.create();
          // Sets stroke width & color
          g2.setColor(rayColor);
          g2.setStroke(new BasicStroke(STROKE_WIDTH));
          
          g2.drawLine(
               (int) getStart().getX(),
               (int) getStart().getY(),
               (int) getEnd().getX(),
               (int) getEnd().getY()
          );

          g2.dispose();
     }

     @Override
     public RayData getRayData() {
          return rayData;
     }
}
