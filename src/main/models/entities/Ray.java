package main.models.entities;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import main.math.Vector2;
import main.models.GameObject;

// Class for light ray
public class Ray extends GameObject {
     private Vector2 startPos;
     private Vector2 endPos;
     private Vector2 directionNormalized;
     private float magnitude;

     private Color rayColor = Color.GREEN;

     private static final int STROKE_WIDTH = 2;

     // Constructors
     public Ray(Vector2 start, Vector2 end) {
          setPoints(start, end);
     }

     public Ray(float x1, float y1, float x2, float y2) {
          setPoints(x1, y1, x2, y2);
     }

     public Ray(Vector2 start, Vector2 end, Color color) {
          if(color != null) {
               this.rayColor = color;
          }

          setPoints(start, end);
     }

     public Ray(Vector2 start, float angle, float magnitude) {
          setFromDirection(start, angle, magnitude);
     }

     // Getters
     public Vector2 getStart() {
          return startPos;
     }

     public Vector2 getEnd() {
          return endPos;
     }

     public Vector2 getNormalizedDirection() {
          return directionNormalized;
     }

     public float getRayLength() {
          return magnitude;
     }

     public Color getRayColor() {
          return rayColor;
     }

     // Setters
     public void setRayColor(Color color) {
          if(color == null) throw new IllegalArgumentException("Color can't be null");
          this.rayColor = color;
     }

     // Sets the origin and end-point of the ray
     void setPoints(Vector2 start, Vector2 end) {
          // Prevent start & end positions from being null or equal
          if(start == null || end == null) throw new IllegalArgumentException("Start & end can't be null");
          if(start == end) throw new IllegalArgumentException("Start & end positions can't be equal");
     
          this.startPos = start;
          this.endPos = end;

          setMagnitudeAndDirection();
     }

     void setPoints(float x1, float y1, float x2, float y2) {
          // Prevent intialization if the front and tail are equal
          if((x1 == x2) && (y1 == y2)) {
               throw new IllegalArgumentException("Start & end positions can't be equal");
          }

          this.startPos = new Vector2(x1, y1);
          this.endPos = new Vector2(x2, y2);

          setMagnitudeAndDirection();
     }

     // Sets the origin & endpoint based on angle & length
     void setFromDirection(Vector2 start, float angle, float magnitude) {
          // Creates a new endpoint

          // Offsets the angle to match the laser's default orientation by subtracting π/2
          float theta = (float) (angle - Math.PI/2);

          /* 
               <x, y> = <x' + rcosθ, y'+ rsinθ>
          */
          Vector2 end = new Vector2(
               start.getX() + (float) (magnitude * Math.cos(theta)),
               start.getY() + (float) (magnitude * Math.sin(theta))
          );

          setPoints(start, end);
     }

     private void setMagnitudeAndDirection() {
          Vector2 directionVector = endPos.subtract(startPos);

          // Sets the normalized vector of the direction of the ray
          this.directionNormalized = endPos.subtract(startPos).getNormalized();
          // Sets the magnitude
          this.magnitude = directionVector.getMagnitude();
          // Sets the default position to the start pos
          this.position = startPos;
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
               (int) startPos.getX(),
               (int) startPos.getY(),
               (int) endPos.getX(),
               (int) endPos.getY()
          );

          g2.dispose();
     }
}
