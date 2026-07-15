package main.models.entities;

import java.awt.Graphics2D;

import main.input.MouseInput;
import main.math.Vector2;
import main.models.GameObject;
import main.models.Sprite;

public class Laser extends GameObject {
     private boolean enabled;
     private boolean on;

     public Laser(float x, float y) {
          super("Laser", x, y);
          enabled = true;
          on = false;

          Sprite laserSprite = new Sprite("laser-pointer.png");
          setSprite(laserSprite);
          sprite.scale(1.5f);
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

     public void setState(boolean state) {
          this.on = state;
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
          }
     }

     @Override
     public void render(Graphics2D g) {
          sprite.draw(g, position.getX(), position.getY());
     }
}
