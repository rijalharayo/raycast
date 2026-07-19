package main.physics.colliders;

import main.math.Line;
import main.math.Vector2;
import main.math.shapes.Shape;
import main.models.IntersectionData;
import main.physics.rays.VirtualRay;

public abstract class Collider {
     protected Shape shape;

     // Getters
     public Vector2 getPosition() {
          return shape.getPosition();
     }

     public float getRotation() {
          return shape.getRotation();
     }

     public Shape getShape() {
          return shape;
     }

     // Updaters
     public void setRotation(float rotation) {
          shape.rotateAround(getPosition(), rotation);
     }

     public void translatePosition(Vector2 t) {
          shape.translate(t);
     }

     protected IntersectionData collideWithRay(VirtualRay vRay) {
          Line rayLine = vRay.getRayData();
          return shape.intersects(rayLine);
     }
}
