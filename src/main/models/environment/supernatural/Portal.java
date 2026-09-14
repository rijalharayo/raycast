package main.models.environment.supernatural;

import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;

import main.game.Scene;
import main.game.ShapeRender;
import main.math.Line;
import main.math.algebra.Matrix2x2;
import main.math.algebra.Vector2;
import main.math.shapes.polygons.Rectangle;
import main.models.data.IntersectionData;
import main.models.entities.LightRay;
import main.models.environment.OpticalObject;
import main.physics.colliders.BoxCollider;
import main.physics.optics.OpticalObjectType;

// Portal that let's light emerge out of another place
public class Portal extends OpticalObject {
     // Rendering shape
     private Rectangle portalShape;
     // The linked counterpart
     private Portal linkedPortal;
     // The matrix representing the portal coordinates
     private Matrix2x2 portalMatrix;

     // Portal colors (for rendering)
     private static final Color PORTAL_COLOR = new Color(23, 22, 22);

     private static final Color GLOW_COLOR_1 = new Color(100, 200, 255, 60);
     private static final Color GLOW_COLOR_2 = new Color(100, 200, 255, 35);
     private static final Color GLOW_COLOR_3 = new Color(100, 200, 255, 15);

     private static final Stroke GLOW_STROKE_1 = new BasicStroke(4f);
     private static final Stroke GLOW_STROKE_2 = new BasicStroke(8f);
     private static final Stroke GLOW_STROKE_3 = new BasicStroke(14f);

     // Constructors
     public Portal(Vector2 position, int width, int height, float rotation) {
          super(
               position,
               new BoxCollider(position, width, height, rotation),
               OpticalObjectType.SUPER_NATURAL
          );

          this.portalShape = (Rectangle) this.collider.getShape();
          initializePortalMatrix();
     }

     public Portal(float x, float y, int width, int height, float rotation) {
          Vector2 position = new Vector2(x, y);

          super(
               position,
               new BoxCollider(position, width, height, rotation),
               OpticalObjectType.SUPER_NATURAL
          );

          this.portalShape = (Rectangle) this.collider.getShape();
          initializePortalMatrix();
     }

     // Setters
     public void setLinkedPortal(Portal otherPortal) {
          this.linkedPortal = otherPortal;
          otherPortal.linkedPortal = this;
     }

     // Initializes the portal's transformation matrix
     private void initializePortalMatrix() {
          float rotation = this.getCollider().getRotation();
          int width = portalShape.getWidth();
          int height = portalShape.getHeight();

          /* 
               Given a portal of width & height 'w' & 'h' respectively, the local axes
               of the portal will be:

                    P = [w/2  0]
                        [0  h/2]

               If the portal is rotated by an angle θ, the the new local axes in cartesian coordinates
               will be:
                    P' = R(θ) ⋅ P
          */

          Matrix2x2 rotationMatrix = Matrix2x2.getRotationMatrix(rotation);

          // Defines the portal's local axes scaled by its width and height
          double[][] mArr = {{width / 2, 0},
                             {0, height / 2}};

          this.portalMatrix = new Matrix2x2(mArr);
          // Rotates the scaled axes into the portal's orientation
          this.portalMatrix = rotationMatrix.multiply(portalMatrix);
     }

     @Override
     protected LightRay interactWithRay(LightRay ray, IntersectionData intersectionData) {
          return teleportRay(ray, intersectionData);
     }

