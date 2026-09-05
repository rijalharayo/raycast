package main.models.environment.refractors.glasses.lens;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Arc2D;
import java.awt.geom.Path2D;

import main.game.Scene;
import main.math.algebra.Vector2;
import main.math.shapes.Arc;
import main.math.shapes.polygons.Polygon;
import main.models.data.IntersectionData;
import main.models.data.SurfaceData;

// Converging lens
public class ConvexLens extends Lens {
     // Constructors
     public ConvexLens(Vector2 position, float radiusOfCurvature, float centerThickness, float rotation) {
          super(
               position,
               createShape(radiusOfCurvature, centerThickness, 0f), // Aperture diameter is not required
               radiusOfCurvature,
               centerThickness,
               calculateApertureDiameter(radiusOfCurvature, centerThickness),
               calculateArcAngle(radiusOfCurvature, centerThickness, 0f), // Aperture diameter is not required
               rotation
          );
     }

     public ConvexLens(float x, float y, float radiusOfCurvature, float centerThickness, float rotation) {
          Vector2 position = new Vector2(x, y);
          
          super(
               position,
               createShape(radiusOfCurvature, centerThickness, 0f), // Aperture diameter is not required
               radiusOfCurvature,
               centerThickness,
               calculateApertureDiameter(radiusOfCurvature, centerThickness),
               calculateArcAngle(radiusOfCurvature, centerThickness, 0f), // Aperture diameter is not required
               rotation
          );
     }

     // Calculates the aperture diameter of the lens from the radius of curvature and thickness
     private static float calculateApertureDiameter(float radiusOfCurvature, float centerThickness) {
          float r = radiusOfCurvature;
          float w = centerThickness;
          float R = r + (w / 2);

          return 2 * (float) Math.sqrt((R * R) - (r * r));
     }

     // Hides parent static calculateArcAngle method
     // Aperture diameter is only included here to properly hide the parent's static method
     protected static float calculateArcAngle(float radiusOfCurvature, float centerThickness, float apertureDiameter) {
          /**
               A convex lens can be constructed from the intersecting regions of two circles.

               Assuming the lens has a radius of curvature 'r' and a center thickness 'w',
               the radius of each circle is:

                    R = r + (w / 2)

               The angle of the arc forming the lens surface can then be determined from
               the geometry of the intersecting circles:

                    θ = π - 2sin⁻¹(r / R)

               The aperture diameter is useless here.
          */

          float r = radiusOfCurvature;
          float R = radiusOfCurvature + (centerThickness / 2);

          // Ratio of the two radii
          float sinRatio = r / R;
          // Clamps the value between -1 and +1
          sinRatio = Math.clamp(sinRatio, -1f, 1f);

          // sin⁻¹(r / R)
          float phi = (float) Math.asin(sinRatio);
          // Calculates and returns the angle of the arc 
          return (float) Math.PI - 2 * phi;
     }

     // Hides parent static createShape method
     // Aperture diameter is only included here to properly hide the parent's static method
     protected static Polygon createShape(float radiusOfCurvature, float centerThickness, float appertureDiameter) {
          // Calculates the required angle
          float theta = calculateArcAngle(radiusOfCurvature, centerThickness, 0f); // Aperture diameter is not required for convex lens

          float r = radiusOfCurvature;
          float R = radiusOfCurvature + (centerThickness / 2);

          // Creates the two opposing lens surfaces
          Arc arc1 = new Arc(R, theta, 0f);
          Arc arc2 = new Arc(R, theta, 0f, (float) Math.PI);

          // Gets vertices relative to each arc's circle center
          Vector2[] localVertices1 = arc1.getApproximatedPolygon().getLocalVertices();
          Vector2[] localVertices2 = arc2.getApproximatedPolygon().getLocalVertices();

          // Positions each circle center relative to the optical center
          Vector2 center1Offset = new Vector2(-r, 0);
          Vector2 center2Offset = new Vector2(r, 0);

          // Combines both arcs while removing their two shared vertices
          Vector2[] vertices = new Vector2[localVertices1.length + localVertices2.length - 2];

          int index = 0;
          
          // First arc
          for (int i = 0; i < localVertices1.length; i++) {
               vertices[index++] = localVertices1[i].add(center1Offset);
          }

          // Second arc, excluding both shared endpoints
          for (int i = 1; i < localVertices2.length - 1; i++) {
               vertices[index++] = localVertices2[i].add(center2Offset);
          }
          
          return new Polygon(vertices);
     }

