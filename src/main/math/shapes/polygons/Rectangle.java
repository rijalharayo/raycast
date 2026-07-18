package main.math.shapes.polygons;

import main.math.Vector2;

// Represents a mathematical rectangle
public class Rectangle extends Polygon {
     private int width = 1;
     private int height = 1;

     // Constructors
     public Rectangle(Vector2 position, int width, int height) {
          super(position, calculateLocalVertices(width, height, 0f));

          this.width = width;
          this.height = height;
     }

     public Rectangle(Vector2 position, int width, int height, float rotation) {
          super(position, rotation, calculateLocalVertices(width, height, rotation));

          this.width = width;
          this.height = height;
     }

     // Getters
     public int getWidth() {
          return this.width;
     }

     public int getHeight() {
          return this.height;
     }

     // Return the local vertices of the rectangle
     public Vector2 getTopLeft() {
          return this.getLocalVertex(1);
     }

     public Vector2 getTopRight() {
          return this.getLocalVertex(2);
     }

     public Vector2 getBottomRight() {
          return this.getLocalVertex(3);
     }

     public Vector2 getBottomLeft() {
          return this.getLocalVertex(4);
     }

     // Calculates the local vertices based on width, height & rotation
     public static Vector2[] calculateLocalVertices(int width, int height, float rotation) {
          float widthOffset = width / 2;
          float heightOffset = height / 2;

          Vector2 topLeft = new Vector2(-widthOffset, heightOffset).rotate(rotation);
          Vector2 topRight = new Vector2(widthOffset, heightOffset).rotate(rotation);

          Vector2 bottomLeft = new Vector2(-widthOffset, -heightOffset).rotate(rotation);
          Vector2 bottomRight = new Vector2(widthOffset, -heightOffset).rotate(rotation);

          return new Vector2[] { topLeft, topRight, bottomRight, bottomLeft };
     }
}
