package main.math.shapes.polygons;

import main.math.algebra.Vector2;

// A right angle triangle
public class RightTriangle extends Triangle {
     private final float perpendicular;
     private final float base;

     // Constructors
     public RightTriangle(float p, float b) {
          super(calculateLocalVertices(p, b));

          this.perpendicular = p;
          this.base = b;
     }

     public RightTriangle(float p, float b, float rotation) {
          super(rotation, calculateLocalVertices(p, b));

          this.perpendicular = p;
          this.base = b;
     }

     // Getters
     
     public float getBase() {
          return this.base;
     }

     public float getHeight() {
          return this.perpendicular;
     }

     public float getHypotenuse() {
          return (float) Math.hypot(base, perpendicular);
     }

     // Calculates the local vertices based on perpendicular & base
     public static Vector2[] calculateLocalVertices(float p, float b) {
          /* 
               For any triangle, it's centroid is:
                    G = (b/3, p/3)


               Suppose a rt. triangle ABC with length p & b
               It's vertices are:
                    A(0, 0)
                    B(b, 0)
                    C(0, p)

               Shifting the centroid to the origin(0, 0), G(centroid) must be subtracted from
               every vertex, hence:

               It's vertices are:
                    A = (-b/3, -p/3)
                    B = (2b/3, -p/3)
                    C = (-b/3, 2p/3)
          */

          Vector2 vertexA = new Vector2(-b/3, -p/3);
          Vector2 vertexB = new Vector2(2 * b/3, -p/3);
          Vector2 vertexC = new Vector2(-b/3, 2 * p/3);

          return new Vector2[] { vertexA, vertexB, vertexC };
     }
}
