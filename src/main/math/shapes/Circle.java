package main.math.shapes;

import main.math.Line;
import main.math.algebra.Vector2;
import main.math.shapes.polygons.Polygon;
import main.models.IntersectionData;

// Represents a circle
public class Circle extends Shape {
     private float radius;
     private Polygon approximatePolygon;
     // Change in angle while making approximated polygon (in degrees)
     private static final float DELTA_THETA = 20f;

     public Circle(float radius) {
          this.radius = radius;
          apporximateCircle();
     }

     // Approximates the shape of a circle using a polygon (this is in degrees)
     private void apporximateCircle() {
          // Calculates the number of vertices required
          int vertexCount = Math.round(360 / DELTA_THETA);

          Vector2[] localVertices = new Vector2[vertexCount];

          for(int i = 0; i < vertexCount; i++) {
               // Converts from degree to radians
               float theta = (float) Math.toRadians(i * DELTA_THETA);

               Vector2 vertex = new Vector2(
                                   (float) (radius * Math.cos(theta)),
                                   (float) (radius * Math.sin(theta))
                                );

               localVertices[i] = vertex;
          }

          this.approximatePolygon = new Polygon(localVertices);
     }

     // Getters
     public float getRadius() {
          return radius;
     }

     @Override
     public void rotate(float theta) {
          // Code
     }

     @Override
     public Line[] getEdges() {
          return approximatePolygon.getEdges();
     }

     @Override
     public Line[] getWorldEdges(Vector2 parentPosition) {
          return approximatePolygon.getWorldEdges(parentPosition);
     }

     @Override
     public IntersectionData intersects(Line line, Vector2 parentPosition) {
          return approximatePolygon.intersects(line, parentPosition);
     }

     @Override
     public void rotateAround(Vector2 center, float theta) {
          // Code
     }
}