     private LightRay teleportRay(LightRay ray, IntersectionData intersectionData) {
          // Just absorb the ray if no portal is linked
          if(linkedPortal == null) return null;

          Vector2 intersectionPoint = intersectionData.getIntersectionPoint();
          Vector2 relativeIntersectionPoint = intersectionPoint.subtract(position);
          // Direction of the ray
          Vector2 incidentVector = intersectionData.getIncomningLine().getNormalizedDirection();

          /*
               Given a point/vector 'v' in cartesian coordinates <a, b>,
               its coordinates relative to the coordinate system R' are:

                    v₁ = M₁⁻¹v

               where M₁ is the matrix representing R' in cartesian coordinates.

               To express the same point relative to another coordinate system
               represented by M₂, we can transform v₁ using M₂:

                    v₂ = M₂v₁

               Therefore:

                    v₂ = M₂M₁⁻¹v

               Thus, the combined matrix M₂M₁⁻¹ represents the portal transform.

               However, when a ray passes through a portal, the intersection point
               must first be reflected across the tangent of the portal surface.
               This maps the point to the corresponding position on the opposite
               side of the portal before applying the portal transformation.

               The reflection is represented by the matrix Fᵣ:

                    v' = Fᵣv

               Therefore:

                    v₁ = M₁⁻¹v'
                or, v₁ = M₁⁻¹Fᵣv

                
               The reflection is therefore applied before the portal transformation:

                    v₂ = M₂M₁⁻¹Fᵣv

               This reflection is only required for the intersection point, since
               the point must be moved to the corresponding side of the portal.

               The ray direction, however, does not need this reflection.
               Its orientation is already determined by the relative orientations
               of the two portals, so it is transformed directly between their
               coordinate systems. Assuming 'd' is the original direction:

                    d₂ = M₂M₁⁻¹d

               Thus, the complete position transformation is:

                    P = M₂M₁⁻¹Fᵣ

               while the direction transformation is:

                    D = M₂M₁⁻¹
          */

          Line tangent = intersectionData.getTargetLine();    
          // Reflection matrix (Fᵣ)
          Matrix2x2 reflectionMatrix = Matrix2x2.getReflectionMatrix(tangent);

          // Inverse of the portal matrix (M₁⁻¹)
          Matrix2x2 portalInverse = this.portalMatrix.getInverse();
          // M₂M₁⁻¹
          Matrix2x2 portalTransformMatrix = linkedPortal.portalMatrix.multiply(portalInverse);
          // M₂M₁⁻¹Fᵣ
          Matrix2x2 pointTransformMatrix = portalTransformMatrix.multiply(reflectionMatrix);

          // Calculates the local ray emergent point & converts it to world co-ordinates
          Vector2 relativeEmergentPoint = pointTransformMatrix.transform(relativeIntersectionPoint);
          Vector2 worldEmergentPoint = linkedPortal.position.add(relativeEmergentPoint);
          
          // The direction only gets transformed by M₂M₁⁻¹ instead of M₂M₁⁻¹Fᵣ
          Vector2 newRayDirection = portalTransformMatrix.transform(incidentVector);
          newRayDirection = newRayDirection.getNormalized();

          // Offset ray start to prevent precision errors
          worldEmergentPoint = worldEmergentPoint.add(newRayDirection.multiply(0.1f));

          // Calculates a temporary endpoint
          Vector2 rayEnd = worldEmergentPoint.add(newRayDirection);
          
          // Returns the new light ray
          return new LightRay(worldEmergentPoint, rayEnd);
     }

     @Override
     public void render(Graphics2D g) {
          Vector2 screenPosition = Scene.worldToScreen(getPosition());
          float rotation = getCollider().getRotation();

          AffineTransform oldTransform = g.getTransform();
          Color oldColor = g.getColor();
          Stroke oldStroke = g.getStroke();

          // Draws the portal glow
          g.rotate(
               -rotation,
               screenPosition.getX(),
               screenPosition.getY()
          );

          g.setColor(GLOW_COLOR_3);
          g.setStroke(GLOW_STROKE_3);
          g.drawRect(
               (int) (screenPosition.getX() - portalShape.getWidth() / 2f),
               (int) (screenPosition.getY() - portalShape.getHeight() / 2f),
               portalShape.getWidth(),
               portalShape.getHeight()
          );

          g.setColor(GLOW_COLOR_2);
          g.setStroke(GLOW_STROKE_2);
          g.drawRect(
               (int) (screenPosition.getX() - portalShape.getWidth() / 2f),
               (int) (screenPosition.getY() - portalShape.getHeight() / 2f),
               portalShape.getWidth(),
               portalShape.getHeight()
          );

          g.setColor(GLOW_COLOR_1);
          g.setStroke(GLOW_STROKE_1);
          g.drawRect(
               (int) (screenPosition.getX() - portalShape.getWidth() / 2f),
               (int) (screenPosition.getY() - portalShape.getHeight() / 2f),
               portalShape.getWidth(),
               portalShape.getHeight()
          );

          g.setTransform(oldTransform);

          // Draws the portal body
          ShapeRender.draw(
               g,
               portalShape,
               getPosition(),
               rotation,
               PORTAL_COLOR
          );

          g.setColor(oldColor);
          g.setStroke(oldStroke);

          super.render(g);
     }
}
