package main.models.environment.refractors.glasses;

import main.math.algebra.Vector2;
import main.math.shapes.polygons.Rectangle;
import main.models.environment.refractors.Refractor;
import main.physics.colliders.BoxCollider;
import main.physics.optics.Medium;

// A solid glass block
public class GlassBlock extends Refractor {
     // Constructors
     public GlassBlock(Vector2 position, int width, int height, float rotation) {
          super(
               position,
               new BoxCollider(position, width, height, rotation),
               Medium.GLASS,
               new Rectangle(width, height, (float) Math.toRadians(rotation))
          );
     }

     public GlassBlock(float x, float y, int width, int height, float rotation) {
          Vector2 pos = new Vector2(x, y);

          super(
               pos,
               new BoxCollider(pos, width, height, rotation),
               Medium.GLASS,
               new Rectangle(width, height, (float) Math.toRadians(rotation))
          );
     }
}
