package main.math;

// Stores a line data
public class Line {
     protected Vector2 start;
     protected Vector2 end;
     protected Vector2 lineVector;

     // Constructors
     public Line() {}

     public Line(Vector2 start, Vector2 end) {
          // Prevent start & end positions from being null or equal
          if(start == null || end == null) throw new IllegalArgumentException("Start & end can't be null");
          if(start == end) throw new IllegalArgumentException("Start & end positions can't be equal");

          this.start = start;
          this.end = end;
          // Stores the line in a vector form
          this.lineVector = end.subtract(start);
     }
     
     // Getters
     public Vector2 getStart() {
          return start;
     }

     public Vector2 getEnd() {
          return end;
     }

     public Vector2 getNormalizedDirection() {
          return lineVector.getNormalized();
     }

     public float getLength() {
          return end.subtract(start).getMagnitude();
     }

     public Vector2 getNormal() {
          return getNormalizedDirection().rotate((float) Math.PI/2);
     }

     // Rotates the line by a certain angle (in radians)
     public void rotate(float theta) {
          this.start = start.rotate(theta);
          this.end = end.rotate(theta);
          this.lineVector = lineVector.rotate(theta);
     }

     public void rotateAround(Vector2 center, float theta) {
          /* For rotating a vector around the centre, the formula is:

               p' = R(θ) * (p - c) + c
             where,
               p' = new vector
               R(θ) = 2D rotation matrix
               p = vector
               c = centre of rotation  
          */

          // Rotates the start & end pos

          Vector2 startRelativeToCenter = start.subtract(center);
          this.start = startRelativeToCenter.rotate(theta).add(center);

          Vector2 endRelativeToCenter = end.subtract(center);
          this.end = endRelativeToCenter.rotate(theta).add(center);
          // Gets the new normalized direction
          this.lineVector = end.subtract(start);
     }
}
