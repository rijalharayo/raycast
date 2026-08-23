package main.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

// Handles keyboard input
public class KeyboardInput implements KeyListener {
     private static final boolean[] held = new boolean[256];
     private static final boolean[] pressed = new boolean[256];
     private static final boolean[] released = new boolean[256];

     @Override
     public void keyPressed(KeyEvent e) {
          int key = e.getKeyCode();

          if (key < held.length && !held[key]) {
               pressed[key] = true;
          }

          if (key < held.length) {
               held[key] = true;
          }
     }

     @Override
     public void keyReleased(KeyEvent e) {
          int key = e.getKeyCode();

          if (key < released.length) {
               held[key] = false;
               released[key] = true;
          }
     }

     public static boolean isHeld(int keyCode) {
          return keyCode < held.length && held[keyCode];
     }

     public static boolean isPressed(int keyCode) {
          return keyCode < pressed.length && pressed[keyCode];
     }

     public static boolean isReleased(int keyCode) {
          return keyCode < released.length && released[keyCode];
     }

     public static void reset() {
          for (int i = 0; i < pressed.length; i++) {
               pressed[i] = false;
               released[i] = false;
          }
     }

     @Override
     public void keyTyped(KeyEvent e) {}
}