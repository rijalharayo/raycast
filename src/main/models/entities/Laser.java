package main.models.entities;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.ArrayList;

import main.audio.SoundEffect;
import main.input.KeyboardInput;
import main.input.MouseInput;
import main.math.algebra.Vector2;
import main.models.GameObject;
import main.physics.rays.RayHit;
import main.sprites.Sprite;

public class Laser extends GameObject {
     private boolean enabled;
     private boolean on;
     // Flag to check if the laser is moving or not
     private boolean isMoving = false;
     // Tracks time between laser updates
     private float timeSinceLastMovement = 0f;

     // Current pointing dir of the laser
     private float firingAngle = 0f;

     private List<LightRay> lightRays = new ArrayList<>();

     private final String SPRITE_NAME = "laser-pointer.png";

     // Default laser sound effects
     public static final SoundEffect LIGHT_ON = new SoundEffect("light-on.wav");
     public static final SoundEffect LIGHT_OFF = new SoundEffect("light-off.wav");
     public static final SoundEffect LASER_ENABLE_DISABLE = new SoundEffect("laser-enable-disable.wav");

     public Laser(float x, float y) {
          super("Laser", x, y, null);
          enabled = true;
          on = true;

          Sprite laserSprite = new Sprite(SPRITE_NAME);
          setSprite(laserSprite);
          sprite.scale(1.5f);
          
          // Adds a default ray
          addRay(
               new LightRay(getRayOriginPoint(), (float) Math.toDegrees(firingAngle), 50f)
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
          LIGHT_OFF.play();
          this.on = false;
     }

     public void turnOn() {
          LIGHT_ON.play();
          this.on = true;
     }

     public void addRay(LightRay ray) {
          lightRays.add(ray);
     }

     // Returns rotation from the cursor to the centre of the sprite
     private float getAngleFromCursor() {
          Vector2 mousePos = MouseInput.getMousePosition();

          Vector2 vectorBetween = mousePos.subtract(position);
          
          return (float) vectorBetween.getAngle();
     }

     // Returns the position of the tip of the laser
     private Vector2 getRayOriginPoint() {
          float widthOffset = sprite.getWidth() / 2;
          // Gets the local tip of the vector
          Vector2 localTip = new Vector2(-widthOffset, 0);

          Vector2 rotatedTip = localTip.rotate(firingAngle);
          // Gets the world tip by rotating the localTip
          return position.add(rotatedTip);
     }

     // Casts & updates the rays
     private void castRays() {
          // Initial ray
          LightRay initialRay = lightRays.getFirst();

          // Offsets by π or 180 as the ray faces away from the laser
          initialRay.setFromDirection(
               getRayOriginPoint(),
               firingAngle + (float) Math.PI
          );

          // Clears the current list and adds the intial ray
          lightRays.clear();
          lightRays.add(initialRay);

          LightRay currentRay = initialRay;

          int maxRebounce = 20;
          int currentRebounce = 0;

          while(currentRebounce <= maxRebounce) {
               RayHit hit = currentRay.trace(currentRay.getStart());
               LightRay next = (LightRay) hit.getNextRay();

               if(next == null)
                    break;

               currentRay = next;
               lightRays.add(currentRay);

               currentRebounce++;
          }
     }

     @Override
     public void update() {
          // Laser turns off on right click
          if(MouseInput.isPressed(MouseEvent.BUTTON3) /* Right click */) {
               on = !on;
               // Play sound effects
               if(on) {
                    LIGHT_ON.play();
               }
               else {
                    LIGHT_OFF.play();
               }
          }

          // Disabled when 'E' is pressed
          if(KeyboardInput.isPressed(KeyEvent.VK_E)) {
               LASER_ENABLE_DISABLE.play();

               enabled = !enabled;
               // Set it to not moving when the laser is disabled
               isMoving = false; 
               timeSinceLastMovement = 0f;
          }

          boolean dirtyEnvironment = this.getObjectLevelScene().isEnvironmentDirty();

          if(enabled) {
               float currentAngle = getAngleFromCursor();
               // Checks if the firing angle has changed
               if(Math.abs(currentAngle - firingAngle) > 0.01f) {
                    isMoving = true;
                    timeSinceLastMovement = 0f;
               }
               else {
                    // Increase time by a tiny amount
                    timeSinceLastMovement += 0.01f;
               }

               // If enough time has passed, consider it stop moving
               if (timeSinceLastMovement >= 0.2f) {
                    isMoving = false;
               }

               // Only update if its moving or if the environment has changed/is changing
               if(isMoving || dirtyEnvironment) {
                    firingAngle = currentAngle;
                    this.setRotation((float) Math.toDegrees(firingAngle));
               }
          }

          // Update rays if the laser is turned on, is moving or is in an modified environment
          if(on && (isMoving || dirtyEnvironment)) {
               castRays();
          }
     }

     @Override
     public void setRotation(float angle) {
          sprite.setRotation(angle);
     }

     @Override
     public void render(Graphics2D g) {
          super.render(g);
          // Render all the rays shot from the laser if its on
          if(on) {
               for(LightRay ray : lightRays) {
                    ray.render(g);
               }
          }
     }
}