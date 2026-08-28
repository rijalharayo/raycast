package main.models.environment.refractors;

import java.awt.Graphics2D;

import main.game.ShapeRender;
import main.math.algebra.Vector2;
import main.math.shapes.Shape;
import main.models.data.IntersectionData;
import main.models.data.RayData;
import main.models.entities.LightRay;
import main.models.environment.OpticalObject;
import main.physics.colliders.Collider;
import main.physics.optics.Medium;
import main.physics.optics.OpticalObjectType;

// Main refractor class
public abstract class Refractor extends OpticalObject {
     private final Medium medium;
     private final Shape shape;

     // Constructors
     public Refractor(Vector2 position, Collider collider, Medium medium, Shape shape) {
          super(position, collider, OpticalObjectType.REFRACTOR);
          this.medium = medium;
          this.shape = shape;
     }

     public Refractor(String name, Vector2 position, Collider collider, Medium medium, Shape shape) {
          super(name, position, collider, OpticalObjectType.REFRACTOR);
          this.medium = medium;
          this.shape = shape;
     }

     // Getters

     public Medium getMedium() {
          return medium;
     }

     public Shape getShape() {
          return shape;
     }

     public float getRefractiveIndex() {
          return medium.getRefractiveIndex();
     }

     @Override
     protected RayData interactWithRay(LightRay ray, IntersectionData intersectionData) {
          return this.refract(ray, intersectionData);
     }
     
     protected RayData refract(LightRay ray, IntersectionData intersectionData) {
          // Code
          return null;
     }

     @Override
     public void render(Graphics2D g) {
          super.render(g);

          // Draws the shape to the screen
          ShapeRender.draw(
               g,
               shape,
               position,
               collider.getRotation(),
               medium.getColor()
          );
     }
}
