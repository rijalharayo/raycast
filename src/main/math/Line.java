package main.math;

import main.math.shapes.IntersectionData;

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

     public Vector2 getLineVector() {
          return lineVector;
     }

     public float getSlope() {
          float m = (float) Math.tan(getLineVector().getY() / getLineVector().getX());
          return m;
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
          this.start = start.rotateAround(center, theta);
          this.end = end.rotateAround(center, theta);
          // Gets the new normalized direction
          this.lineVector = end.subtract(start);
     }

     // Checks if the lines are collinear(or parrallel)
     public static boolean areParallel(Line l1, Line l2) {
          /* 
               Two lines are parrallel or collinear,
               if the dot product between their unit vectors is either +1 or -1
          */
          float dotProduct = l1.getNormalizedDirection().dot(l2.getNormalizedDirection());
          
          float d1 = Math.abs(dotProduct - 1.0f);
          float d2 = Math.abs(dotProduct + 1.0f);

          float epsilon = 0.00000001f;
          // Accounts for precision errors & returns
          return d1 <= epsilon || d2 <= epsilon;
     }

     // Calculates the intersection points of 2 lines
     public Vector2 findIntersection(Line line2) {
          Vector2 solution = null;
          // They don't have a solution if they are parallel
          if(areParallel(this, line2)) {
               return null;
          }

          /* 
               Given two lines in their point-slope form:
                    1. f(x) = m1 (x - a) + b
                    2. g(x) = m2 (x - c) + d

               Their point of intersection is:
                    x = (d - b + m1 * a - m2 * c) / (m1 - m2)
                    y = f(x) or g(x) (Putting the result into any of the 2 functions)
          */

          // Values for the variables
          float m1 = this.getSlope();
          float m2 = line2.getSlope();

          float a = this.getStart().getX();
          float b = this.getStart().getY();

          float c = line2.getStart().getX();
          float d = line2.getStart().getY();

          float numerator = d - b + (m1 * a) - (m2 * c);
          float denominator = m1 - m2;
          // Calculate x
          float x = numerator / denominator;
          // Calculate y by putting x in f(x)
          float y = m1 * (x - a) + b;

          // Set the solution
          solution = new Vector2(x, y);

          return solution;
     }

     // Checks and returns data if the lines intersect
     public IntersectionData intersects(Line line2) {
          IntersectionData iData = null;

          // Line segments can't intersect if they are parralell
          if(areParallel(line2, line2)) {
               return null;
          }

          /* 
               Given two non-collinear line segments AB & CD in their vector form,
               they intersect when they meet the following conditions:

               The following dot products must be subjects to their respective constraints:
                    1. AB ⋅ AC ∈ [0, ∞)
                    2. AB ⋅ BC ∈ (-∞, 0]
                    3. CD ⋅ CA ∈ [0, ∞)
                    4. CD ⋅ DA ∈ (-∞, 0]
          */
          
          // Vectors required
          Vector2 AB = this.lineVector;
          Vector2 CD = line2.lineVector;

          Vector2 AC = line2.getStart().subtract(this.getStart());
          Vector2 BC = line2.getEnd().subtract(this.getStart());

          Vector2 CA = AC.multiply(-1);
          Vector2 DA = this.getStart().subtract(line2.getEnd());

          // The respective dot products
          float ABdotAC = AB.dot(AC);
          float ABdotBC = AB.dot(BC);
          float CDdotCA = CD.dot(CA);
          float CDdotDA = CD.dot(DA);

          // Checking constraints
          boolean check1 = ABdotAC >= 0;
          boolean check2 = ABdotBC <= 0;
          boolean check3 = CDdotCA >= 0;
          boolean check4 = CDdotDA <= 0;

          // If all checks are true, then the lines intersect
          if(check1 && check2 && check3 && check4) {
               // Calculate their intersection point
               Vector2 intersection = this.findIntersection(line2);
               // Sets the intersection data
               iData = new IntersectionData(intersection, this, line2);
          }

          return iData;
     }
}
