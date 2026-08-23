package main.models.environment;

import main.math.algebra.Vector2;
import main.models.GameObject;
import main.models.data.IntersectionData;
import main.models.data.RayData;
import main.physics.colliders.Collider;
import main.physics.rays.Ray;
import main.physics.rays.reflectors.OpticalObjectType;
import main.physics.rays.reflectors.RayInteractable;

// Class representing optical objects that can alter light rays
public abstract class OpticalObject extends GameObject implements RayInteractable {
     private final OpticalObjectType opticalObjectType;

     // Constructors
     public OpticalObject(Vector2 position, Collider collider, OpticalObjectType oType) {
          super("", position, collider);
          this.opticalObjectType = oType;
     }

     public OpticalObject(String name, Vector2 position, Collider collider, OpticalObjectType oType) {
          super(name, position, collider);
          this.opticalObjectType = oType;
     }

     // Getters

     public OpticalObjectType getOpticType() {
          return opticalObjectType;
     }

     @Override
     public RayData interact(Ray ray) {
          IntersectionData iData = collider.collideWithRay(ray);
          if(iData == null) return null;
          return this.interactWithRay(ray, iData);
     }
     
     // Abstract methods

     // Interacts & returns data when a ray collides
     protected abstract RayData interactWithRay(Ray ray, IntersectionData intersectionData);
}
