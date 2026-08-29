package main.models.environment.absorbers;

import main.math.algebra.Vector2;
import main.math.shapes.polygons.Rectangle;
import main.models.data.IntersectionData;
import main.models.data.RayData;
import main.models.entities.LightRay;
import main.physics.colliders.BoxCollider;
import main.physics.optics.Material;

// Represents a completely black rectangular absorber
public class BlackAbsorber extends Absorber {
     // Constructors
     public BlackAbsorber(Vector2 position, int width, int height, float rotation) {
          super(
               position,
               new BoxCollider(position, width, height, rotation),
               Material.BLACK,
               new Rectangle(width, height)
          );
     }

     public BlackAbsorber(float x, float y, int width, int height, float rotation) {
          Vector2 pos = new Vector2(x, y);

          super(
               pos,
               new BoxCollider(pos, width, height, rotation),
               Material.BLACK,
               new Rectangle(width, height)
          );
     }

     @Override
     protected RayData absorb(LightRay ray, IntersectionData intersectionData) {
          // Completely absorbs light
          return null;
     }
}
