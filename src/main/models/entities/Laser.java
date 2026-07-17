package main.models.entities;

import java.awt.Graphics2D;
import java.util.List;
import java.util.ArrayList;

import main.input.MouseInput;
import main.math.Vector2;
import main.models.GameObject;
import main.models.Sprite;

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
               new LightRay(position, getAngleFromCursor(), 0)
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
          Vector2 mousePos = new Vector2(
               MouseInput.getMouseX(),
               MouseInput.getMouseY()
          );

          Vector2 vectorBetween = mousePos.subtract(position);
          // Offsets the angle to match the sprite's default orientation by subtracting π/2
          return vectorBetween.getAngle() - (float) Math.PI/2;
     }

     @Override
     public void update() {
          if(enabled) {
               this.setRotation(getAngleFromCursor());

               // Update rays if the laser is turned on
               if(on) {
                    for(LightRay ray : lightRays) {
                        ray.setFromDirection(position, getAngleFromCursor(), 50f);
                    }
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