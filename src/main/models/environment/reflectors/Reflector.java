package main.models.environment.reflectors;

import main.math.algebra.Vector2;
import main.models.data.IntersectionData;
import main.models.data.RayData;
import main.models.environment.OpticalObject;
import main.physics.colliders.Collider;
import main.physics.rays.Ray;
import main.physics.rays.reflectors.OpticalObjectType;

public abstract class Reflector extends OpticalObject {
     // Constructors
     public Reflector(Vector2 position, Collider collider) {
          super(position, collider, OpticalObjectType.REFLECTOR);
     }

     public Reflector(String name, Vector2 position, Collider collider) {
          super(name, position, collider, OpticalObjectType.REFLECTOR);
     }

     // Getters
     @Override
     protected RayData interactWithRay(Ray ray, IntersectionData intersectionData) {
          return this.reflect(ray, intersectionData);
     }

     @Override
     public void update() {
          // Code
     }
     
     // Abstract methods
     protected abstract RayData reflect(Ray ray, IntersectionData intersectionData);
}