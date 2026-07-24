package main.physics.colliders;

import main.math.algebra.Vector2;
import main.math.shapes.Arc;

// Represents an collider in shape of some arc
public class ArcCollider extends Collider {
     // Constructors
     public ArcCollider(Vector2 position, float radius, float angle, float thickness) {
          super(position, 
               new Arc(radius, angle, thickness)
          );
     }
     
     // Getters
     public float getRadius() {
          Arc arc = (Arc) shape;
          return arc.getRadius();
     }

     public float getAngle() {
          Arc arc = (Arc) shape;
          return arc.getAngle();
     }

     public float getThickness() {
          Arc arc = (Arc) shape;
          return arc.getThickness();
     }
}