package main.math;

// Class representing vector quantites (positions, velocity etc..)
public class Vector2 {
     private float x;
     private float y;

     public static final Vector2 ZERO = new Vector2(0, 0);

     // Standard unit vectors (Up & Down are exchanged in this co-ordinate system)
     public static final Vector2 UP = new Vector2(0, -1);
     public static final Vector2 DOWN = new Vector2(0, 1);
     public static final Vector2 RIGHT = new Vector2(1, 0);
     public static final Vector2 LEFT = new Vector2(-1, 0);

     public Vector2(float x, float y) {
          this.x = x;
          this.y = y;
     }

     // Main getters
     public float getX() {
          return x;
     }

     public float getY() {
          return y;
     }

     // Returns the dot product between vectors 
     public float dot(Vector2 v2) {
          return x * v2.getX() + y * v2.getY();
     }

     // Returns the angle of the vector
     public float getAngle() {
          return (float) Math.atan2(y, x);
     }

     // Returns the magnitude of a vector
     public float getMagnitude() {
          float h = x * x + y * y;
          return (float) Math.sqrt(h);
     }
     
     // Adds the vector
     public Vector2 add(Vector2 v2) {
          Vector2 v = new Vector2(
               x + v2.getX(),
               y + v2.getY()
          );

          return v;
     }

     // Subtracts the vector 
     public Vector2 subtract(Vector2 v2) {
          Vector2 v = new Vector2(
               x - v2.getX(),
               y - v2.getY()
          );

          return v;
     }

     // Scales the vector
     public Vector2 multiply(float k) {
          Vector2 v = new Vector2(
               k * x,
               k * y
          );

          return v;
     }

     // Returns the normalized vector
     public Vector2 getNormalized() {
          float magnitude = getMagnitude();

          // Return the ZERO vector if the magnitude is 0
          if(magnitude == 0) {
               return ZERO;
          }

          Vector2 v = new Vector2(
               x / magnitude,
               y / magnitude
          );

          return v;
     }

     // Rotates the vector by an angle theta (in radians)
     public Vector2 rotate(float theta) {
          float newX = (float) (x * Math.cos(theta) - y * Math.sin(theta));
          float newY = (float) (x * Math.sin(theta) + y * Math.cos(theta));

          Vector2 v = new Vector2(newX, newY);
          return v;
     }

     public Vector2 rotateAround(Vector2 center, float theta) {
          /* For rotating a vector around the centre, the formula is:

               p' = R(θ) * (p - c) + c
             where,
               p' = new vector
               R(θ) = 2D rotation matrix
               p = vector
               c = centre of rotation  
          */

          // Rotates the vector around a centre

          Vector2 vectorRelativeToCenter = this.subtract(center);
          Vector2 rotatedVector = vectorRelativeToCenter.rotate(theta).add(center);

          return rotatedVector;
     }

     // Adds two vectors
     public static Vector2 add(Vector2 v1, Vector2 v2) {
          return v1.add(v2);
     }

     // Subtracts two vectors
     public static Vector2 subtract(Vector2 v1, Vector2 v2) {
          return v1.subtract(v2);
     }

     // Gets the dot product between two vectors
     public static float dot(Vector2 v1, Vector2 v2) {
          return v1.dot(v2);
     }

     @Override
     public String toString() {
          return "<" + x + ", " + y + ">";
     }

     @Override
     public boolean equals(Object obj) {
          Vector2 v = (Vector2) obj;
          return (x == v.getX()) && (y == v.getY());
     }
}