     @Override
     public SurfaceData calculateSurfaceData(IntersectionData intersectionData) {

          // Calculate the centers of curvature for both faces
          Vector2 leftFaceCenter = getLeftFaceCenterOfCurvature();
          Vector2 rightFaceCenter = getRightFaceCenterOfCurvature();

          Vector2 intersectionPoint = intersectionData.getIntersectionPoint();
          
          // Incoming line vector
          Vector2 incidentVector = intersectionData.getIncomningLine().getLineVector();

          // Distance from each center of curvature to the intersection point
          float distanceFromLeftFaceCenter = intersectionPoint.distance(leftFaceCenter);
          float distanceFromRightFaceCenter = intersectionPoint.distance(rightFaceCenter);

          // Fallback normal
          Vector2 normal = Vector2.UP;

          // The intersection belongs to the left surface if it is closer to the right center of curvature
          if(distanceFromLeftFaceCenter > distanceFromRightFaceCenter) {
               normal = intersectionPoint.subtract(leftFaceCenter).getNormalized();
          }
          // Otherwise, the intersection belongs to the right surface
          else if(distanceFromRightFaceCenter > distanceFromLeftFaceCenter) {
               normal = intersectionPoint.subtract(rightFaceCenter).getNormalized();
          }

          // Ensures the normal faces against the incoming ray
          if(normal.dot(incidentVector.getNormalized()) > 0) {
               normal = normal.multiply(-1f);
          }

          return new SurfaceData(intersectionData.getTargetLine().getNormalizedDirection(), normal);
     }

     // Draws the shape to be rendered
     @Override
     protected void drawShape(Graphics2D g) {
          float r = radiusOfCurvature;
          float w = centerThickness;
          float R = r + (w / 2);
          float theta = arcAngle;
          float rotation = collider.getRotation();

          Color oldColor = g.getColor();
          
          g.setColor(getMedium().getColor());

          // Calculate the two circle centers
          // The center of curvature of each convex surface lies behind that surface
          Vector2 leftFaceCenter = getLeftFaceCenterOfCurvature();
          Vector2 rightFaceCenter = getRightFaceCenterOfCurvature();

          Vector2 leftScreenCenter = Scene.worldToScreen(leftFaceCenter);
          Vector2 rightScreenCenter = Scene.worldToScreen(rightFaceCenter);

          float leftCx = (float) leftScreenCenter.getX();
          float leftCy = (float) leftScreenCenter.getY();

          float rightCx = (float) rightScreenCenter.getX();
          float rightCy = (float) rightScreenCenter.getY();

          // Starts the left surface at the upper endpoint
          // The left surface lies on the opposite side of its circle, so its angle is offset by PI
          float leftStartAngle =
               (float) Math.toDegrees(
                    rotation + Math.PI - (theta / 2)
               );

          // Starts the right surface at the lower endpoint
          // The right surface is centered around the lens rotation direction
          float rightStartAngle =
               (float) Math.toDegrees(
                    rotation - (theta / 2)
               );

          // Left convex surface
          Arc2D.Float leftSurface =
               new Arc2D.Float(
                    leftCx - R,
                    leftCy - R,
                    R * 2,
                    R * 2,
                    leftStartAngle,
                    (float) Math.toDegrees(theta),
                    Arc2D.OPEN
               );

          // Right convex surface
          Arc2D.Float rightSurface =
               new Arc2D.Float(
                    rightCx - R,
                    rightCy - R,
                    R * 2,
                    R * 2,
                    rightStartAngle,
                    (float) Math.toDegrees(theta),
                    Arc2D.OPEN
               );

          Path2D.Float lensShape = new Path2D.Float();

          // Left surface: upper endpoint → lower endpoint
          lensShape.append(leftSurface, true);

          // Right surface: lower endpoint → upper endpoint
          lensShape.append(rightSurface, false);

          // Connect the two upper endpoints
          lensShape.closePath();

          g.fill(lensShape);
          g.setColor(oldColor);
     }
}