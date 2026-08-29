package main.models.environment.refractors.glasses;

import main.game.SceneManager;
import main.math.algebra.Matrix2x2;
import main.math.algebra.Vector2;
import main.math.shapes.polygons.Rectangle;
import main.models.environment.absorbers.BlackAbsorber;
import main.models.environment.refractors.Refractor;
import main.physics.colliders.BoxCollider;
import main.physics.optics.Medium;

// Represents an optical fibre wire
public class OpticalFibre extends Refractor {
     private BlackAbsorber[] covers = new BlackAbsorber[2];
     private boolean inScene = false;
     
     // Constructors
     public OpticalFibre(Vector2 position, int width, int height, float rotation) {
          super(
               position,
               new BoxCollider(position, width, height, rotation),
               Medium.DENSE_GLASS,
               new Rectangle(width, height)
          );

          initializeCovers();
     }

     public OpticalFibre(float x, float y, int width, int height, float rotation) {
          Vector2 pos = new Vector2(x, y);

          super(
               pos,
               new BoxCollider(pos, width, height, rotation),
               Medium.DENSE_GLASS,
               new Rectangle(width, height)
          );

          initializeCovers();
     }

     // Initializes covers
     private void initializeCovers() {
          Rectangle rectangle = (Rectangle) getShape();

          float width = rectangle.getWidth();

          covers[0] = new BlackAbsorber(
               getCoverPosition(1),
               (int) width,
               25,
               (float) Math.toDegrees(getCollider().getRotation())
          );

          covers[1] = new BlackAbsorber(
               getCoverPosition(-1),
               (int) width,
               25,
               (float) Math.toDegrees(getCollider().getRotation())
          );

          covers[0].setDraggable(false);
          covers[1].setDraggable(false);
     }

     // Gets the position of a cover
     private Vector2 getCoverPosition(float direction) {
          Rectangle rectangle = (Rectangle) getShape();

          float height = rectangle.getHeight();
          float offset = height / 2f + 12.5f;
          float rotation = getCollider().getRotation();

          Vector2 offsetVector = new Vector2(
               0,
               offset * direction
          );

          Vector2 rotatedOffset = Matrix2x2.getRotationMatrix(rotation).transform(offsetVector);

          return getPosition().add(rotatedOffset);
     }

     // Updates cover positions and rotations
     private void updateCovers() {
          covers[0].setPosition(getCoverPosition(1));
          covers[1].setPosition(getCoverPosition(-1));

          if(SceneManager.getCurrentScene() != null && !inScene) {
               for(BlackAbsorber absorber : covers) {
                    SceneManager.getCurrentScene().add(absorber);
               }

               inScene = true;
          }
     }

     @Override
     public void update() {
          super.update();
          updateCovers();
     }
}
