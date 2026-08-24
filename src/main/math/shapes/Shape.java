package main.math.shapes;

import main.math.Line;
import main.math.algebra.Vector2;
import main.models.data.IntersectionData;

// Represents a mathemtical shape
public abstract class Shape {
     protected float rotation = 0f;

     // Constructors
     protected Shape() {} // Default implicit constructor

     public Shape(float rotation) {
          this.rotation = rotation;
     }

     // Getters
     public float getRotation() {
          return rotation;
     }

     // Setters
     public void setRotation(float r) {
          this.rotation = r;
     }

     public Shape rotate(float theta) {
          Shape rotatedShape = this.rotateShape(theta);
          rotatedShape.rotation = this.rotation + theta;
          
          return rotatedShape;
     }

     public Shape rotateAround(Vector2 center, float theta) {
          Shape rotatedShape = this.rotateShapeAround(center, theta);
          rotatedShape.rotation = this.rotation + theta;

          return rotatedShape;
     }

     // Abstract methods
     protected abstract Shape rotateShape(float theta);
     protected abstract Shape rotateShapeAround(Vector2 center, float theta);
     public abstract boolean containsPoint(Vector2 worldPosition, Vector2 parentPosition);
     public abstract Line[] getEdges();
     public abstract Line[] getWorldEdges(Vector2 parentPosition);
     public abstract IntersectionData intersects(Line line, Vector2 parentPosition);
}