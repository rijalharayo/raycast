package main.input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import main.game.Scene;
import main.math.Vector2;

// Tracks mouse & keyboard inputs
public class MouseInput implements MouseMotionListener {
     private static int mouseX;
     private static int mouseY;

     @Override
     public void mouseMoved(MouseEvent e) {
          mouseX = e.getX();
          mouseY = e.getY();
     }

     @Override
     public void mouseDragged(MouseEvent e) {
          mouseX = e.getX();
          mouseY = e.getY();
     }

     public static int getMouseScreenX() {
          return mouseX;
     }

     public static int getMouseScreenY() {
          return mouseY;
     }

     public static Vector2 getMousePosition() {
          Vector2 screenPos = new Vector2(getMouseScreenX(), getMouseScreenY());
          return Scene.screenToWorld(screenPos);
     }

     public static Vector2 getMouseScreenPosition() {
          return new Vector2(mouseX, mouseY);
     }
}