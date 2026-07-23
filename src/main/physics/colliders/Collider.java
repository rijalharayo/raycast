package main.physics.colliders;

import java.awt.Color;
import java.awt.Graphics2D;

import main.game.Scene;
import main.math.Line;
import main.math.algebra.Vector2;
import main.math.shapes.Shape;
import main.models.IntersectionData;
import main.physics.rays.Ray;

public abstract class Collider {
     protected Shape shape;
     protected Vector2 position;

     // Constructors
     public Collider(Vector2 position, Shape shape) {
          this.shape = shape;
          this.position = position;
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

     public IntersectionData collideWithRay(Ray vRay) {
          Line rayLine = vRay.getRayData();
          return shape.intersects(rayLine, this.position);
     }

     // Draws the collider hitbox
     public void draw(Graphics2D g) {
     Line[] edges = shape.getWorldEdges(position);

     g.setColor(Color.RED);

     for(Line edge : edges) {
          Vector2 start = Scene.worldToScreen(edge.getStart());
          Vector2 end = Scene.worldToScreen(edge.getEnd());

          // Draw edge
          g.drawLine(
               (int) start.getX(),
               (int) start.getY(),
               (int) end.getX(),
               (int) end.getY()
          );

          // Draw start vertex
          g.fillOval(
               (int) start.getX() - 4,
               (int) start.getY() - 4,
               8,
               8
          );
     }

     // Draw center
     Vector2 center = Scene.worldToScreen(position);

     g.setColor(Color.BLUE);
     g.fillOval(
          (int) center.getX() - 5,
          (int) center.getY() - 5,
          10,
          10
     );
}
}