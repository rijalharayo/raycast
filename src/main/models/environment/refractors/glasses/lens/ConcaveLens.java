package main.models.environment.refractors.glasses.lens;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Arc2D;
import java.awt.geom.Path2D;

import main.game.Scene;
import main.math.Line;
import main.math.algebra.Vector2;
import main.math.shapes.Arc;
import main.math.shapes.polygons.Polygon;
import main.models.data.IntersectionData;
import main.models.data.SurfaceData;

// Diverging lens
public class ConcaveLens extends Lens {
     private Line[] flatEdges = new Line[2];

     // Constructors
     public ConcaveLens(Vector2 position, float radiusOfCurvature, float centerThickness, float apertureDiameter, float rotation) {
          super(
               position,
               createShape(radiusOfCurvature, centerThickness, apertureDiameter),
               radiusOfCurvature,
               centerThickness,
               apertureDiameter,
               calculateArcAngle(radiusOfCurvature, centerThickness, apertureDiameter),
               rotation
          );

          setFlatEdges();
     }

     public ConcaveLens(float x, float y, float radiusOfCurvature, float centerThickness, float apertureDiameter, float rotation) {
          Vector2 position = new Vector2(x, y);
          
          super(
               position,
               createShape(radiusOfCurvature, centerThickness, apertureDiameter),
               radiusOfCurvature,
               centerThickness,
               apertureDiameter,
               calculateArcAngle(radiusOfCurvature, centerThickness, apertureDiameter),
               rotation
          );

          setFlatEdges();
     }

     // Getters
     public Line[] getFlatEdges() {
          return flatEdges.clone();
     }

     // Sets the flat edges of the lens
     private void setFlatEdges() {
          Line[] worldEdges = getShape().getWorldEdges(position);

          /*
               For an concave lens formed of two arcs:
                    AB and CD
               where,
                    the vertex ordering is:
                         A -> B -> D -> C
          */

          int edgeCount = worldEdges.length;

          Line edgeAC = new Line(worldEdges[edgeCount - 1]);

          /*
               In the approximated polygon, the nuumber of edges on both arcs is equal.

               Assuming the number of edges is 'n', the total number of edges will be:
                    n(Edges) = 2n + 2
               where the (+2) includes the two flat edges AC & BD.

               Subtracting (n + 1) from n(Edges) gives us the index of the edge BD
               It is equivalent to n(Edges) / 2.
          */
          Line edgeBD = new Line(worldEdges[(edgeCount / 2) - 1]); // 1 is subtracted to account for the index starting from 0
     
          flatEdges[0] = edgeAC;
          flatEdges[1] = edgeBD;
     }

     @Override
     public SurfaceData calculateSurfaceData(IntersectionData intersectionData) {
          Line targetLine = intersectionData.getTargetLine();

          boolean isFlatEdge = false;

          // Fallback normal
          Vector2 normal = Vector2.UP;

          // Checks if the target line is one of the flat edge
          for(Line flatEdge : flatEdges) {
               if(flatEdge.isSameSegment(targetLine)) {
                    // If yes, just return the normal of the edge
                    normal = targetLine.getNormal().getNormalized();
                    isFlatEdge = true;
                    break;
               }
          }

          if(!isFlatEdge) {
               // Calculate the centers of curvature for both faces
               Vector2 leftFaceCenter = getLeftFaceCenterOfCurvature();
               Vector2 rightFaceCenter = getRightFaceCenterOfCurvature();

               Vector2 intersectionPoint = intersectionData.getIntersectionPoint();

               // Distance from each center of curvature to the intersection point
               float distanceFromLeftFaceCenter = intersectionPoint.distance(leftFaceCenter);
               float distanceFromRightFaceCenter = intersectionPoint.distance(rightFaceCenter);

               // The intersection belongs to the left surface if it is closer to the left center of curvature
               if(distanceFromLeftFaceCenter < distanceFromRightFaceCenter) {
                    normal = intersectionPoint.subtract(leftFaceCenter).getNormalized();
               }
               // Otherwise, the intersection belongs to the right surface
               else if(distanceFromRightFaceCenter < distanceFromLeftFaceCenter) {
                    normal = intersectionPoint.subtract(rightFaceCenter).getNormalized();
               }
          }

          // Incoming line vector
          Vector2 incidentVector = intersectionData.getIncomningLine().getLineVector();

          // Ensures the normal faces against the incoming ray
          if(normal.dot(incidentVector.getNormalized()) > 0) {
               normal = normal.multiply(-1f);
          }
          
          return new SurfaceData(intersectionData.getTargetLine().getNormalizedDirection(), normal);
     }

