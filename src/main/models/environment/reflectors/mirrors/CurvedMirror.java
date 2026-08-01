package main.models.environment.reflectors.mirrors;

import java.awt.Graphics2D;

import main.math.algebra.Vector2;
import main.models.data.IntersectionData;
import main.models.data.SurfaceData;
import main.physics.colliders.ArcCollider;

// Represents a mirror in an arc shape
public class CurvedMirror extends Mirror {
     // Constructors
     public CurvedMirror(Vector2 position, float radius, float angle, float thickness) {
          super(position, 
               new ArcCollider(position, radius, angle, thickness)
          );
     }

     public CurvedMirror(float x, float y, float radius, float angle, float thickness) {
          super(new Vector2(x, y), 
               new ArcCollider(new Vector2(x, y), radius, angle, thickness)
          );
     }

     public CurvedMirror(Vector2 position, float radius, float angle, float thickness, float rotation) {
          super(position, 
               new ArcCollider(position, radius, angle, thickness) 
          );

          this.setRotation(rotation);
     }

     public CurvedMirror(float x, float y, float radius, float angle, float thickness, float rotation) {
          super(new Vector2(x, y), 
               new ArcCollider(new Vector2(x, y), radius, angle, thickness)
          );
          
          this.setRotation(angle);
     }

     @Override
     public SurfaceData calculateSurfaceData(IntersectionData intersectionData) {
          return null;
     }
     
     @Override
     public void render(Graphics2D g) {
          // Code
     }
}
