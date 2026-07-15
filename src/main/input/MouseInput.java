package main.input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

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

     public static int getMouseX() {
          return mouseX;
     }

     public static int getMouseY() {
          return mouseY;
     }
}