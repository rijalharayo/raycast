package main.models.environment.reflectors.mirrors;

import main.math.Vector2;
import main.models.IntersectionData;
import main.models.RayData;
import main.models.environment.reflectors.Reflector;
import main.physics.colliders.Collider;
import main.physics.rays.Ray;

public abstract class Mirror extends Reflector {
     // Constructors
     public Mirror(Vector2 position, Collider collider) {
          super(position, collider);
     }

     public Mirror(String name, Vector2 position, Collider collider) {
          super(name, position, collider);
     }

     @Override
     protected abstract RayData reflect(Ray ray, IntersectionData intersectionData);
}
