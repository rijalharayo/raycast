package main.models.entities;

import java.awt.Graphics2D;
import java.util.List;
import java.util.ArrayList;

import main.input.MouseInput;
import main.math.algebra.Vector2;
import main.models.GameObject;
import main.models.RayData;
import main.models.Sprite;
import main.physics.rays.RayHit;

public class Laser extends GameObject {
     private boolean enabled;
     private boolean on;

     private List<LightRay> lightRays = new ArrayList<>();

     private final String SPRITE_NAME = "laser-pointer.png";

     public Laser(float x, float y) {
          super("Laser", x, y);
          enabled = true;
          on = true;

          Sprite laserSprite = new Sprite(SPRITE_NAME);
          setSprite(laserSprite);
          sprite.scale(1.5f);
          
          // Adds a default ray
          addRay(
               new LightRay(getRayOriginPoint(), getAngleFromCursor(), 50f)
          );
     }

     // Getters
     public boolean isEnabled() {
          return enabled;
     }

     public boolean isOn() {
          return on;
     }

     // Setters
     public void enable() {
          this.enabled = true;
     }

     public void disable() {
          this.enabled = false;
     }

     public void turnOff() {
          this.on = false;
     }

     public void turnOn() {
          this.on = true;
     }

     public void addRay(LightRay ray) {
          lightRays.add(ray);
     }

     // Returns rotation from the cursor to the centre of the sprite
     private float getAngleFromCursor() {
          Vector2 mousePos = MouseInput.getMousePosition();

          Vector2 vectorBetween = mousePos.subtract(position);
          // Offsets the angle to match the sprite's default orientation by subtracting π/2
          return vectorBetween.getAngle();
     }

     // Returns the position of the tip of the laser
     private Vector2 getRayOriginPoint() {
          float widthOffset = sprite.getWidth() / 2;
          // Gets the local tip of the vector
          Vector2 localTip = new Vector2(-widthOffset, 0);

          Vector2 rotatedTip = localTip.rotate(getAngleFromCursor());
          // Gets the world tip by rotating the localTip
          return position.add(rotatedTip);
     }

     // Casts & updates the rays
     private void castRays() {
          // Initial ray
          LightRay initialRay = lightRays.getFirst();

          // Offesets by π as the ray faces away from the laser
          initialRay.setFromDirection(
               getRayOriginPoint(),
               getAngleFromCursor() + (float)Math.PI
          );

          // Clears the current list and adds the intial ray
          lightRays.clear();
          lightRays.add(initialRay);

          LightRay currentRay = initialRay;

          int maxRebounce = 10;
          int currentRebounce = 0;

          while(currentRebounce <= maxRebounce) {
               RayHit hit = currentRay.trace(currentRay.getStart());
               RayData next = hit.getNextRayData();

               if(next == null)
                    break;

               currentRay = new LightRay(next);
               lightRays.add(currentRay);

               currentRebounce++;
          }
     }

     @Override
     public void update() {
          if(enabled) {
               this.setRotation(getAngleFromCursor());
               // Update rays if the laser is turned on
               if(on) {
                    castRays();
               }
          }
     }

     @Override
     public void render(Graphics2D g) {
          sprite.draw(g, position.getX(), position.getY());
          // Render all the rays shot from the laser if its on
          if(on) {
               for(LightRay ray : lightRays) {
                    ray.render(g);
               }
          }
     }
}