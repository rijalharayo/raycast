package main.ui;

import java.awt.Graphics2D;

import main.math.algebra.Vector2;

// Represents different components of the ui
public abstract class UIComponent {
     protected Vector2 position;
     protected int width, height;

     // Constructors
     public UIComponent() {}

     public UIComponent(float x, float y, int width, int height) {
          this.position = new Vector2(x, y);
          this.width = width;
          this.height = height;
     }

     public UIComponent(int width, int height) {
          this.position = Vector2.ZERO;
          this.width = width;
          this.height = height;
     }

     public UIComponent(float x, float y) {
          this.position = new Vector2(x, y);
          this.width = 1;
          this.height = 1;
     }

     // Setters
     public void setPosition(float x, float y) {
          this.position = new Vector2(x, y);
     }

     public void setSize(int width, int height) {
          this.width = width;
          this.height = height;
     }

     public abstract void render(Graphics2D g);
     // Renders at some other position
     public abstract void render(Graphics2D g, Vector2 position);
}
