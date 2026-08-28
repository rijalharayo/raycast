package main.physics.rays;

import main.math.algebra.Vector2;
import main.models.GameObject;
import main.models.data.RayData;
import main.physics.colliders.CollisionType;

// Stores the data after a ray collides with something
public class RayHit {
     private boolean hit;
     private Vector2 collisionPoint;
     private CollisionType collisionType;
     private GameObject collisionObject;
     private RayData nextRayData;

     // Constructors
     public RayHit() {};

     public RayHit(boolean hit, Vector2 collisionPoint) {
          this.hit = hit;
          this.collisionPoint = collisionPoint;
     }

     public RayHit(boolean hit, Vector2 collisionPoint, GameObject targetObject, CollisionType collisionType) {
          this.hit = hit;
          this.collisionPoint = collisionPoint;
          this.collisionType = collisionType;
          this.collisionObject = targetObject;
     }

     // Getters
     public boolean hasHit() {
          return hit;
     }

     // Only return other data if the ray has actaully hit something

     public Vector2 getCollisionPoint() {
          return hit ? collisionPoint : null;
     }

     public CollisionType getCollisionType() {
          return hit ? collisionType : null;
     }

     public GameObject getTargetObject() {
          return hit ? collisionObject : null;
     }

     public RayData getNextRayData() {
          return hit ? nextRayData : null;
     }

     // Setters
     public void setNextRayData(RayData data) {
          // Only set data if the ray has collided with something
          if(hit) { this.nextRayData = data; return; }
          throw new IllegalStateException("Cannot set next ray when the ray didn't hit anything");
     }
}
