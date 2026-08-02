package main.physics.colliders;

import main.math.algebra.Vector2;
import main.math.shapes.polygons.Rectangle;

// Represents a rectangular / box collider
public class BoxCollider extends Collider {
     public BoxCollider(Vector2 position, int width, int height) {
          super(position, new Rectangle(width, height));
     }

     public BoxCollider(Vector2 position, int width, int height, float rotation) {
          super(position, new Rectangle(width, height, (float) Math.toRadians(rotation)));
     }
}