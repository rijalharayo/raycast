package main.math;

import main.math.algebra.Matrix2x2;
import main.math.algebra.Vector2;
import main.models.data.IntersectionData;

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

     // Returns one of the surface normal or the line
     public Vector2 getNormal() {
          return Matrix2x2.ROTATE_ANTI_CLOCKWISE_90.transform(getNormalizedDirection());
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
          // The following method uses crammer's rule

          /* 
               If two points of a line are given as:
                    P(x1, y1)
                    Q(x2, y2)

               The standard form of a line will be:
                    Ax + By = C
                    
                    Where,
                         A = y1 - y2
                         B = x2 - x1
                         C = (y1 - y2) * x1 + (x2 - x1) * y1 or, Ax + By

               ------------------------------------------------------------------------
               ------------------------------------------------------------------------

               According to crammer's rule, if two lines are given as:
                    (1.) A₁x + B₁y = C₁
                    (2.) A₂x + B₂y = C₂

               Then their intersection point will be:
                    x = det(Ax) / det(A)
                    y = det(Ay) / det(A)

               Where,
                    A =  [A1 B1] (Coefficient matrix)
                         [A2 B2]

                    Ax = [C1 B1]
                         [C2 B2]

                    Ay = [A1 C1]
                         [A2 C2]

               And,
                    det(M) = |M| = | a  b | = determinant of matrix M = ad - bc
                                   | c  d |

               If the coefficient matrix 'A' has a determinant 0
               or,
               if det(A) = 0, the lines are parallel or collinear
          */

          // Endpoints of first line
          Vector2 l1Start = this.getStart();
          Vector2 l1End = this.getEnd();
          
          float A1 = l1Start.getY() - l1End.getY();
          float B1 = l1End.getX() - l1Start.getX();
          float C1 = A1 * l1Start.getX() + B1 * l1Start.getY();

          // Endpoints of second line
          Vector2 l2Start = line2.getStart();
          Vector2 l2End = line2.getEnd();

          float A2 = l2Start.getY() - l2End.getY();
          float B2 = l2End.getX() - l2Start.getX();
          float C2 = A2 * l2Start.getX() + B2 * l2Start.getY();

          // Constructs the coefficient matrix
          Matrix2x2 coefficientMatrix = new Matrix2x2(
                                             A1, B1,
                                             A2, B2
                                       );

          float determinant = coefficientMatrix.getDeterminant();

          // Determinant near zero means no unique intersection
          // (parallel or coincident lines)
          if(Math.abs(determinant) < 0.0001f) {
               return null;
          }

          Vector2 constantVector = new Vector2(C1, C2);

          Matrix2x2 Mx = new Matrix2x2(constantVector, coefficientMatrix.getColumn(2));
          Matrix2x2 My = new Matrix2x2(coefficientMatrix.getColumn(1), constantVector);

          float x = Mx.getDeterminant() / determinant;
          float y = My.getDeterminant() / determinant;

          return new Vector2(x, y);
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
