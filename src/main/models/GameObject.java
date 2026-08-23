package main.models;

import java.awt.Graphics2D;

import main.math.algebra.Vector2;
import main.physics.colliders.Collider;

// Model representing the components that can exist within a scene
public abstract class GameObject {
     protected Vector2 position = Vector2.ZERO;
     protected String name = "";
     protected Sprite sprite;
     protected Collider collider;

     private boolean displayCollider = false;

     // Overloaded constructors
     public GameObject() {};

     public GameObject(String name) {
          this.name = (name == null || name.isBlank())
                    ? getClass().getSimpleName()
                    : name;
     }

     public GameObject(String name, Vector2 position, Collider collider) {
          this.name = (name == null || name.isBlank())
                    ? getClass().getSimpleName()
                    : name;

          this.position = position;
          this.collider = collider;
     }

     public GameObject(String name, float x, float y, Collider collider) {
          this.name = (name == null || name.isBlank())
                    ? getClass().getSimpleName()
                    : name;

          this.position = new Vector2(x, y);
          this.collider = collider;
     }

     // Main getters
     public String getName() {
          return name;
     }

     public Vector2 getPosition() {
          return position;
     }

     public Sprite getSprite() {
          return sprite;
     }

     public Collider getCollider() {
          return collider;
     }

     // Main setters
     public void setName(String name) {
          if(name == null) {
               throw new IllegalArgumentException("GameObject name can't be null");
          }

          this.name = name;
     }

     public void setPosition(Vector2 pos) {
          this.position = pos;
     }
     
     public void setPosition(float x, float y) {
          this.position = new Vector2(x, y);
     }

     public void setSprite(Sprite sprite) {
          if(sprite == null) {
               throw new IllegalArgumentException("Sprite can't be null");
          }

          this.sprite = sprite;
     }

     public void setRotation(float angle) {
          if(sprite == null) {
               throw new NullPointerException("No sprite set");
          }
          sprite.setRotation(angle);

          if(collider != null) {
               collider.setRotation((float) Math.toRadians(angle));
          }
     }

     public void rotate(float angle) {
          if(sprite == null) {
               throw new NullPointerException("No sprite set");
          }
          sprite.rotate(angle);

          if(collider != null) {
               collider.setRotation(collider.getRotation() + (float) Math.toRadians(angle));
          }
     }

     public void showCollider() {
          this.displayCollider = true;
     }

     public void hideCollider() {
          this.displayCollider = false;
     }

     public void render(Graphics2D g) {
          // Renders the sprite
          if(sprite != null) this.sprite.draw(g, position.getX(), position.getY());
          // Draw collider if it's assigned
          if(displayCollider && collider != null) collider.draw(g);
     };

     // Abstract methods
     public abstract void update();

     @Override
     public String toString() {
          return "( " +  getName() + ", " + position + " )";
     }
}