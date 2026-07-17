package main.physics.rays;

import main.game.Game;
import main.math.Vector2;
import main.models.RayData;
import main.physics.colliders.CollisionType;

// Virtual rays used for raycasting
public class VirtualRay implements Ray {
     private RayData rayData;
     public static final float LENGTH = 2f;

     private Vector2 currentPosition;
     
     // Overloaded constructors
     public VirtualRay(Vector2 origin, Vector2 direction) {
          rayData = new RayData();
          rayData.setFromDirection(origin, direction.getAngle(), LENGTH);
          this.currentPosition = origin;
     }

     public VirtualRay(Vector2 origin, float angle) {
          rayData = new RayData();
          rayData.setFromDirection(origin, angle, LENGTH);
          this.currentPosition = origin;
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
               boolean hit = hasCollidedWithCollider();

               if(out || hit) {
                    collision = true;

                    if(out) {
                         rayHit = new RayHit(true, currentPosition, CollisionType.BOUNDS_COLLISION);
                    }
                    
                    if(hit) {
                         // Code
                    }
               }

               Vector2 velocity = rayData.getNormalizedDirection().multiply(LENGTH);
               // Updates ray position
               currentPosition = currentPosition.add(velocity);
          }

          return rayHit;
     }

     // Checks if the virtaul ray has gone out of bounds
     public boolean isOutOfBounds() {
          boolean outOfBounds = false;

          // Checks if the ray has gone out of bounds on the X-axis
          if((currentPosition.getX() >= Game.WIDTH) || (currentPosition.getX() <= 0)) {
               outOfBounds = true;
          }
          // Checks if the ray has gone out of bounds on the Y-axis
          if((currentPosition.getY() >= Game.HEIGHT) || (currentPosition.getY() <= 0)) {
               outOfBounds = true;
          }

          return outOfBounds;
     }

     // Checks if the virtual ray has collided with a collider
     public boolean hasCollidedWithCollider() {
          // Code
          return false;
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
