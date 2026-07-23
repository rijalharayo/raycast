package main.math.shapes.polygons;

import main.math.algebra.Vector2;

// Represents a mathematical rectangle
public class Rectangle extends Polygon {
     private int width = 1;
     private int height = 1;

     // Constructors
     public Rectangle(int width, int height) {
          super(calculateLocalVertices(width, height));

          this.width = width;
          this.height = height;
     }

     public Rectangle(int width, int height, float rotation) {
          super(rotation, calculateLocalVertices(width, height));
     

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
     public static Vector2[] calculateLocalVertices(int width, int height) {
          float widthOffset = width / 2;
          float heightOffset = height / 2;

          Vector2 topLeft = new Vector2(-widthOffset, heightOffset);
          Vector2 topRight = new Vector2(widthOffset, heightOffset);

          Vector2 bottomLeft = new Vector2(-widthOffset, -heightOffset);
          Vector2 bottomRight = new Vector2(widthOffset, -heightOffset);

          return new Vector2[] { topLeft, topRight, bottomRight, bottomLeft };
     }
}
