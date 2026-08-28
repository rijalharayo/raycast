package main.models.entities;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import main.game.Scene;
import main.math.algebra.Vector2;
import main.models.GameObject;
import main.models.data.RayData;
import main.models.environment.OpticalObject;
import main.physics.colliders.CollisionType;
import main.physics.rays.Ray;
import main.physics.rays.RayHit;
import main.physics.rays.VirtualRay;

// Class for the light ray
public class LightRay extends GameObject implements Ray {
     private Color rayColor = Color.GREEN;
     private static final int STROKE_WIDTH = 2;

     private RayData rayData;

     // Constructors
     public LightRay(Vector2 start, Vector2 end) {
          super("", start, null);
          this.rayData = new RayData(start, end);
     }

     public LightRay(float x1, float y1, float x2, float y2) {
          super("", new Vector2(x1, y1), null);
          this.rayData = new RayData(x1, y1, x2, y2);
     }

     public LightRay(Vector2 start, Vector2 end, Color color) {
          super("", start, null);
          this.rayData = new RayData(start, end);

          if(color != null) {
               this.rayColor = color;
          }

     }

     public LightRay(Vector2 start, float angle, float magnitude) {
          super("", start, null);
          rayData = new RayData(start, (float) Math.toRadians(angle), magnitude);
     }

     public LightRay(RayData data) {
          super("", data.getStart(), null);
          this.rayData = data;
     }

     // Getters
     public Color getRayColor() {
          return rayColor;
     }

     // Setters
     public void setRayColor(Color color) {
          if(color == null) throw new IllegalArgumentException("Color can't be null");
          this.rayColor = color;
     }

     // Traces/Updates the light ray and returns data on collision
     public RayHit trace(Vector2 prevPos) {
          // Creates and casts a virtaul ray from the position of the light ray
          VirtualRay vRay = new VirtualRay(prevPos, this.getRayData().getNormalizedDirection());
          // Gets the collision data
          RayHit rayHit = vRay.cast();

          if((rayHit != null) && rayHit.getCollisionType() == CollisionType.OPTICAL_COLLISION) {
               OpticalObject targetObject = (OpticalObject) rayHit.getTargetObject();

               RayData nextRayData = targetObject.interact(vRay);
               rayHit.setNextRayData(nextRayData);
          }

          /* 
               Since the cast() method stops when it collides,
               the endpoint of the light ray is set as the collision point of the virtual ray
          */ 
          this.setPoints(prevPos, rayHit.getCollisionPoint());

          return rayHit;
     }

     @Override
     public void update() {
          // Code
     }

     @Override
     public void render(Graphics2D g) {
          // Create a 2nd graphics variable
          Graphics2D g2 = (Graphics2D) g.create();
          // Sets stroke width & color
          g2.setColor(rayColor);
          g2.setStroke(new BasicStroke(STROKE_WIDTH));

          Vector2 screenStart = Scene.worldToScreen(getStart());
          Vector2 screenEnd = Scene.worldToScreen(getEnd());
          
          g2.drawLine(
               (int) screenStart.getX(),
               (int) screenStart.getY(),
               (int) screenEnd.getX(),
               (int) screenEnd.getY()
          );

          g2.dispose();
     }

     @Override
     public RayData getRayData() {
          return rayData;
     }

     @Override
     public String toString() {
          return rayData.toString();
     }
}
