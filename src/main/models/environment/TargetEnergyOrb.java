package main.models.environment;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

import main.audio.SoundEffect;
import main.game.LevelManager;
import main.game.ShapeRender;
import main.math.algebra.Vector2;
import main.math.shapes.Circle;
import main.models.GameObject;
import main.models.data.IntersectionData;
import main.models.data.SurfaceData;
import main.models.entities.LightRay;
import main.physics.colliders.CircleCollider;
import main.physics.colliders.CollisionData;
import main.physics.optics.RayInteractable;

// The object the ray must hit to advance
public class TargetEnergyOrb extends GameObject implements RayInteractable {
     private static final Color MAIN_ORB_COLOR = new Color(225, 224, 255);

     // Glow colors
     private static final Color[] GLOW_COLORS = {
          new Color(245, 244, 255, 100),
          new Color(240, 239, 255, 92),
          new Color(235, 234, 255, 84),
          new Color(230, 229, 255, 76),
          new Color(225, 224, 255, 68),
          new Color(220, 219, 255, 60),
          new Color(215, 214, 255, 52),
          new Color(210, 209, 255, 44),
          new Color(205, 204, 255, 36),
          new Color(200, 199, 255, 30),
          new Color(195, 194, 255, 24),
          new Color(190, 189, 255, 19),
          new Color(185, 184, 255, 14),
          new Color(180, 179, 255, 10),
          new Color(175, 174, 255, 7),
          new Color(170, 169, 255, 5),
          new Color(165, 164, 255, 3),
          new Color(160, 159, 255, 2),
          new Color(155, 154, 255, 1)
     };

     // Caches the glowing-effect circles of the orb
     private Circle[] cachedGlowCirlces = new Circle[GLOW_COLORS.length];

     private static final int PARTICLE_COUNT = 10;
     // Array of all glow particles
     private final GlowParticle[] glowParticles = new GlowParticle[PARTICLE_COUNT];
     // Original radius of orb
     private float radius;
     // Original shape of orb
     private Circle originalShape;

     // Used for delays between level switches
     private static final float LEVEL_COMPLETE_DELAY = 0.5f;

     private boolean completing = false;
     private float completionTimer = 0f;

     // Constructors
     public TargetEnergyOrb(Vector2 position, float radius) {
          super(
               "Target orb",
               position,
               new CircleCollider(position, radius * 0.7f) // Reduces hitbox radius by 30%
          );

          this.radius = radius;
          this.originalShape = new Circle(radius);

          initializeGlowParticles();
          initializeGlowCircles();
     }

     public TargetEnergyOrb(float x, float y, float radius) {
          Vector2 position = new Vector2(x, y);
          
          super(
               "Target orb",
               position,
               new CircleCollider(position, radius * 0.7f) // Reduces hitbox radius by 30%
          );

          this.radius = radius;
          this.originalShape = new Circle(radius);

          initializeGlowParticles();
          initializeGlowCircles();
     }

     // Initializes & caches the cirlces that give glow effect
     private void initializeGlowCircles() {
          // Difference in radii
          float deltaRadius = 1.5f;

          for(int i = 1; i < cachedGlowCirlces.length + 1; i++) {
               // Each cirlce will be a little bigger
               cachedGlowCirlces[i - 1] = new Circle(this.radius + (i * deltaRadius));
          }
     }

     // Initializes all glow particles
     private void initializeGlowParticles() {
          for (int i = 0; i < glowParticles.length; i++) {
               glowParticles[i] = new GlowParticle(
                    position,
                    radius
               );
          }
     }

     @Override
     public LightRay interact(LightRay ray, CollisionData collisionData) {
          // Plays sound effect
          SoundEffect.LEVEL_COMPLETE.play();

          // Do nothing if already completed
          if (completing) {
               return null;
          }

          // The player has passed this level
          completing = true;
          // Disable the laser
          this.getObjectLevelScene().getLevelLaser().disable();

          return null;
     }

     // Surface data isn't requried for orb
     @Override
     public SurfaceData calculateSurfaceData(IntersectionData intersectionData) {
          return null;
     }

     @Override
     public void update() {
          // Update particles every frame
          for (GlowParticle particle : glowParticles) {
               particle.update();
          }

          // If the player has passed this level, advacne to the next one after delay
          if (completing) {
               completionTimer += 1f / 100f;

               if (completionTimer >= LEVEL_COMPLETE_DELAY) {
                    int currentLevelIndex = LevelManager.getCurrentLevelIndex();
                    LevelManager.setCurrentLevel(currentLevelIndex + 1);
               }
          }
     }

     @Override
     public void render(Graphics2D g) {
          super.render(g);

          // Gives glow effect
          for(int i = 0; i < cachedGlowCirlces.length; i++) {
               ShapeRender.draw(
                    g,
                    cachedGlowCirlces[i],
                    position,
                    0f,
                    GLOW_COLORS[i]
               );
          }

          ShapeRender.draw(
               g,
               originalShape,
               position,
               0f,
               MAIN_ORB_COLOR
          );

          // Render particles
          for (GlowParticle particle : glowParticles) {
               particle.render(g);
          }
     }
}

// Glow particle that moves away from the center
class GlowParticle extends GameObject {
     private Vector2 position;

     private static final Circle PARTICLE_SHAPE = new Circle(5f);
     private static final Color PARTICLE_COLOR = new Color(232, 227, 253, 70);

     private static final Random random = new Random();

     // Orb center & radius
     private final Vector2 center;
     private final float orbRadius;

     // Direction of the particle
     private Vector2 direction;
     // Max distance of travel
     private float maxDistance;

     public GlowParticle(Vector2 center, float orbRadius) {
          this.center = center;
          this.orbRadius = orbRadius;

          reset();
     }

     // Reset the particle
     private void reset() {
          float angle = random.nextFloat() * (float) (Math.PI * 2);
          float distance = orbRadius + random.nextFloat(2f, 5f);
          this.maxDistance = random.nextFloat(30f, 60f);

          position = center.add(
               new Vector2(
                    (float) Math.cos(angle),
                    (float) Math.sin(angle)
               ).multiply(distance)
          );

          direction = position.subtract(center).getNormalized();
     }

     @Override
     public void render(Graphics2D g) {
          ShapeRender.draw(
               g,
               PARTICLE_SHAPE,
               position,
               0f,
               PARTICLE_COLOR
          );
     }

     @Override
     public void update() {
          position = position.add(
               direction.multiply(0.15f)
          );

          // Reset it if it has gone too far
          if (position.distance(center) > (orbRadius + maxDistance)) {
               reset();
          }
     }
}