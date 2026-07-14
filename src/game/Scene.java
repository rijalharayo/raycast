package game;

import java.awt.Graphics2D;

public abstract class Scene {

     // Updates the scene logic every frame
     public abstract void update();

     // Draws the scene to the screen
     public abstract void render(Graphics2D g);
}