package main.physics.optics;

import main.models.data.IntersectionData;
import main.models.data.RayData;
import main.models.data.SurfaceData;
import main.models.entities.LightRay;
import main.physics.colliders.CollisionData;

// Implemented by the objects a ray can interact with
public interface RayInteractable {
     // Interacts with the incoming ray
     public RayData interact(LightRay ray, CollisionData collisionData);
     // Returns the surface data of the surface
     public SurfaceData calculateSurfaceData(IntersectionData intersectionData);
}