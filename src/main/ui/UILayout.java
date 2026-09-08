package main.ui;

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
}
