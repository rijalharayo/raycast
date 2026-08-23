package main.input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import main.game.Scene;
import main.math.algebra.Vector2;

// Handles mouse input
public class MouseInput implements MouseListener, MouseMotionListener {
     private static int mouseX;
     private static int mouseY;

     private static final boolean[] held = new boolean[4];
     private static final boolean[] pressed = new boolean[4];
     private static final boolean[] released = new boolean[4];

     @Override
     public void mousePressed(MouseEvent e) {
          int button = e.getButton();

          if (button < held.length) {
               if (!held[button]) {
                    pressed[button] = true;
               }

               held[button] = true;
          }
     }

     @Override
     public void mouseReleased(MouseEvent e) {
          int button = e.getButton();

          if (button < released.length) {
               held[button] = false;
               released[button] = true;
          }
     }

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

     public static boolean isHeld(int button) {
          return button < held.length && held[button];
     }

     public static boolean isPressed(int button) {
          return button < pressed.length && pressed[button];
     }

     public static boolean isReleased(int button) {
          return button < released.length && released[button];
     }

     public static void reset() {
          for (int i = 0; i < pressed.length; i++) {
               pressed[i] = false;
               released[i] = false;
          }
     }

     public static int getMouseScreenX() {
          return mouseX;
     }

     public static int getMouseScreenY() {
          return mouseY;
     }

     public static Vector2 getMousePosition() {
          Vector2 screenPos = new Vector2(mouseX, mouseY);
          return Scene.screenToWorld(screenPos);
     }

     public static Vector2 getMouseScreenPosition() {
          return new Vector2(mouseX, mouseY);
     }

     @Override
     public void mouseClicked(MouseEvent e) {}

     @Override
     public void mouseEntered(MouseEvent e) {}

     @Override
     public void mouseExited(MouseEvent e) {}
}