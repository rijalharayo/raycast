package main.physics.rays.reflectors;

import main.physics.rays.Ray;
import main.physics.rays.RayHit;

// Implemented by the objects a ray can interact with
public interface RayInteractable {
     // Interacts with the incoming ray
     public RayHit interact(Ray ray);
}