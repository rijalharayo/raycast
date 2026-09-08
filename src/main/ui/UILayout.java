package main.ui;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import main.math.algebra.Vector2;

// Different layouts of the ui
public abstract class UILayout {
     protected List<UIComponent> components = new ArrayList<>();
     protected Vector2 position = Vector2.ZERO;

     public void addUIComponent(UIComponent component) {
          components.add(component);
     }

     public void removeUIComponent(UIComponent component) {
          components.remove(component);
     }

     // Getters
     public List<UIComponent> getUIComponents() {
          return components;
     }

     public Vector2 getPosition() {
          return this.position;
     }

     // Setters
     public void setPosition(float x, float y) {
          this.position = new Vector2(x, y);
     }

     public abstract void layout();

     // Updates it's components
     public void update() {
          for (UIComponent component : components) {
               component.update();
          }
     }

     // Renders the components
     public void render(Graphics2D g) {
          for (UIComponent component : components) {
               component.render(g);
          }
     }
}
