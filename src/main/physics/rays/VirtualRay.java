package main.physics.rays;

import main.game.Game;
import main.game.Scene;
import main.game.SceneManager;
import main.math.Vector2;
import main.models.RayData;
import main.models.environment.OpticalObject;
import main.physics.colliders.CollisionType;

// Virtual rays used for raycasting
public class VirtualRay implements Ray {
     private RayData rayData;
     public static final float LENGTH = 2f;

     private Vector2 currentPosition;
     private Vector2 prevPosition;
     
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

     // Getters
     public Vector2 getCurrentPosition() {
          return currentPosition;
     }

     // Casts the ray in the direction until collision
     public RayHit cast() {
          boolean collision = false;

          RayHit rayHit = null;

          // Run until it collides
          while(!collision) {
               boolean out = isOutOfBounds();
               RayData newRayData = checkOpticalObjectCollision();
               boolean hit = newRayData != null;

               if(out || hit) {
                    collision = true;

                    if(out) {
                         rayHit = new RayHit(true, currentPosition, CollisionType.BOUNDS_COLLISION);
                    }
                    
                    if(hit) {
                         rayHit = new RayHit(true, newRayData.getStart(), CollisionType.MIRROR_COLLISION);
                         rayHit.setNextRayData(newRayData);
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
     public RayData checkOpticalObjectCollision() {
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
               RayData newRayData = opticalObject.interact(this);
               if(newRayData != null) {
                    // The ray has hit something
                    return newRayData;
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
