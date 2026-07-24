package main.models.data;

import main.math.algebra.Vector2;

// Represents the surface data of a object
public class SurfaceData {
     private final Vector2 normal;
     private final Vector2 tangent;

     public SurfaceData(Vector2 tangent, Vector2 normal) {
          this.normal = normal;
          this.tangent = tangent;
     }

     public Vector2 getNormal() {
          return normal;
     }

     public Vector2 getTangent() {
          return tangent;
     }
}
