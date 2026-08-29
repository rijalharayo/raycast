package main.models.environment.refractors.glasses;

import main.math.algebra.Matrix2x2;
import main.math.algebra.Vector2;
import main.math.shapes.Circle;
import main.models.data.IntersectionData;
import main.models.data.SurfaceData;
import main.models.environment.refractors.Refractor;
import main.physics.colliders.CircleCollider;
import main.physics.optics.Medium;

// Represents a circular glass disc as a refractor
public class GlassDisc extends Refractor {
     // Constructors
     public GlassDisc(Vector2 position, float radius) {
          super(
               position,
               new CircleCollider(position, radius),
               Medium.FLINT_GLASS,
               new Circle(radius)
          );
     }

     public GlassDisc(float x, float y, float radius) {
          Vector2 pos = new Vector2(x, y);

          super(
               pos,
               new CircleCollider(pos, radius),
               Medium.FLINT_GLASS,
               new Circle(radius)
          );
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
