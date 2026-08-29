package main.models.environment.refractors.glasses;

import main.math.algebra.Vector2;
import main.math.shapes.Circle;
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
}
