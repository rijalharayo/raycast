package main.models.environment.reflectors.mirrors;

import main.math.algebra.Vector2;
import main.models.IntersectionData;
import main.models.RayData;
import main.models.SurfaceData;
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
     protected RayData reflect(Ray ray, IntersectionData intersectionData) {
          // Calculates the mirror's relative surface data
          SurfaceData surfaceData = calculateSurfaceData(intersectionData);

          Vector2 mirrorNormal = surfaceData.getNormal();
          Vector2 mirrorSurface = surfaceData.getTangent();

          // Incident ray
          Vector2 incidentVector = intersectionData.getIncomningLine().getLineVector();
          
          // Projects the incident vector onto the normal
          Vector2 incidentOnMirrorNormal = incidentVector.projectOnto(mirrorNormal);
          Vector2 incidentOnMirrorSurface = incidentVector.projectOnto(mirrorSurface);

          Vector2 reflectionVector = incidentOnMirrorSurface.add(incidentOnMirrorNormal.multiply(-1f));
          
          Vector2 reflectionTip = intersectionData.getIntersectionPoint().add(reflectionVector);
          // The tail is offset by a tiny bit (to account for precision errros)
          Vector2 reflectionTail = intersectionData.getIntersectionPoint().add(reflectionVector.multiply(0.01f));

          return new RayData(reflectionTail, reflectionTip);
     }

     // Abstract methods

     // Calculates the surface data of the mirror based on intersection data
     public abstract SurfaceData calculateSurfaceData(IntersectionData intersectionData);
}
