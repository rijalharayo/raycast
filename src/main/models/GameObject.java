package main.models;

import java.awt.Graphics2D;

import main.math.algebra.Vector2;

// Model representing the components that can exist within a scene
public abstract class GameObject {
     protected Vector2 position = Vector2.ZERO;
     protected String name = "";
     protected Sprite sprite;

     // Overloaded constructors
     public GameObject() {};

     public GameObject(String name) {
          this.name = (name == null || name.isBlank())
                    ? getClass().getSimpleName()
                    : name;
     }

     public GameObject(String name, Vector2 position) {
          this.name = (name == null || name.isBlank())
                    ? getClass().getSimpleName()
                    : name;

          this.position = position;
     }

     public GameObject(String name, float x, float y) {
          this.name = (name == null || name.isBlank())
                    ? getClass().getSimpleName()
                    : name;

          this.position = new Vector2(x, y);
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
     }

     public void rotate(float angle) {
          if(sprite == null) {
               throw new NullPointerException("No sprite set");
          }
          sprite.rotate(angle);
     }

     // Abstract methods
     public abstract void update();
     public abstract void render(Graphics2D g);

     @Override
     public String toString() {
          return "( " +  getName() + ", " + position + " )";
     }
}