     // Hides parent static calculateArcAngle method
     protected static float calculateArcAngle(float radiusOfCurvature, float centerThickness, float apertureDiameter) {
          /**
               A concave lens can be constructed from the non-intersecting regions of two circles.

               Assuming the lens has a radius of curvature 'r', a center thickness 'w' and a aperture radius of 'rₐ',
               the radius of each circle is:

                    R = r - (w / 2)

               The angle of the arc forming the lens surface can then be determined from
               the geometry of the non-intersecting circles:

                    θ = π - 2cos⁻¹(rₐ / R)
          */

          float r = radiusOfCurvature;
          float w = centerThickness;

          float R = r - (w / 2);
          float rA = apertureDiameter / 2; // Aperture radius

          // The cosine ratio of the related triangle
          float cosRatio = rA / R;
          // Clamps the value between -1 & +1
          cosRatio = Math.clamp(cosRatio, -1f, 1f);

          // cos⁻¹(rₐ / R)
          float phi = (float) Math.acos(cosRatio);
          // Calculates and returns the angle of the arc 
          return (float) Math.PI - 2 * phi;
     }

     // Hides parent static createShape method
     protected static Polygon createShape(float radiusOfCurvature, float centerThickness, float appertureDiameter) {
          // Calculates the required angle
          float theta = calculateArcAngle(radiusOfCurvature, centerThickness, appertureDiameter); // Aperture diameter is not required for convex lens

          float r = radiusOfCurvature;
          float R = radiusOfCurvature - (centerThickness / 2);

          // Creates the two opposing lens surfaces
          Arc arc1 = new Arc(R, theta, 0f,  (float) Math.PI);
          Arc arc2 = new Arc(R, theta, 0f);

          // Gets vertices relative to each arc's circle center
          Vector2[] localVertices1 = arc1.getApproximatedPolygon().getLocalVertices();
          Vector2[] localVertices2 = arc2.getApproximatedPolygon().getLocalVertices();

          // Positions each circle center relative to the optical center
          Vector2 center1Offset = Vector2.RIGHT.multiply(r); // (r, 0)
          Vector2 center2Offset = Vector2.LEFT.multiply(r); // (-r, 0)

          // Combines both arcs
          Vector2[] vertices = new Vector2[localVertices1.length + localVertices2.length];

          int index = 0;
          
          // First arc
          for (int i = 0; i < localVertices1.length; i++) {
               vertices[index++] = localVertices1[i].add(center1Offset);
          }

          // Second arc
          for (int i = 0; i < localVertices2.length; i++) {
               vertices[index++] = localVertices2[i].add(center2Offset);
          }
          
          return new Polygon(vertices);
     }

     // Draws the shape to be rendered
     @Override
     protected void drawShape(Graphics2D g) {
          float r = radiusOfCurvature;
          float w = centerThickness;
          float R = r - (w / 2);
          float theta = arcAngle;
          float rotation = collider.getRotation();

          Color oldColor = g.getColor();
          g.setColor(getMedium().getColor());

          // Calculate the two circle centers
          // The center of curvature of each concave surface lies behind its surface
          Vector2 leftFaceCenter = getLeftFaceCenterOfCurvature();
          Vector2 rightFaceCenter = getRightFaceCenterOfCurvature();

          Vector2 leftScreenCenter = Scene.worldToScreen(leftFaceCenter);
          Vector2 rightScreenCenter = Scene.worldToScreen(rightFaceCenter);

          float leftCx = (float) leftScreenCenter.getX();
          float leftCy = (float) leftScreenCenter.getY();
          float rightCx = (float) rightScreenCenter.getX();
          float rightCy = (float) rightScreenCenter.getY();

          // Starts the left surface at the upper endpoint
          // The left surface lies on the opposite side of its circle
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

          // Left concave surface
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

          // Right concave surface
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

          // Left surface
          lensShape.append(leftSurface, true);
          // Right surface
          lensShape.append(rightSurface, true);

          lensShape.closePath();

          g.fill(lensShape);
          g.setColor(oldColor);
     }
}
