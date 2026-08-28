package main.physics.colliders;

import main.math.algebra.Vector2;
import main.models.GameObject;
import main.models.data.IntersectionData;

public class CollisionData {
     private final Vector2 collisionPoint;
     private final GameObject collisionObject;
     private final IntersectionData intersectionData;

     public CollisionData(Vector2 collisionPoint, GameObject collisionObject, IntersectionData iData) {
          this.collisionPoint = collisionPoint;
          this.collisionObject = collisionObject;
          this.intersectionData = iData;
     }

     // Getters

     public Vector2 getCollisionPoint() {
          return collisionPoint;
     }

     public GameObject getCollisionObject() {
          return collisionObject;
     }

     public IntersectionData getIntersectionData() {
          return intersectionData;
     }
}
