package main.math.shapes;

import main.math.Line;
import main.math.Shape;
import main.math.Vector2;

// Represents a circle
public class Circle extends Shape {
     private float radius;

     public Circle(Vector2 position, float radius) {
          super(position);
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
     public IntersectionData intersects(Line line) {
          // Code
          return null;
     }

     @Override
     public void rotateAround(Vector2 center, float theta) {
          // Code
     }
     
}
