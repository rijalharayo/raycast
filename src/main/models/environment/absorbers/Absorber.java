package main.models.environment.absorbers;

import java.awt.Graphics2D;

import main.game.ShapeRender;
import main.math.algebra.Vector2;
import main.math.shapes.Shape;
import main.models.data.IntersectionData;
import main.models.entities.LightRay;
import main.models.environment.OpticalObject;
import main.physics.colliders.Collider;
import main.physics.optics.Material;
import main.physics.optics.OpticalObjectType;

// Represents a light absorber
public abstract class Absorber extends OpticalObject {
     private final Material material;
     private final Shape shape;

     // Constructors
     public Absorber(Vector2 posiiton, Collider collider, Material material, Shape shape) {
          super(posiiton, collider, OpticalObjectType.ABSORBER);
          
          this.material = material;
          this.shape = shape;
     }

     // Getters
     public Material getMaterial() {
          return material;
     }

     public Shape getShape() {
          return shape;
     }

     @Override
     protected LightRay interactWithRay(LightRay ray, IntersectionData intersectionData) {
          return this.absorb(ray, intersectionData);
     }

     // Abstract methods
     protected abstract LightRay absorb(LightRay ray, IntersectionData intersectionData);

     @Override
     public void render(Graphics2D g) {
          super.render(g);

          ShapeRender.draw(g, shape, position, collider.getRotation(), material.getColor());
     }

     @Override
     public void rotate(float angle) {
          if(collider != null) {
               collider.setRotation(collider.getRotation() + (float) Math.toRadians(angle));
          }
     }

     @Override
     public void setRotation(float angle) {
          if(collider != null) {
               collider.setRotation((float) Math.toRadians(angle));
          }
     }
}
