package main.physics.rays.reflectors;

import main.models.RayData;
import main.physics.rays.Ray;

// Implemented by the objects a ray can interact with
public interface RayInteractable {
     // Interacts with the incoming ray
     public RayData interact(Ray ray);
}