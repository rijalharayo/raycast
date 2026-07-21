package main.math.shapes;

import main.math.Line;
import main.math.Vector2;
import main.models.IntersectionData;

// Represents a circle
public class Circle extends Shape {
     private float radius;

     public Circle(float radius) {
          this.radius = radius;
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
          // Code
          return null;
     }

     @Override
     public IntersectionData intersects(Line line, Vector2 parentPosition) {
          // Code
          return null;
     }

     @Override
     public void rotateAround(Vector2 center, float theta) {
          // Code
     }

     @Override
     public Line[] getWorldEdges(Vector2 parentPosition) {
          return null;
     }
     
}
