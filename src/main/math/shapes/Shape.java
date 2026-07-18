package main.math.shapes;

import main.math.Line;
import main.math.Vector2;
import main.models.IntersectionData;

// Represents a mathemtical shape
public abstract class Shape {
     protected Vector2 position;
     protected float rotation = 0f;

     // Constructors
     protected Shape() {} // Default implicit constructor

     public Shape(Vector2 position) {
          this.position = position;
     }

     public Shape(Vector2 pos, float rotation) {
          this.position = pos;
          this.rotation = rotation;
     }

     // Getters
     public Vector2 getPosition() {
          return position;
     }

     public float getRotation() {
          return rotation;
     }

     // Translates the position of the shape
     public void translate(Vector2 t) {
          this.position = position.add(t);
     }

     // Abstract methods
     public abstract void rotate(float theta);
     public abstract void rotateAround(Vector2 center, float theta);
     public abstract Line[] getEdges();
     public abstract IntersectionData intersects(Line line);
}