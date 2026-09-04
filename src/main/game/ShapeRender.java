package main.game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

import main.math.algebra.Vector2;
import main.math.shapes.Arc;
import main.math.shapes.Circle;
import main.math.shapes.Shape;
import main.math.shapes.polygons.Polygon;
import main.math.shapes.polygons.Rectangle;

// Rendering utility for drawing shapes to the screen
public class ShapeRender {

     // For all shapes
     public static void draw(
          Graphics2D g,
          Shape shape,
          Vector2 position,
          float rotation,
          Color color
     ) {

          if (shape instanceof Circle circle) {
               draw(g, circle, position, rotation, color);
          }

          else if (shape instanceof Rectangle rectangle) {
               draw(g, rectangle, position, rotation, color);
          }

          else if (shape instanceof Arc arc) {
               draw(g, arc, position, rotation, color);
          }

          else if (shape instanceof Polygon polygon) {
               draw(g, polygon, position, rotation, color);
          }
     }

     // Draws a circle
     public static void draw(
          Graphics2D g,
          Circle circle,
          Vector2 worldPosition,
          float rotation,
          Color color
     ) {

          Vector2 screenPosition = Scene.worldToScreen(worldPosition);

          float radius = circle.getRadius();

          AffineTransform oldTransform = g.getTransform();
          Color oldColor = g.getColor();

          g.setColor(color);

          g.rotate(-rotation,
               screenPosition.getX(),
               screenPosition.getY()
          );

          g.fill(new Ellipse2D.Float(
               (float) screenPosition.getX() - radius,
               (float) screenPosition.getY() - radius,
               radius * 2,
               radius * 2
          ));

          g.setTransform(oldTransform);
          g.setColor(oldColor);
     }

     // Draws a rectangle
     public static void draw(
          Graphics2D g,
          Rectangle rectangle,
          Vector2 worldPosition,
          float rotation,
          Color color
     ) {

          Vector2 screenPosition = Scene.worldToScreen(worldPosition);

          float width = rectangle.getWidth();
          float height = rectangle.getHeight();

          AffineTransform oldTransform = g.getTransform();
          Color oldColor = g.getColor();

          g.setColor(color);

          g.rotate(
               -rotation,
               screenPosition.getX(),
               screenPosition.getY()
          );

          g.fill(new Rectangle2D.Float(
               (float) screenPosition.getX() - width / 2,
               (float) screenPosition.getY() - height / 2,
               width,
               height
          ));

          g.setTransform(oldTransform);
          g.setColor(oldColor);
     }

     // Draws an arc
     public static void draw(
          Graphics2D g,
          Arc arc,
          Vector2 worldPosition,
          float rotation,
          Color color
     ) {

          float radius = arc.getRadius();
          float thickness = arc.getThickness();
          float angle = arc.getAngle();

          float innerRadius = radius - thickness;

          Vector2 worldCenter =
               arc.getWorldCenterOfCurvature(
                    worldPosition,
                    rotation
               );

          Vector2 screenCenter =
               Scene.worldToScreen(worldCenter);

          float cx = (float) screenCenter.getX();
          float cy = (float) screenCenter.getY();

          AffineTransform oldTransform = g.getTransform();
          Color oldColor = g.getColor();

          g.setColor(color);

          // Starts the outer arc half an angle before the midpoint
          float outerStartAngle = (float) Math.toDegrees(arc.getRotation() - (angle / 2));

          Arc2D.Float outerArc =
               new Arc2D.Float(
                    cx - radius,
                    cy - radius,
                    radius * 2,
                    radius * 2,
                    outerStartAngle,
                    (float) Math.toDegrees(angle),
                    Arc2D.OPEN
               );

          // Starts the inner arc at the opposite endpoint
          float innerStartAngle = (float) Math.toDegrees(arc.getRotation() + (angle / 2));

          Arc2D.Float innerArc =
               new Arc2D.Float(
                    cx - innerRadius,
                    cy - innerRadius,
                    innerRadius * 2,
                    innerRadius * 2,
                    innerStartAngle,
                    (float) Math.toDegrees(-angle),
                    Arc2D.OPEN
               );

          Path2D.Float arcShape = new Path2D.Float();

          arcShape.append(outerArc, false);
          arcShape.append(innerArc, true);
          arcShape.closePath();

          g.fill(arcShape);

          g.setTransform(oldTransform);
          g.setColor(oldColor);
     }

     // Draws a polygon
     public static void draw(
          Graphics2D g,
          Polygon polygon,
          Vector2 worldPosition,
          float rotation,
          Color color
     ) {

          Vector2[] vertices = polygon.getLocalVertices();

          if (vertices.length < 3)
               return;

          AffineTransform oldTransform = g.getTransform();
          Color oldColor = g.getColor();

          g.setColor(color);

          Path2D.Float path = new Path2D.Float();

          Vector2 first = vertices[0];

          Vector2 screenFirst =
               Scene.worldToScreen(
                    worldPosition.add(first.rotate(rotation))
               );

          path.moveTo(
               screenFirst.getX(),
               screenFirst.getY()
          );

          for (int i = 1; i < vertices.length; i++) {

               Vector2 vertex = vertices[i];

               Vector2 screenVertex =
                    Scene.worldToScreen(
                         worldPosition.add(
                              vertex.rotate(rotation)
                         )
                    );

               path.lineTo(
                    screenVertex.getX(),
                    screenVertex.getY()
               );
          }

          path.closePath();

          g.fill(path);

          g.setTransform(oldTransform);
          g.setColor(oldColor);
     }
}