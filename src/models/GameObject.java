package models;

import java.awt.Graphics2D;

import math.Vector2;

// Model representing the components that can exist within a scene
public abstract class GameObject {
     private Vector2 position;
     private String name;

     // Overloaded constructors
     public GameObject() {
          this.name = "";
          this.position = Vector2.ZERO;
     }

     public GameObject(String name) {
          this.name = (name == null || name.isBlank())
                    ? getClass().getSimpleName()
                    : name;

          position = Vector2.ZERO;
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

     public Vector2 getPos() {
          return position;
     }

     // Main setters
     public void setName(String name) {
          if(name == null) {
               throw new IllegalArgumentException("GameObject name can't be null");
          }

          this.name = name;
     }

     public void setPos(Vector2 pos) {
          this.position = pos;
     }
     
     public void setPos(float x, float y) {
          this.position = new Vector2(x, y);
     }

     // Abstract methods
     public abstract void update();
     public abstract void render(Graphics2D g);

     @Override
     public String toString() {
          return "( " +  getName() + ", " + position + " )";
     }
}