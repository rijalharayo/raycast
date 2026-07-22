package main.models.environment.reflectors.mirrors;

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
          Vector2 mirrorSurface = intersectionData.getTargetLine().getLineVector();
          Vector2 mirrorNormal = intersectionData.getTargetLine().getNormal();
          // Incident ray
          Vector2 incidentVector = intersectionData.getIncomningLine().getLineVector();

          // If the incident ray & normal face the same direction, invert it
          if(incidentVector.dot(mirrorNormal) > 0) {
               mirrorNormal = mirrorNormal.multiply(-1f);
          }
          
          // Projects the incident vector onto the normal
          Vector2 incidentOnMirrorNormal = incidentVector.projectOnto(mirrorNormal);
          Vector2 incidentOnMirrorSurface = incidentVector.projectOnto(mirrorSurface);

          Vector2 reflectionVector = incidentOnMirrorSurface.add(incidentOnMirrorNormal.multiply(-1f));
          
          Vector2 reflectionTip = intersectionData.getIntersectionPoint().add(reflectionVector);
          // The tail is offset by a tiny bit (to account for precision errros)
          Vector2 reflectionTail = intersectionData.getIntersectionPoint().add(reflectionVector.multiply(0.01f));

          return new RayData(reflectionTail, reflectionTip);
     }
}