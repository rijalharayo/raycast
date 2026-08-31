package main.physics.rays;

import main.game.Game;
import main.game.Scene;
import main.game.SceneManager;
import main.math.algebra.Vector2;
import main.models.data.RayData;
import main.models.environment.OpticalObject;
import main.physics.colliders.CollisionData;
import main.physics.colliders.CollisionType;

// Virtual rays used for raycasting
public class VirtualRay implements Ray {
     private RayData rayData;
     // For discrete casting
     public static final float LENGTH = 2f;

     private Vector2 currentPosition;
     private Vector2 prevPosition;

     // An object the ray must ignore when casted
     private OpticalObject objectOfAvoidance = null;

     // For continuous casting
     private float length = -1f;
     
     // Overloaded constructors
     public VirtualRay(Vector2 origin, Vector2 direction) {
          rayData = new RayData();
          rayData.setFromDirection(origin, direction.getAngle(), LENGTH);
          this.currentPosition = origin;
          this.prevPosition = origin;
     }

     public VirtualRay(Vector2 origin, float angle) {
          rayData = new RayData();
          rayData.setFromDirection(origin, angle, LENGTH);
          this.currentPosition = origin;
          this.prevPosition = origin;
     }

     public VirtualRay(Vector2 origin, Vector2 direction, float length) {
          if(length <= 0) {
               throw new IllegalArgumentException("Length must be greater than 0");
          }

          rayData = new RayData();
          rayData.setFromDirection(origin, direction.getAngle(), length);
          this.currentPosition = origin;
          this.prevPosition = origin;
          this.length = length;
     }

     public VirtualRay(Vector2 origin, float angle, float length) {
          if(length <= 0) {
               throw new IllegalArgumentException("Length must be greater than 0");
          }

          rayData = new RayData();
          rayData.setFromDirection(origin, angle, length);
          this.currentPosition = origin;
          this.prevPosition = origin;
          this.length = length;
     }

     // Getters
     public Vector2 getCurrentPosition() {
          return currentPosition;
     }

     // Setters
     public void ignoreObject(OpticalObject object) {
          this.objectOfAvoidance = object;
     }

     // Casts the ray in the direction until collision
     public RayHit castDiscrete() {
          boolean collision = false;

          RayHit rayHit = null;

          // Run until it collides
          while(!collision) {
               boolean out = isOutOfBounds();
               rayHit = checkOpticalObjectCollision();
               boolean hit = rayHit != null;

               if(out || hit) {
                    collision = true;

                    if(out) {
                         rayHit = new RayHit(true, currentPosition, null, CollisionType.BOUNDS_COLLISION);
                    }

                    break;
               }

               Vector2 velocity = rayData.getLineVector();
               // Updates the previous position
               prevPosition = currentPosition;
               // Updates ray position
               currentPosition = prevPosition.add(velocity);
          }

          return rayHit;
     }

     // Cast a ray of some length and returns the closest collision
     public RayHit cast() {
          if(this.length <= 0f) {
               throw new IllegalArgumentException("A valid length must be set");
          }

          Vector2 start = rayData.getStart();

          RayHit closestHit = null;
          float closestDistance = Float.MAX_VALUE;

          for (OpticalObject opticalObject : SceneManager.getCurrentScene().getSceneOpticalObjects()) {
               CollisionData collisionData = opticalObject.getCollider().collideWithRay(this);

               // Continue if no collision
               if (collisionData == null) {
                    continue;
               }

               // Get the distance between the collision point and the origin of the ray
               float distance = (float) collisionData.getCollisionPoint().subtract(start).getMagnitude();

               // If it's closer, set it as the closest
               if ((distance < closestDistance) && opticalObject != objectOfAvoidance) {
                    closestDistance = distance;
                    closestHit = new RayHit(true, collisionData, opticalObject, CollisionType.OPTICAL_COLLISION);
               }
          }

          // The closest collision is the next collision
          return closestHit;
     }

          // Checks if the virtual ray has gone out of bounds
     public boolean isOutOfBounds() {
          boolean outOfBounds = false;

          // Converts world positions to screen positions
          Vector2 screenPosition = Scene.worldToScreen(currentPosition);
          Vector2 prevScreenPosition = Scene.worldToScreen(prevPosition);

          // Checks if the current position has gone out of bounds on the X-axis
          if((screenPosition.getX() >= Game.WIDTH) || (screenPosition.getX() <= 0)) {
               outOfBounds = true;
          }

          // Checks if the current position has gone out of bounds on the Y-axis
          if((screenPosition.getY() >= Game.HEIGHT) || (screenPosition.getY() <= 0)) {
               outOfBounds = true;
          }

          // Checks if the previous position has gone out of bounds on the X-axis
          if((prevScreenPosition.getX() >= Game.WIDTH) || (prevScreenPosition.getX() <= 0)) {
               outOfBounds = true;
          }

          // Checks if the previous position has gone out of bounds on the Y-axis
          if((prevScreenPosition.getY() >= Game.HEIGHT) || (prevScreenPosition.getY() <= 0)) {
               outOfBounds = true;
          }

          return outOfBounds;
     }

     // Checks if the virtual ray has collided with a collider
     public RayHit checkOpticalObjectCollision() {
          // The ray hasn't moved yet
          if(currentPosition.equals(prevPosition)) {
               return null;
          }

          Scene currentScene = SceneManager.getCurrentScene();
          // Gets the list of optical objects in that scene
          OpticalObject[] opticalObjects = currentScene.getSceneOpticalObjects();

          RayData currentRayData = new RayData(prevPosition, currentPosition);
          this.rayData = currentRayData;

          // Checks for collision between all optical objects
          for(OpticalObject opticalObject : opticalObjects) {
               CollisionData collisionData = opticalObject.getCollider().collideWithRay(this);

               if((collisionData != null) && (opticalObject != objectOfAvoidance)) {
                    RayHit rayHit = new RayHit(true, collisionData, opticalObject, CollisionType.OPTICAL_COLLISION);

                    // The ray has hit something
                    return rayHit;
               }
          }
          
          return null;
     }

     @Override
     public RayData getRayData() {
          return rayData;
     }

     @Override
     public String toString() {
          return rayData.toString() + "\nLength: " + LENGTH;
     }
}
