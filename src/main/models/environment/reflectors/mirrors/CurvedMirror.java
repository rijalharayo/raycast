package main.models.environment.reflectors.mirrors;

import java.awt.Color;
import java.awt.Graphics2D;
import main.game.ShapeRender;
import main.math.Line;
import main.math.algebra.Matrix2x2;
import main.math.algebra.Vector2;
import main.math.shapes.Arc;
import main.models.data.IntersectionData;
import main.models.data.SurfaceData;
import main.physics.colliders.ArcCollider;

// Represents a mirror in an arc shape
public class CurvedMirror extends Mirror {
     // Constructors
     public CurvedMirror(Vector2 position, float radius, float angle, float thickness) {
          super(position, 
               new ArcCollider(position, radius, (float) Math.toRadians(angle), thickness)
          );
     }

     public CurvedMirror(float x, float y, float radius, float angle, float thickness) {
          super(new Vector2(x, y), 
               new ArcCollider(new Vector2(x, y), radius, (float) Math.toRadians(angle), thickness)
          );
     }

     public CurvedMirror(Vector2 position, float radius, float angle, float thickness, float rotation) {
          super(position, 
               new ArcCollider(position, radius, (float) Math.toRadians(angle), thickness, (float) Math.toRadians(rotation)) 
          );
     }

     public CurvedMirror(float x, float y, float radius, float angle, float thickness, float rotation) {
          super(new Vector2(x, y), 
               new ArcCollider(new Vector2(x, y), radius, (float) Math.toRadians(angle), thickness, (float) Math.toRadians(rotation))
          );
     }

     @Override
     public SurfaceData calculateSurfaceData(IntersectionData intersectionData) {
          Line targetLine = intersectionData.getTargetLine();

          Arc arc = (Arc) this.getCollider().getShape();

          // Flat edges of the arc
          Line[] flatEdges = arc.getWorldFlatEdges(position);
          // Checks if the target line is a flat edge
          for(Line edge : flatEdges) {
               if(edge.isSameSegment(targetLine)) {
                    // It acts as a plane mirro
                    
                    Vector2 normal = targetLine.getNormal();
                    Vector2 incidentVector = intersectionData.getIncomningLine().getLineVector();

                    // If the incident ray & normal face the same direction, invert it
                    if(incidentVector.dot(normal) > 0) {
                         normal = normal.multiply(-1f);
                    }

                    // The mirror's surface is the collider's edge
                    SurfaceData mirrorSurface = new SurfaceData(targetLine.getLineVector(), normal);

                    return mirrorSurface;
               }
          }

          Vector2 arcCenter = arc.getWorldCenterOfCurvature(position, arc.getRotation());
          Vector2 intersectionPoint = intersectionData.getIntersectionPoint();

          /* 
               The radius drawn from the arc's center of curvature to the tangent is perpendicular to the tangent.
               
               Since the intersection point lies on the tanget, & also on the circumference
               of the outer arc, it's at a distance of radius 'r' from the center of the
               arc.

               So the vector from the centre to the intersection-point will be the normal.
               The same vector rotated 90deg will be the tangent line itself.
          */

          Vector2 normal = intersectionPoint.subtract(arcCenter);
          Vector2 tangent = Matrix2x2.ROTATE_ANTI_CLOCKWISE_90.transform(normal);

          return new SurfaceData(tangent, normal);
     }
     
     @Override
     public void render(Graphics2D g) {
          super.render(g);

          // Get arc collider data
          ArcCollider collider = (ArcCollider) this.getCollider();

          // Get the actual center of curvature in world space
          Arc arc = (Arc) collider.getShape();

          ShapeRender.draw(
               g,
               arc,
               position,
               collider.getRotation(),
               new Color(144, 213, 255, 190)
          );
     }
}
