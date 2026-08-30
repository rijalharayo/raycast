package main.physics.rays;

import main.math.algebra.Vector2;
import main.models.GameObject;
import main.physics.colliders.CollisionData;
import main.physics.colliders.CollisionType;

// Stores the data after a ray collides with something
public class RayHit {
     private boolean hit;
     private Vector2 collisionPoint;
     private CollisionData collisionData;
     private CollisionType collisionType;
     private GameObject collisionObject;
     private Ray nextRay;

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

     public RayHit(boolean hit, CollisionData collisionData, GameObject targetObject, CollisionType collisionType) {
          this.hit = hit;
          this.collisionData = collisionData;
          this.collisionPoint = collisionData.getCollisionPoint();
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

     public CollisionData getCollisionData() {
          return hit ? collisionData : null;
     }

     public GameObject getTargetObject() {
          return hit ? collisionObject : null;
     }

     public Ray getNextRay() {
          return hit ? nextRay : null;
     }

     // Setters
     public void setNextRay(Ray ray) {
          // Only set ray if the ray has collided with something
          if(hit) { this.nextRay = ray; return; }
          throw new IllegalStateException("Cannot set next ray when the ray didn't hit anything");
     }
}
