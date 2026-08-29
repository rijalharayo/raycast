package main.game;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import main.math.algebra.Vector2;
import main.models.GameObject;
import main.models.Sprite;
import main.models.environment.OpticalObject;

public abstract class Scene {

     protected final List<GameObject> gameObjects = new ArrayList<>();
     protected final List<GameObject> objectsToAdd = new ArrayList<>();
     protected final List<GameObject> objectsToRemove = new ArrayList<>();
     protected final List<OpticalObject> opticalObjects = new ArrayList<>();

     protected Sprite background;

     // Updates queued objects state
     private void updatedQueuedObjects() {
          for(GameObject obj : objectsToAdd) {
               gameObjects.add(obj);

               if(obj instanceof OpticalObject) {
                    opticalObjects.add((OpticalObject) obj);
               }
          }

          for(GameObject obj : objectsToRemove) {
               gameObjects.remove(obj);

               // If the game object is an optical object, remove it from the optical objects list as well
               if(obj instanceof OpticalObject) {
                    opticalObjects.remove((OpticalObject) obj);
               }
          }

          objectsToAdd.clear();
          objectsToRemove.clear();
     }

     // Adds object to the list
     public void add(GameObject gameObject) {
          if(gameObject == null) {
               throw new IllegalArgumentException("Object can't be null");
          }

          objectsToAdd.add(gameObject);
     }
     
     // Removes object from the list
     public void remove(GameObject gameObject) {
          if(gameObject == null) {
               throw new IllegalArgumentException("Object can't be null");
          }

          objectsToRemove.add(gameObject);
     }

     public OpticalObject[] getSceneOpticalObjects() {
          return opticalObjects.toArray(new OpticalObject[0]);
     }

     // Updates the scene logic every frame
     public void update() {
          // Update all game objects
          for(GameObject obj : gameObjects) {
               obj.update();
          }

          updatedQueuedObjects();
     }

     // Draws the scene to the screen
     public void render(Graphics2D g) {
          for(GameObject obj : gameObjects) {
               obj.render(g);
          }
     }

     // Converts world coordinates to screen coordinates
     public static Vector2 worldToScreen(Vector2 worldCoordinate) {
          return new Vector2(
               worldCoordinate.getX() + Game.WORLD_CENTER.getX(),
               Game.WORLD_CENTER.getY() - worldCoordinate.getY()
          );
     }

     // Converts screen coordinates to world coordinates
     public static Vector2 screenToWorld(Vector2 screenCoordinate) {
          return new Vector2(
               screenCoordinate.getX() - Game.WORLD_CENTER.getX(),
               Game.WORLD_CENTER.getY() - screenCoordinate.getY()
          );
     }

     // Getters
     public Sprite getBackground() {
          return background;
     }
}