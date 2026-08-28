package main.models.environment.reflectors;

import main.math.algebra.Vector2;
import main.models.data.IntersectionData;
import main.models.data.RayData;
import main.models.entities.LightRay;
import main.models.environment.OpticalObject;
import main.physics.colliders.Collider;
import main.physics.optics.OpticalObjectType;

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
     protected RayData interactWithRay(LightRay ray, IntersectionData intersectionData) {
          return this.reflect(ray, intersectionData);
     }
     
     // Abstract methods
     protected abstract RayData reflect(LightRay ray, IntersectionData intersectionData);
}