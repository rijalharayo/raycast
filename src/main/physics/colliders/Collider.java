package main.physics.colliders;

import main.math.Line;
import main.math.Vector2;
import main.math.shapes.Shape;
import main.models.IntersectionData;
import main.physics.rays.VirtualRay;

public abstract class Collider {
     protected Shape shape;
     protected Vector2 position;

     // Constructors
     public Collider(Vector2 position, Shape shape) {
          this.shape = shape;
          this.position = position;
     }

     public Collider(Vector2 position, Shape shape, float rotation) {
          this.position = position;
          this.shape = shape;
          shape.setRotation(rotation);
     }

     // Getters
     public Vector2 getPosition() {
          return position;
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
          this.position = position.add(t);
     }

     public void setPosition(Vector2 pos) {
          this.position = pos;
     }

     protected IntersectionData collideWithRay(VirtualRay vRay) {
          Line rayLine = vRay.getRayData();
          return shape.intersects(rayLine, this.position);
     }
}
