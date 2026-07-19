package main.game;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import main.math.Vector2;
import main.models.GameObject;
import main.models.Sprite;

public abstract class Scene {

     protected final List<GameObject> gameObjects = new ArrayList<>();
     protected Sprite background;

     // Adds object to the list
     public void add(GameObject gameObject) {
          if(gameObject == null) {
               throw new IllegalArgumentException("Object can't be null");
          }

          gameObjects.add(gameObject);
     }
     
     // Removes object from the list
     public void remove(GameObject gameObject) {
          if(gameObject == null) {
               throw new IllegalArgumentException("Object can't be null");
          }

               gameObjects.remove(gameObject);
     }

     // Updates the scene logic every frame
     public void update() {
          for(GameObject obj : gameObjects) {
               obj.update();
          }
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