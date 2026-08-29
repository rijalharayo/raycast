package main.physics.colliders;

import main.math.algebra.Vector2;
import main.math.shapes.polygons.Polygon;

// Represents a collier with custom bounding boxes
public class PolygonCollider extends Collider {
     // Constructors
     public PolygonCollider(Vector2 position, Polygon polygon) {
          super(position, polygon);
     }

     public PolygonCollider(Vector2 position, Polygon polygon, float rotation) {
          super(position, polygon);
          this.setRotation(rotation);
     }
}
