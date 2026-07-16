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

     private List<Ray> rays = new ArrayList<>();

     public Laser(float x, float y) {
          super("Laser", x, y);
          enabled = true;
          on = true;

          Sprite laserSprite = new Sprite("laser-pointer.png");
          setSprite(laserSprite);
          sprite.scale(1.5f);

          // Adds a default ray
          addRay(
               new Ray(position, getAngleFromCursor(), 0)
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

     public void addRay(Ray ray) {
          rays.add(ray);
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
                    for(Ray ray : rays) {
                         // Update code
                    }
               }
          }
     }

     @Override
     public void render(Graphics2D g) {
          sprite.draw(g, position.getX(), position.getY());
          // Render all the rays shot from the laser if its on
          if(on) {
               for(Ray ray : rays) {
                    ray.render(g);
               }
          }
     }
}