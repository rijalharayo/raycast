package main.models.environment.reflectors.mirrors;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Path2D;

import main.game.Scene;
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
          // Get arc collider data
          ArcCollider collider = (ArcCollider) this.getCollider();

          float radius = collider.getRadius();
          float thickness = collider.getThickness();
          float angle = collider.getAngle();

          float innerRadius = radius - thickness;


          // Get the actual center of curvature in world space
          Arc arc = (Arc) collider.getShape();

          Vector2 worldCenter = arc.getWorldCenterOfCurvature(
               this.getPosition(),
               collider.getRotation()
          );


          // Convert world coordinates to screen coordinates
          Vector2 screenCenter = Scene.worldToScreen(worldCenter);

          float cx = screenCenter.getX();
          float cy = screenCenter.getY();

          // Save current graphics settings
          Color oldColor = g.getColor();
          AffineTransform oldTransform = g.getTransform();

          // Set arc appearance
          g.setColor(new Color(144, 213, 255, 190));

          // Create outer circular arc
          Arc2D.Float outerArc =
               new Arc2D.Float(
                    cx - radius,
                    cy - radius,
                    radius * 2,
                    radius * 2,
                    0,
                    (float) Math.toDegrees(angle),
                    Arc2D.OPEN
               );

          // Create inner circular arc in reverse direction
          Arc2D.Float innerArc =
               new Arc2D.Float(
                    cx - innerRadius,
                    cy - innerRadius,
                    innerRadius * 2,
                    innerRadius * 2,
                    (float) Math.toDegrees(angle),
                    (float) Math.toDegrees(-angle),
                    Arc2D.OPEN
               );

          // Combine both arcs into a filled ring sector
          Path2D.Float arcShape = new Path2D.Float();

          arcShape.append(outerArc, false);
          arcShape.append(innerArc, true);

          arcShape.closePath();

          // Rotate arc according to collider rotation
          g.rotate(
               -collider.getRotation(),
               cx,
               cy
          );

          // Render arc
          g.fill(arcShape);

          // Restore graphics state
          g.setTransform(oldTransform);
          g.setColor(oldColor);


          // Draw collider outline
          //this.getCollider().draw(g);
     }
}
