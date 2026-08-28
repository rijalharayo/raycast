package main.models.environment.refractors;

import main.math.algebra.Vector2;
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

     // Constructors
     public Refractor(Vector2 position, Collider collider, Medium medium) {
          super(position, collider, OpticalObjectType.REFRACTOR);
          this.medium = medium;
     }

     public Refractor(String name, Vector2 position, Collider collider, Medium medium) {
          super(name, position, collider, OpticalObjectType.REFRACTOR);
          this.medium = medium;
     }

     // Getters

     public Medium getMedium() {
          return medium;
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
}
