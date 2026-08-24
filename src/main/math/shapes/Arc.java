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
          this.radius = radius;
          this.angle = angle;
          this.thickness = thickness;

          apporximateArc();
     }

     public Arc(float radius, float angle, float thickness, float rotation) {
          this.radius = radius;
          this.angle = angle;
          this.thickness = thickness;
          this.rotation = rotation;

          apporximateArc();
     }

     public void apporximateArc() {
          int arcVertexCount = Math.round(angle / DELTA_THETA) + 1;

          Vector2[] localVertices = new Vector2[arcVertexCount * 2];

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

          // Inner arc backwards
          for(int i = 0; i < arcVertexCount; i++) {
               // Accounts for rotation
               float theta = rotation + angle - (i * DELTA_THETA);

               localVertices[i + arcVertexCount] = new Vector2(
                    (float) (innerRadius * Math.cos(theta)),
                    (float) (innerRadius * Math.sin(theta))
               );
          }

          this.approximatedPolygon = new Polygon(localVertices);

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
          return approximatedPolygon.containsPoint(worldPosition, parentPosition);
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

     @Override
     public IntersectionData intersects(Line line, Vector2 parentPosition) {
          Vector2 center = getWorldCenterOfCurvature(
               parentPosition,
               rotation
          );

          return approximatedPolygon.intersects(line, center);
     }
}