package main.physics.optics;

import main.models.data.RayData;
import main.physics.colliders.CollisionData;
import main.physics.rays.Ray;

// Implemented by the objects a ray can interact with
public interface RayInteractable {
     // Interacts with the incoming ray
     public RayData interact(Ray ray, CollisionData collisionData);
}