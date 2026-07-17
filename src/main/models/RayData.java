package main.models;

import main.math.Vector2;

// Class to store the general data of a ray
public class RayData {
     private Vector2 startPos;
     private Vector2 endPos;
     private Vector2 directionNormalized;
     private float magnitude;

     // Constructors
     public RayData() {}

     public RayData(Vector2 start, Vector2 end) {
          setPoints(start, end);
     }

     public RayData(float x1, float y1, float x2, float y2) {
          setPoints(x1, y1, x2, y2);
     }

     public RayData(Vector2 start, float angle, float magnitude) {
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

     // Setters
     // Sets the origin and end-point of the ray
     public void setPoints(Vector2 start, Vector2 end) {
          // Prevent start & end positions from being null or equal
          if(start == null || end == null) throw new IllegalArgumentException("Start & end can't be null");
          if(start == end) throw new IllegalArgumentException("Start & end positions can't be equal");
     
          this.startPos = start;
          this.endPos = end;

          setMagnitudeAndDirection();
     }

     public void setPoints(float x1, float y1, float x2, float y2) {
          // Prevent intialization if the front and tail are equal
          if((x1 == x2) && (y1 == y2)) {
               throw new IllegalArgumentException("Start & end positions can't be equal");
          }

          this.startPos = new Vector2(x1, y1);
          this.endPos = new Vector2(x2, y2);

          setMagnitudeAndDirection();
     }

     // Sets the origin & endpoint based on angle & length
     public void setFromDirection(Vector2 start, float angle, float magnitude) {
          // Creates a new endpoint

          /* 
               <x, y> = <x' + rcosθ, y'+ rsinθ>
          */
          Vector2 end = new Vector2(
               start.getX() + (float) (magnitude * Math.cos(angle)),
               start.getY() + (float) (magnitude * Math.sin(angle))
          );

          setPoints(start, end);
     }

     // Sets the origin & endpoint based on angle
     public void setFromDirection(Vector2 start, float angle) {
          // Creates a new endpoint

          // Offsets the angle to match the laser's default orientation by subtracting π/2
          float theta = (float) (angle - Math.PI/2);

          /* 
               <x, y> = <x' + rcosθ, y'+ rsinθ>
          */
          Vector2 end = new Vector2(
               start.getX() + (float) (1f * Math.cos(theta)),
               start.getY() + (float) (1f * Math.sin(theta))
          );

          setPoints(start, end);
     }

     private void setMagnitudeAndDirection() {
          Vector2 directionVector = endPos.subtract(startPos);

          // Sets the normalized vector of the direction of the ray
          this.directionNormalized = endPos.subtract(startPos).getNormalized();
          // Sets the magnitude
          this.magnitude = directionVector.getMagnitude();
     }

     @Override
     public String toString() {
          return "Start: " + startPos + "\nEnd: " + endPos;
     }
}