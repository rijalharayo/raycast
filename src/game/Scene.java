package game;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import models.GameObject;

public abstract class Scene {

     protected final List<GameObject> gameObjects = new ArrayList<>();

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
}