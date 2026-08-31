package main.models.environment.refractors;

import java.awt.Graphics2D;

import main.game.ShapeRender;
import main.math.algebra.Vector2;
import main.math.shapes.Shape;
import main.models.data.IntersectionData;
import main.models.data.RayData;
import main.models.data.SurfaceData;
import main.models.entities.LightRay;
import main.models.environment.OpticalObject;
import main.physics.colliders.Collider;
import main.physics.optics.Medium;
import main.physics.optics.OpticalObjectType;
import main.physics.rays.RayHit;
import main.physics.rays.VirtualRay;

// Main refractor class
public abstract class Refractor extends OpticalObject {
     private final Medium medium;
     private final Shape shape;

     // Constructors
     public Refractor(Vector2 position, Collider collider, Medium medium, Shape shape) {
          super(position, collider, OpticalObjectType.REFRACTOR);
          this.medium = medium;
          this.shape = shape;
     }

     public Refractor(String name, Vector2 position, Collider collider, Medium medium, Shape shape) {
          super(name, position, collider, OpticalObjectType.REFRACTOR);
          this.medium = medium;
          this.shape = shape;
     }

     // Getters

     public Medium getMedium() {
          return medium;
     }

     public Shape getShape() {
          return shape;
     }

     public float getRefractiveIndex() {
          return medium.getRefractiveIndex();
     }

     @Override
     protected LightRay interactWithRay(LightRay ray, IntersectionData intersectionData) {
          return this.refract(ray, intersectionData);
     }
     
     protected LightRay refract(LightRay ray, IntersectionData intersectionData) {
          LightRay newRay;

          // Incident ray's unit vector
          Vector2 incidentVector = ray.getRayData().getNormalizedDirection();

          SurfaceData surfaceData = calculateSurfaceData(intersectionData);

          Vector2 normal = surfaceData.getNormal();
          Vector2 tangent = surfaceData.getTangent();

          Medium nextMedium;
          // If the ray is passing from air, the new medium is the object medium
          if(ray.getCurrentMedium() == Medium.AIR) nextMedium = this.medium;
          else {
               nextMedium = getNextMedium(
                              intersectionData.getIntersectionPoint(),
                              normal.multiply(-1f)
                         );
          };

          // RI of current medium
          float n1 = ray.getCurrentMedium().getRefractiveIndex();
          // RI of the refraction medium
          float n2 = nextMedium.getRefractiveIndex();
          // Ratio of the refractive indices
          float ratio = n2 / n1;

          float angleOfIncidence = incidentVector.getAngleBetween(normal);
          float sinIncident = (float) Math.sin(angleOfIncidence);

          // Decomposes the incident ray into it's perpendicular and horizontal components along the surface
          Vector2 incidentAlongNormal = incidentVector.projectOnto(normal);
          Vector2 incidentAlongTangent = incidentVector.projectOnto(tangent);

          /* 
               The incident ray goes through TIR(Total Internal Reflection)
               if:
                    sin(θᵢ) > μ₂ / μ₁
               AND
                    μ₁ > μ₂
          */
          if((n1 > n2) && (sinIncident > ratio)) {
               // Reflect instead of refracting

               Vector2 reflectionVector = incidentAlongTangent.add(incidentAlongNormal.multiply(-1f));
          
               Vector2 reflectionTip = intersectionData.getIntersectionPoint().add(reflectionVector);
               // The tail is offset by a tiny bit (to account for precision errros)
               Vector2 reflectionTail = intersectionData.getIntersectionPoint().add(reflectionVector.multiply(0.01f));
          
               RayData data = new RayData(reflectionTail, reflectionTip);
               newRay = new LightRay(data);

               return newRay;
          }

          /* 
               According to snell's law, for a ray going from medium 1 to medium 2:
                    μ₁sin(θᵢ) = μ₂sin(θᵣ)
               
               Solving for the angle of refraction gives us:
                    θᵣ = sin⁻¹[μ₁sin(θᵢ) / μ₂]
          */
          float sinRefracted = (n1 * sinIncident) / n2;
          // Clamps the value between -1 & 1
          sinRefracted = Math.clamp(sinRefracted, -1f, 1f);
          
          float angleOfRefraction = (float) Math.asin(sinRefracted);

          /* 
               Since the refracted vector will be a unit vector,
               the length of it's perpendicular & horizontal components
               along surface will be respectively:

                    |p| = cos(θᵣ)
                    |b| = sin(θᵣ)

               Assuming n & t are the unit projₛ(i) vectors (s = surface || normal & tangent), the refracted vector
               will be the linear combination of n & t where:
                    r = |b|t + |p|n
                or, r = sin(θᵣ) * t + cos(θᵣ) * n
          */
          Vector2 normalizedIncidentAlongNormal = incidentAlongNormal.getNormalized();
          Vector2 normalizedIncidentAlongTangent = incidentAlongTangent.getNormalized();

          Vector2 refractedAlongNormal = normalizedIncidentAlongNormal.multiply((float) Math.cos(angleOfRefraction));
          Vector2 refractedAlongTangent = normalizedIncidentAlongTangent.multiply((float) Math.sin(angleOfRefraction));
          
          // Calculates the refracted ray vector
          Vector2 refractedVector = refractedAlongNormal.add(refractedAlongTangent);

          Vector2 refractionTip = intersectionData.getIntersectionPoint().add(refractedVector);
          // The tail is offset by a tiny bit (to account for precision errros)
          Vector2 refractionTail = intersectionData.getIntersectionPoint().add(refractedVector.multiply(0.01f));

          RayData data = new RayData(refractionTail, refractionTip);
          newRay = new LightRay(data);
          // Sets the new medium as the current one
          newRay.setCurrentMedium(nextMedium);

          return newRay;
     }

     // Calculates the next medium of travel
     private Medium getNextMedium(Vector2 intersectionPoint, Vector2 normal) {
          // Starts a little back
          Vector2 testStart = intersectionPoint.subtract(normal.multiply(0.001f));
          // Creates a new virtual ray of a small length
          VirtualRay vRay = new VirtualRay(testStart, normal, 0.5f);
          // Continuous cast
          RayHit hit = vRay.cast();

          // If the ray hit nothing, or hit the object itself, it is about to exit
          if((hit == null) || hit.getTargetObject() == this) return Medium.AIR;

          // If it hits any other refractor, return it's medium
          if(hit.getTargetObject() instanceof Refractor refractor) {
               return refractor.getMedium();
          }

          return Medium.AIR;
     }

     @Override
     public void render(Graphics2D g) {
          super.render(g);

          // Draws the shape to the screen
          ShapeRender.draw(
               g,
               shape,
               position,
               collider.getRotation(),
               medium.getColor()
          );
     }
}
