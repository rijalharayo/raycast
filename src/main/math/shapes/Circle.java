package main.math.shapes;

import main.math.Line;
import main.math.algebra.Vector2;
import main.math.shapes.polygons.Polygon;
import main.models.data.IntersectionData;

// Represents a circle
public class Circle extends Shape {
     private float radius;
     private Polygon approximatePolygon;
     // Change in angle while making approximated polygon (in radians)
     private static final float DELTA_THETA = (float) Math.toRadians(20f);

     public Circle(float radius) {
          this.radius = radius;
          apporximateCircle();
     }

     // Approximates the shape of a circle using a polygon
     private void apporximateCircle() {
          double fullCircle = 2 * Math.PI;

          // Calculates the number of vertices required
          int vertexCount = (int) Math.round(fullCircle / DELTA_THETA);

          Vector2[] localVertices = new Vector2[vertexCount];

          for(int i = 0; i < vertexCount; i++) {
               // Converts from degree to radians
               float theta = i * DELTA_THETA;

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
     public Circle rotate(float theta) {
          // Code
          System.out.println("A circle rotation is symmetric");
          return null;
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
     public Shape rotateAround(Vector2 center, float theta) {
          return approximatePolygon.rotateAround(center, theta);
     }
}
