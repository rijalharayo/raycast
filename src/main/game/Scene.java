package main.game;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import main.game.scenes.LevelScene;
import main.input.KeyboardInput;
import main.input.MouseInput;
import main.math.algebra.Vector2;
import main.models.GameObject;
import main.physics.optics.RayInteractable;
import main.sprites.Sprite;
import main.ui.UIComponent;

public abstract class Scene {
     protected final List<GameObject> gameObjects = new ArrayList<>();
     protected final List<GameObject> objectsToAdd = new ArrayList<>();
     protected final List<GameObject> objectsToRemove = new ArrayList<>();
     protected final List<RayInteractable> rayInteractables = new ArrayList<>();
     protected final List<UIComponent> uiComponents = new ArrayList<>();

     private RayInteractable[] cachedRayInteractables;

     protected Sprite background;

     // Updates queued objects state
     private void updatedQueuedObjects() {
          for(GameObject obj : objectsToAdd) {
               gameObjects.add(obj);

               if(obj instanceof RayInteractable) {
                    rayInteractables.add((RayInteractable) obj);
                    // Reset cached data if the list is updated
                    cachedRayInteractables = null;
               }
          }

          for(GameObject obj : objectsToRemove) {
               gameObjects.remove(obj);

               // If the game object is a ray interactable, remove it from the ray interactable list as well
               if(obj instanceof RayInteractable) {
                    rayInteractables.remove((RayInteractable) obj);
                    // Reset cached data if the list is updated
                    cachedRayInteractables = null;
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

          // If it's in a level, set its scene
          if (this instanceof LevelScene levelScene) {
               gameObject.setLevelScene(levelScene);
          }

          objectsToAdd.add(gameObject);
     }
     
     // Removes object from the list
     public void remove(GameObject gameObject) {
          if(gameObject == null) {
               throw new IllegalArgumentException("Object can't be null");
          }

          // If it was in some level, remove it's scene
          if (this instanceof LevelScene) {
               gameObject.setLevelScene(null);
          }

          objectsToRemove.add(gameObject);
     }

     // Adds ui component to the list
     public void add(UIComponent uiComponent) {
          if(uiComponent == null) {
               throw new IllegalArgumentException("UI Component can't be null");
          }

          uiComponents.add(uiComponent);
     }

     public RayInteractable[] getSceneRayInteractables() {
          // Cache array if it's modified or new
          if(cachedRayInteractables == null) {
               cachedRayInteractables = rayInteractables.toArray(new RayInteractable[0]);
          }

          return cachedRayInteractables;
     }

     // Updates the scene logic every frame
     public void update() {
          // Update all game objects
          for(GameObject obj : gameObjects) {
               obj.update();
          }

          // Update all ui components
          for(UIComponent uiComponent : uiComponents) {
               uiComponent.update();
          }

          // Print the current mouse world position if 'P' is pressed
          if(KeyboardInput.isPressed(KeyEvent.VK_P)) {
               Vector2 mouseWorld = MouseInput.getMousePosition();
               System.out.println("Mouse position: " + mouseWorld);
          }

          updatedQueuedObjects();
     }

     // Draws the scene to the screen
     public void render(Graphics2D g) {
          for(GameObject obj : gameObjects) {
               obj.render(g);
          }

          for(UIComponent uiComponent : uiComponents) {
               uiComponent.render(g);
          }
     }

     // Initialization method of Scenes
     public static void initailize() {};

     // Converts world coordinates to screen coordinates
     public static Vector2 worldToScreen(Vector2 worldCoordinate) {
          float scale = Game.getWorldScale();
          float offsetX = Game.getWorldOffsetX();
          float offsetY = Game.getWorldOffsetY();

          float virtualX = worldCoordinate.getX() + Game.WORLD_CENTER.getX();
          float virtualY = Game.WORLD_CENTER.getY() - worldCoordinate.getY();

          return new Vector2(
               virtualX * scale + offsetX,
               virtualY * scale + offsetY
          );
     }

     // Converts screen coordinates to world coordinates
     public static Vector2 screenToWorld(Vector2 screenCoordinate) {
          float scale = Game.getWorldScale();
          float offsetX = Game.getWorldOffsetX();
          float offsetY = Game.getWorldOffsetY();

          float virtualX = (screenCoordinate.getX() - offsetX) / scale;
          float virtualY = (screenCoordinate.getY() - offsetY) / scale;

          return new Vector2(
               virtualX - Game.WORLD_CENTER.getX(),
               Game.WORLD_CENTER.getY() - virtualY
          );
     }

     // Getters
     public Sprite getBackground() {
          return background;
     }
}