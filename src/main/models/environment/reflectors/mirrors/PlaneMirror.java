package main.models.environment.reflectors.mirrors;

import main.math.algebra.Vector2;
import main.models.Sprite;
import main.physics.colliders.BoxCollider;

// Represents a Plane reflective mirror
public class PlaneMirror extends Mirror {

     // Constructors
     public PlaneMirror(float x, float y, int width, int height, float rotation) {
          super(
               "Plane mirror",
               new Vector2(x, y),
               new BoxCollider(new Vector2(x, y), width, height)
          );

          // Sets sprite
          Sprite sprite = new Sprite("plane-mirror.png");
          this.setSprite(sprite);
          // Sets the height & width of the sprite
          sprite.setWidth(width);
          sprite.setHeight(height);
          // Also set's collider rotation
          this.setRotation(rotation);
     }

     public PlaneMirror(Vector2 position, int width, int height, float rotation) {
          super(
               "Plane mirror",
               position,
               new BoxCollider(position, width, height)
          );
          // Sets sprite
          Sprite sprite = new Sprite("plane-mirror.png");
          this.setSprite(sprite);
          // Sets the height & width of the sprite
          sprite.setWidth(width);
          sprite.setHeight(height);

          this.setRotation(rotation);
     }
}