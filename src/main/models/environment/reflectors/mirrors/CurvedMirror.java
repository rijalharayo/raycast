package main.models.environment.reflectors.mirrors;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Path2D;

import main.game.Scene;
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
               new ArcCollider(position, radius, (float) Math.toRadians(angle), thickness) 
          );

          this.setRotation(rotation);
     }

     public CurvedMirror(float x, float y, float radius, float angle, float thickness, float rotation) {
          super(new Vector2(x, y), 
               new ArcCollider(new Vector2(x, y), radius, (float) Math.toRadians(angle), thickness)
          );
          
          this.setRotation(angle);
     }

     @Override
     public SurfaceData calculateSurfaceData(IntersectionData intersectionData) {
          return null;
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


          // Rotate arc according to collider rotation
          g.rotate(
               Math.toRadians(collider.getRotation()),
               cx,
               cy
          );


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


          // Render arc
          g.fill(arcShape);


          // Restore graphics state
          g.setTransform(oldTransform);
          g.setColor(oldColor);


          // Draw collider outline
          this.getCollider().draw(g);
     }
}
