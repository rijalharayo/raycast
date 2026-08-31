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
     private Cladding[] claddings = new Cladding[2];

     private boolean inScene = false;

     private static final int COVER_HEIGHT = 25;
     private static final int CLADDING_HEIGHT = 20;
     
     // Constructors
     public OpticalFibre(Vector2 position, int width, int height, float rotation) {
          super(
               position,
               new BoxCollider(position, width, height, rotation),
               Medium.DENSE_GLASS,
               new Rectangle(width, height)
          );

          initializeCovers();
          initializeCladdings();
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
          initializeCladdings();
     }

     // Initializes covers
     private void initializeCovers() {
          Rectangle rectangle = (Rectangle) getShape();

          float width = rectangle.getWidth();

          covers[0] = new BlackAbsorber(
               getCoverPosition(1),
               (int) width,
               COVER_HEIGHT,
               (float) Math.toDegrees(getCollider().getRotation())
          );

          covers[1] = new BlackAbsorber(
               getCoverPosition(-1),
               (int) width,
               COVER_HEIGHT,
               (float) Math.toDegrees(getCollider().getRotation())
          );

          covers[0].setDraggable(false);
          covers[1].setDraggable(false);
     }

     // Initializes claddings
     private void initializeCladdings() {
          Rectangle rectangle = (Rectangle) getShape();

          float width = rectangle.getWidth();
          float rotation = (float) Math.toDegrees(getCollider().getRotation());

          claddings[0] = new Cladding(
               getCladdingPosition(1),
               (int) width,
               CLADDING_HEIGHT,
               rotation
          );

          claddings[1] = new Cladding(
               getCladdingPosition(-1),
               (int) width,
               CLADDING_HEIGHT,
               rotation
          );

          claddings[0].setDraggable(false);
          claddings[1].setDraggable(false);
     }

     // Gets the position of a cover
     private Vector2 getCoverPosition(float direction) {
          Rectangle rectangle = (Rectangle) getShape();

          float height = rectangle.getHeight();
          float offset = height / 2f + CLADDING_HEIGHT +  (COVER_HEIGHT / 2);
          float rotation = getCollider().getRotation();

          Vector2 offsetVector = new Vector2(
               0,
               offset * direction
          );

          Vector2 rotatedOffset = Matrix2x2.getRotationMatrix(rotation).transform(offsetVector);

          return getPosition().add(rotatedOffset);
     }

     // Gets the position of a cladding
     private Vector2 getCladdingPosition(float direction) {
          Rectangle rectangle = (Rectangle) getShape();

          float height = rectangle.getHeight();
          float offset = height / 2f + (CLADDING_HEIGHT / 2);
          float rotation = getCollider().getRotation();

          Vector2 offsetVector = new Vector2(
               0,
               offset * direction
          );

          Vector2 rotatedOffset = Matrix2x2.getRotationMatrix(rotation).transform(offsetVector);

          return getPosition().add(rotatedOffset);
     }

     // Updates cover & cladding positions and rotations
     private void updateComponents() {
          covers[0].setPosition(getCoverPosition(1));
          covers[1].setPosition(getCoverPosition(-1)); 

          claddings[0].setPosition(getCladdingPosition(1));
          claddings[1].setPosition(getCladdingPosition(-1));

          if(SceneManager.getCurrentScene() != null && !inScene) {
               for(Cladding cladding : claddings) {
                    SceneManager.getCurrentScene().add(cladding);
               }

               for(BlackAbsorber absorber : covers) {
                    SceneManager.getCurrentScene().add(absorber);
               }

               inScene = true;
          }
     }

     @Override
     public void update() {
          super.update();
          updateComponents();
     }
}

// Cladding of the optical fibre
class Cladding extends Refractor {
     // Constructors
     public Cladding(Vector2 position, int width, int height, float rotation) {
          super(
               position,
               new BoxCollider(position, width, height, rotation),
               Medium.FLINT_GLASS,
               new Rectangle(width, height)
          );
     }
}