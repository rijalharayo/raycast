package main.models.environment.refractors;

import main.math.algebra.Vector2;
import main.models.data.IntersectionData;
import main.models.data.RayData;
import main.models.environment.OpticalObject;
import main.physics.colliders.Collider;
import main.physics.optics.Medium;
import main.physics.optics.OpticalObjectType;
import main.physics.rays.Ray;

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
     protected RayData interactWithRay(Ray ray, IntersectionData intersectionData) {
          return this.refract(ray, intersectionData);
     }
     
     // Abstract methods
     protected abstract RayData refract(Ray ray, IntersectionData intersectionData);
}
