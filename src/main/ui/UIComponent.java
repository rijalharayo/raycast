package main.ui;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import main.input.MouseInput;
import main.math.algebra.Vector2;
import main.math.shapes.polygons.Rectangle;

// Represents different components of the ui
public abstract class UIComponent {
     protected Vector2 position;
     protected Rectangle dimensionShape;

     // Constructors
     public UIComponent() {}

     public UIComponent(float x, float y, int width, int height) {
          this.position = new Vector2(x, y);
          this.dimensionShape = new Rectangle(width, height);
     }

     public UIComponent(int width, int height) {
          this.position = Vector2.ZERO;
          this.dimensionShape = new Rectangle(width, height);
     }

     public UIComponent(float x, float y) {
          this.position = new Vector2(x, y);
          this.dimensionShape = new Rectangle(1, 1);
     }

     // Getters
     public int width() {
          return dimensionShape.getWidth();
     }

     public int height() {
          return dimensionShape.getHeight();
     }

     public Vector2 getPosition() {
          return this.position;
     }

     // Setters
     public void setPosition(float x, float y) {
          this.position = new Vector2(x, y);
     }

     public void setSize(int width, int height) {
          this.dimensionShape = new Rectangle(width, height);
     }

     // Updates components
     public void update() {
          if(dimensionShape != null) {
               if(dimensionShape.containsPoint(position, MouseInput.getMousePosition())) {
                    onHover();
                    if(MouseInput.isPressed(MouseEvent.BUTTON1)) {
                         onClick();
                    }
               }
               else {
                    setAsDefault();
               }
          }
     };

     protected void onHover() {
          // Do nothing
     }

     protected void onClick() {
          // Do nothing
     }

     protected void setAsDefault() {
          // Do nothing
     };

     public abstract void render(Graphics2D g);
     // Renders at some other position
     public void render(Graphics2D g, Vector2 position) {};
}
