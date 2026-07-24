package main.models.environment.reflectors.mirrors;

import main.math.algebra.Vector2;
import main.math.shapes.Circle;
import main.models.IntersectionData;
import main.models.RayData;
import main.models.Sprite;
import main.physics.colliders.CircleCollider;
import main.physics.rays.Ray;

// Represents a completely circular mirror
public class CircularMirror extends Mirror {
     // Constructors
     public CircularMirror(Vector2 position, float radius) {
          super(position, new CircleCollider(position, radius));
          
          Sprite sprite = new Sprite("circular-mirror.png");

          sprite.setWidth((int) (2 * radius));
          sprite.setHeight((int) (2 * radius));

          this.setSprite(sprite);
     }

     public CircularMirror(float x, float y, float radius) {
          super(new Vector2(x, y), new CircleCollider(new Vector2(x, y), radius));

          Sprite sprite = new Sprite("circular-mirror.png");

          sprite.setWidth((int) (2 * radius));
          sprite.setHeight((int) (2 * radius));

          this.setSprite(sprite);
     }

     // Getters
     public float getRadius() {
          Circle colliderShape = (Circle) this.getCollider().getShape();
          return colliderShape.getRadius();
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