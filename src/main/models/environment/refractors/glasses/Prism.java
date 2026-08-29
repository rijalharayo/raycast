package main.models.environment.refractors.glasses;

import main.math.algebra.Vector2;
import main.math.shapes.polygons.RightTriangle;
import main.math.shapes.polygons.Triangle;
import main.models.environment.refractors.Refractor;
import main.physics.colliders.PolygonCollider;
import main.physics.optics.Medium;

// Represents a triangular glass prism
public class Prism extends Refractor {
     // Constructors
     public Prism(Vector2 position, float perpendicular, float base, float rotation) {
          RightTriangle rightTriangle = new RightTriangle(perpendicular, base);

          super(
               position,
               new PolygonCollider(position, rightTriangle, rotation),
               Medium.FLINT_GLASS,
               rightTriangle
          );
     }

     public Prism(float x, float y, float perpendicular, float base, float rotation) {
          RightTriangle rightTriangle = new RightTriangle(perpendicular, base);
          Vector2 pos = new Vector2(x, y);

          super(
               pos,
               new PolygonCollider(pos, rightTriangle, rotation),
               Medium.FLINT_GLASS,
               rightTriangle
          );
     }

     public Prism(float x, float y, Triangle triangle, float rotation) {
          Vector2 pos = new Vector2(x, y);
          
          super(
               pos,
               new PolygonCollider(pos, triangle, rotation),
               Medium.FLINT_GLASS,
               triangle
          );
     }
}
