package main.physics.colliders;

import main.math.algebra.Vector2;
import main.math.shapes.Circle;

// Represents a rectangular / box collider
public class CircleCollider extends Collider {
     public CircleCollider(Vector2 position, float radius) {
          super(position, new Circle(radius));
     }
}
