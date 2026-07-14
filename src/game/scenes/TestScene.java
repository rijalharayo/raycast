package game.scenes;

import java.awt.Color;
import java.awt.Graphics2D;

import game.Scene;

public class TestScene extends Scene {

     @Override
     public void update() {
          // Code
     }

     @Override
     public void render(Graphics2D g) {
          g.setColor(Color.LIGHT_GRAY);
          g.fillRect(199, 100, 50, 20);
     }
     
}
