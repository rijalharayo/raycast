package main.math;

import main.models.IntersectionData;

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
          float m = (float) getLineVector().getY() / getLineVector().getX();
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
     public static boolean haveParalellDirection(Line l1, Line l2) {
          /* 
               Two lines are parrallel or collinear,
               if the dot product between their unit vectors is either +1 or -1
          */
          float dotProduct = l1.getNormalizedDirection().dot(l2.getNormalizedDirection());
          
          float d1 = Math.abs(dotProduct - 1.0f);
          float d2 = Math.abs(dotProduct + 1.0f);

          float epsilon = 0.0001f;
          // Accounts for precision errors & returns
          return d1 <= epsilon || d2 <= epsilon;
     }

     // Calculates the intersection points of 2 lines
     public Vector2 findIntersection(Line line2) {
          Vector2 solution = null;
          // They don't have a solution if they are parallel
          if(haveParalellDirection(this, line2)) {
               return null;
          }

          float dx1 = this.lineVector.getX();
          float dx2 = line2.lineVector.getX();

          // First line is vertical
          if(dx1 == 0) {
               float x = this.start.getX();
               float m2 = line2.getSlope();

               float y = m2 * (x - line2.start.getX()) + line2.start.getY();

               return new Vector2(x, y);
          }

          // Second line is vertical
          if(dx2 == 0) {
               float x = line2.start.getX();
               float m1 = this.getSlope();

               float y = m1 * (x - this.start.getX()) + this.start.getY();

               return new Vector2(x, y);
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

     // Checks if a point is projected to a line
     private boolean liesBetween(Vector2 pA, Vector2 pB, Vector2 position) {
          /* 
               pA = A
               pB = B
               postion = C
          */
          
          Vector2 AB = pB.subtract(pA);
          Vector2 AP = position.subtract(pA);
          Vector2 BP = position.subtract(pB);

          float d1 = AB.dot(AP);
          float d2 = AB.dot(BP);

          return d1 * d2 <= 0;
     }

     // Checks and returns data if the lines intersect
     public IntersectionData intersects(Line line2) {
          IntersectionData iData = null;

          // Line segments can't intersect if they are parralell
          if(haveParalellDirection(this, line2)) {
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

               If P is the intersection point between the two segments, it must satisfy:
                    1. AB ⋅ AP ∈ [0, |AB|²]     (P is between A and B)
                    2. CD ⋅ CP ∈ [0, |CD|²]     (P is between C and D)
          */
          
          // Checking constraints

          // Checks if the end-points themselves are the intersections
          boolean C_on_AB = liesBetween(start, end, line2.getStart());
          boolean D_on_AB = liesBetween(start, end, line2.getEnd());

          boolean A_on_CD = liesBetween(line2.getStart(), line2.getEnd(), start);
          boolean B_on_CD = liesBetween(line2.getStart(), line2.getEnd(), end);
          // Checks if the segments could possibly intersect somewhere
          boolean possibleIntersection = C_on_AB || D_on_AB || A_on_CD || B_on_CD;

          // Return null if no possibility of intersection to save efficiency
          if(!possibleIntersection) {
               return null;
          }

          // Finds the intersection of the infinite lines the segments lie on
          Vector2 intersection = this.findIntersection(line2);
          if (intersection == null) {
               return null;
          }

          // Checks if the intersection lies within the line segments
          boolean onThisSegment = liesBetween(this.start, this.end, intersection);
          boolean onLine2Segment = liesBetween(line2.getStart(), line2.getEnd(), intersection);

          if ((onThisSegment && onLine2Segment)) {
               // That is the interseciton point
               iData = new IntersectionData(intersection, line2, this);
          }

          return iData;
     }

     @Override
     public String toString() {
          return "Start: " + start + "\nEnd: " + end;
     }
}
