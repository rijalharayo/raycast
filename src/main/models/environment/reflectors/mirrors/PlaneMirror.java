package main.models.environment.reflectors.mirrors;

import java.awt.Graphics2D;

import main.math.Vector2;
import main.models.IntersectionData;
import main.models.RayData;
import main.models.Sprite;
import main.physics.colliders.BoxCollider;
import main.physics.rays.Ray;

// Represents a Plane reflective mirror
public class PlaneMirror extends Mirror {

     // Constructors
     public PlaneMirror(float x, float y, int width, int height, float rotation) {
          super(
               "Plane mirror",
               new Vector2(x, y),
               new BoxCollider(new Vector2(x, y), width, height, rotation)
          );

          // Sets sprite
          Sprite sprite = new Sprite("plane-mirror.png");
          this.setSprite(sprite);
          // Sets the height & width of the sprite
          sprite.setWidth(width);
          sprite.setHeight(height);

          this.setRotation(rotation);
     }

     public PlaneMirror(Vector2 position, int width, int height, float rotation) {
          super(
               "Plane mirror",
               position,
               new BoxCollider(position, width, height, rotation)
          );
          // Sets sprite
          Sprite sprite = new Sprite("plane-mirror.png");
          this.setSprite(sprite);
          // Sets the height & width of the sprite
          sprite.setWidth(width);
          sprite.setHeight(height);

          this.setRotation(rotation);
     }

     @Override
     protected RayData reflect(Ray ray, IntersectionData intersectionData) {
          return new RayData(intersectionData.getIntersectionPoint(), Vector2.LEFT);
     }
     

     @Override
     public void render(Graphics2D g) {
          // Renders the mirror sprite
          this.sprite.draw(
               g,
               position.getX(),
               position.getY()
          );

          getCollider().draw(g);
     }
}
