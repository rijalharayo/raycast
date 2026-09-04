package main.math.shapes;

import main.math.Line;
import main.math.algebra.Vector2;
import main.math.shapes.polygons.Polygon;
import main.models.data.IntersectionData;

// Represents a mathematical arc
public class Arc extends Shape {
     private float radius;
     private float angle;
     private float thickness;

     private Polygon approximatedPolygon;
     private Line[] flatEdges = new Line[2];

     // Change in angle while making approximated polygon (in radians)
     private static final float DELTA_THETA = (float) Math.toRadians(10f);

     // Constructors
     public Arc(float radius, float angle, float thickness) {
          if(thickness < 0) throw new IllegalArgumentException("Thickness can't be negative");

          this.radius = radius;
          this.angle = angle;
          this.thickness = thickness;

          apporximateArc();
     }

     public Arc(float radius, float angle, float thickness, float rotation) {
          if(thickness < 0) throw new IllegalArgumentException("Thickness can't be negative");

          this.radius = radius;
          this.angle = angle;
          this.thickness = thickness;
          this.rotation = rotation;

          apporximateArc();
     }

     public void apporximateArc() {
          int arcVertexCount = Math.round(angle / DELTA_THETA) + 1;
          int actualVertexCount = thickness == 0 ? arcVertexCount : arcVertexCount * 2;

          Vector2[] localVertices = new Vector2[actualVertexCount];

          float innerRadius = radius - thickness;

          // Outer arc
          for(int i = 0; i < arcVertexCount; i++) {
               // Accounts for rotation
               float theta = rotation + i * DELTA_THETA;

               localVertices[i] = new Vector2(
                    (float) (radius * Math.cos(theta)),
                    (float) (radius * Math.sin(theta))
               );
          }

          // Only make inner arc if it has thickness
          if(thickness > 0) {
               // Inner arc backwards
               for(int i = 0; i < arcVertexCount; i++) {
                    // Accounts for rotation
                    float theta = rotation + angle - (i * DELTA_THETA);

                    localVertices[i + arcVertexCount] = new Vector2(
                         (float) (innerRadius * Math.cos(theta)),
                         (float) (innerRadius * Math.sin(theta))
                    );
                    
               }

               // The two flat edges connecting the arcs
               Line startFlat = new Line(
                    localVertices[2 * arcVertexCount - 1],
                    localVertices[0]
               );

               Line endFlat = new Line(
                    localVertices[arcVertexCount - 1],
                    localVertices[arcVertexCount]
               );

               this.flatEdges[0] = startFlat;
               this.flatEdges[1] = endFlat;
          }
          else {
               flatEdges[0] = new Line(
                    localVertices[arcVertexCount - 1],
                    localVertices[0]
               );
          }

          this.approximatedPolygon = new Polygon(localVertices);
     }

     // Getters
     public float getRadius() {
          return radius;
     }

     public float getAngle() {
          return angle;
     }

     public float getThickness() {
          return thickness;
     }

     public Line[] getFlatEdges() {
          return flatEdges.clone();
     }

     public Line[] getWorldFlatEdges(Vector2 parentPosition) {
          Vector2 center = getWorldCenterOfCurvature(
               parentPosition,
               rotation
          );

          return new Line[] {
               flatEdges[0].translate(center),
               flatEdges[1].translate(center)
          };
     }

     public Vector2 getLocalCenterOffset() {
          float middleAngle = angle / 2;

          Vector2 surfaceOffset = new Vector2(
               radius * (float) Math.cos(middleAngle),
               radius * (float) Math.sin(middleAngle)
          );

          return surfaceOffset.multiply(-1f);
     }

     public Vector2 getWorldCenterOfCurvature(Vector2 parentPosition, float rotation) {
          // Returns the center of curvature in world coordinates
          return parentPosition.add(
               getLocalCenterOffset().rotate(rotation)
          );
     }

     @Override
     public Arc rotateShape(float theta) {
          return new Arc(
               radius,
               angle,
               thickness,
               rotation + theta
          );
     }

     @Override
     public Arc rotateShapeAround(Vector2 center, float theta) {
          this.approximatedPolygon = (Polygon) approximatedPolygon.rotateAround(center, theta);
          return this;
     }

     @Override
     public boolean containsPoint(Vector2 worldPosition, Vector2 parentPosition) {
          /* 
               This method checks if a point lies in the annular sector by:

                    1. Checking if the point's distance from the center is
                       between the two radii.

                    2. The point vector relative to the center lies between
                       the two flat edges of the vector.

                       It must be counter-clockwise from one edge & clockwise
                       from another.
          */

          Vector2 centerOfCurvature = getWorldCenterOfCurvature(parentPosition, rotation);

          // World point relative to center
          Vector2 pointRelativeToCenter = worldPosition.subtract(centerOfCurvature);

          float smRadius = radius - thickness;
          float bgRadius = radius;

          // Checks if the point lies in between the radii (distance wise)
          boolean inBetweenRadii = smRadius <= pointRelativeToCenter.getMagnitude() &&
                                   bgRadius >= pointRelativeToCenter.getMagnitude();

          // Return false if the conidtion fails
          if(!inBetweenRadii) return false;

          // Checks if the relative vector lies in between the two flat edges

          Vector2 relativeUnitVector = pointRelativeToCenter.getNormalized();

          Vector2 flatEdge1 = flatEdges[0].getNormalizedDirection();
          Vector2 flatEdge2 = flatEdges[1].getNormalizedDirection();

          /* 
               For a vector C to lie between vectors A & B:

                    A x C < 0 and B x C > 0
                    or,
                    A x C > 0 and B x C < 0

               where, A x B means 2D cross product
          */

          // A x C
          float crossA = flatEdge1.cross(relativeUnitVector);
          // B x C
          float crossB = flatEdge2.cross(relativeUnitVector);

          /*
               The two flat edges are oriented in opposite directions:
               the first points outward from the center, while the second
               points inward. This flipped direction reverses the sign of
               its cross product, so a point inside the minor sector has
               cross products with the same sign.
          */
          boolean inMinor = crossA >= 0 && crossB >= 0;

          // Checks if the arc is a major arc or a minor arc
          // For a major sector, the valid region is the complement of the minor sector.
          boolean isMajorSector = angle > Math.PI;

          return isMajorSector ? !inMinor : inMinor;
     }

     @Override
     public Line[] getEdges() {
          return approximatedPolygon.getEdges();
     }

     @Override
     public Line[] getWorldEdges(Vector2 parentPosition) {
          Vector2 center = getWorldCenterOfCurvature(
               parentPosition,
               rotation
          );

          return approximatedPolygon.getWorldEdges(center);
     }

     public Polygon getApproximatedPolygon() {
          return approximatedPolygon;
     }

     @Override
     public IntersectionData intersects(Line line, Vector2 parentPosition) {
          Vector2 center = getWorldCenterOfCurvature(
               parentPosition,
               rotation
          );

          return approximatedPolygon.intersects(line, center);
     }
}