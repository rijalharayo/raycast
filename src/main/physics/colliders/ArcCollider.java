package main.physics.colliders;

import java.awt.Color;
import java.awt.Graphics2D;

import main.game.Scene;
import main.math.Line;
import main.math.algebra.Vector2;
import main.math.shapes.Arc;

// Represents an collider in shape of some arc
public class ArcCollider extends Collider {
     // Constructors
     public ArcCollider(Vector2 position, float radius, float angle, float thickness) {
          super(position, 
               new Arc(radius, angle, thickness)
          );
     }

     public ArcCollider(Vector2 position, float radius, float angle, float thickness, float rotation) {
          super(position, 
               new Arc(radius, angle, thickness, rotation)
          );
     }
     
     // Getters
     public float getRadius() {
          Arc arc = (Arc) shape;
          return arc.getRadius();
     }

     public float getAngle() {
          Arc arc = (Arc) shape;
          return arc.getAngle();
     }

     public float getThickness() {
          Arc arc = (Arc) shape;
          return arc.getThickness();
     }

     @Override
     public void draw(Graphics2D g) {
          super.draw(g);

          // Renders the center of curvature
          Arc arc = (Arc) shape;

          Vector2 center = arc.getWorldCenterOfCurvature(
               this.position,
               this.getRotation()
          );

          Vector2 screenCenter = Scene.worldToScreen(center);

          int r = 5;

          // Orange
          g.setColor(new Color(255, 140, 0));
          g.fillOval(
               (int) (screenCenter.getX() - r),
               (int) (screenCenter.getY() - r),
               r * 2,
               r * 2
          );

          // Flat edges of the arc
          Line[] flats = arc.getWorldFlatEdges(position);

          for(Line edge : flats) {
               // Each flat edge has one inner endpoint and one outer endpoint
               Vector2 innerPoint;

               if(edge.getStart().subtract(center).getMagnitude() < edge.getEnd().subtract(center).getMagnitude()) {
                    innerPoint = edge.getStart();
               } 
               else {
                    innerPoint = edge.getEnd();
               }

               Vector2 screenInner = Scene.worldToScreen(innerPoint);

               g.drawLine(
                    (int) screenCenter.getX(),
                    (int) screenCenter.getY(),
                    (int) screenInner.getX(),
                    (int) screenInner.getY()
               );
          }
     }
}