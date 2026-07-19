package main.models.environment;

import main.math.Vector2;
import main.models.GameObject;
import main.models.IntersectionData;
import main.physics.colliders.Collider;
import main.physics.rays.Ray;
import main.physics.rays.RayHit;
import main.physics.rays.reflectors.OpticalObjectType;
import main.physics.rays.reflectors.RayInteractable;

// Class representing optical objects that can alter light rays
public abstract class OpticalObject extends GameObject implements RayInteractable {
     private final Collider collider;
     private final OpticalObjectType opticalObjectType;

     // Constructors
     public OpticalObject(Vector2 position, Collider collider, OpticalObjectType oType) {
          super("", position);
          this.collider = collider;
          this.opticalObjectType = oType;
     }

     public OpticalObject(String name, Vector2 position, Collider collider, OpticalObjectType oType) {
          super(name, position);
          this.collider = collider;
          this.opticalObjectType = oType;
     }

     // Getters
     public Collider getCollider() {
          return this.collider;
     }

     public OpticalObjectType getOpticType() {
          return opticalObjectType;
     }

     @Override
     public RayHit interact(Ray ray) {
          IntersectionData iData = collider.collideWithRay(ray);
          return this.interactWithRay(ray, iData);
     }
     // Abstract methods

     // Interacts & returns data when a ray collides
     protected abstract RayHit interactWithRay(Ray ray, IntersectionData intersectionData);
}
