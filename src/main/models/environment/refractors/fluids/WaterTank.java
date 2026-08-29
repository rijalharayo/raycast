package main.models.environment.refractors.fluids;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import main.game.Scene;
import main.math.algebra.Vector2;
import main.math.shapes.polygons.Rectangle;
import main.models.environment.refractors.Refractor;
import main.physics.colliders.BoxCollider;
import main.physics.optics.Medium;

// A solid glass block

public class WaterTank extends Refractor {
     private float waveTime = 0f;

     private List<Vector2> bubbles = new ArrayList<>();
     private Random random = new Random();

     // Constructors

     public WaterTank(Vector2 position, int width, int height, float rotation) {
          super(
               position,
               new BoxCollider(position, width, height, rotation),
               Medium.WATER,
               new Rectangle(width, height, (float) Math.toRadians(rotation))
          );

          createBubbles(width, height);
     }

     public WaterTank(float x, float y, int width, int height, float rotation) {
          Vector2 pos = new Vector2(x, y);

          super(
               pos,
               new BoxCollider(pos, width, height, rotation),
               Medium.WATER,
               new Rectangle(width, height, (float) Math.toRadians(rotation))
          );

          createBubbles(width, height);
     }

     // Initializes bubbles
     private void createBubbles(int width, int height) {
          for (int i = 0; i < 30; i++) {
               float x =
                    random.nextFloat() * (width - 20)
                    - (width - 20) / 2f;

               float y =
                    random.nextFloat() * (height - 20)
                    - (height - 20) / 2f;

               bubbles.add(
                    new Vector2(x, y)
               );
          }
     }


     @Override
     public void update() {
          super.update();

          // Animates water waves
          waveTime += 0.05f;
     }

     @Override
     public void render(Graphics2D g) {
          super.render(g);

          Rectangle rectangle = (Rectangle) getCollider().getShape();
          float width = rectangle.getWidth();
          float height = rectangle.getHeight();

          Vector2 screenPosition =
               Scene.worldToScreen(getPosition());

          float cx = screenPosition.getX();
          float cy = screenPosition.getY();

          AffineTransform oldTransform = g.getTransform();
          Color oldColor = g.getColor();

          g.rotate(
               -collider.getRotation(),
               cx,
               cy
          );

          // Glass tank frame

          g.setColor(
               new Color(210, 250, 255, 110)
          );

          float glassThickness = 5f;

          // Left glass wall

          g.fillRect(
               (int) (cx - width / 2 - glassThickness),
               (int) (cy - height / 2),
               (int) glassThickness,
               (int) height
          );

          // Right glass wall

          g.fillRect(
               (int) (cx + width / 2),
               (int) (cy - height / 2),
               (int) glassThickness,
               (int) height
          );

          // Bottom glass wall

          g.fillRect(
               (int) (cx - width / 2 - glassThickness),
               (int) (cy + height / 2),
               (int) (width + glassThickness * 2),
               (int) glassThickness
          );

          // Glass top rim

          g.fillRect(
               (int) (cx - width / 2 - glassThickness),
               (int) (cy - height / 2 - glassThickness),
               (int) (width + glassThickness * 2),
               (int) glassThickness
          );

          // Bubbles

          g.setColor(
               new Color(220, 250, 255, 150)
          );

          for (int i = 0; i < bubbles.size(); i++) {
               Vector2 bubble = bubbles.get(i);

               float x = cx + bubble.getX();
               float y = cy + bubble.getY() - ((waveTime * 20f + i * 35f) % (height - 20));

               if (y < cy - height / 2 || y > cy + height / 2) {
                    continue;
               }

               float size = 4f;

               g.draw(
                    new Ellipse2D.Float(
                         x,
                         y,
                         size,
                         size
                    )
               );
          }

          g.setTransform(oldTransform);
          g.setColor(oldColor);
     }
}