package main.models.environment.reflectors.mirrors;

import main.math.algebra.Matrix2x2;
import main.math.algebra.Vector2;
import main.math.shapes.Circle;
import main.models.Sprite;
import main.models.data.IntersectionData;
import main.models.data.SurfaceData;
import main.physics.colliders.CircleCollider;

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
     public SurfaceData calculateSurfaceData(IntersectionData intersectionData) {
          Vector2 circleCenter = this.position;
          Vector2 intersectionPoint = intersectionData.getIntersectionPoint();

          /* 
               The radius drawn from the circle to the tangent is perpendicular to the tangent.
               
               Since the intersection point lies on the tanget, & also on the circumference
               of the circle, it's at a distance of radius 'r' from the center of the
               circle.

               So the vector from the centre to the intersection-point will be the normal.
               The same vector rotated 90deg will be the tangent line itself.
          */

          Vector2 normal = intersectionPoint.subtract(circleCenter);
          Vector2 tangent = Matrix2x2.ROTATE_ANTI_CLOCKWISE_90.transform(normal);

          return new SurfaceData(tangent, normal);
     }
}