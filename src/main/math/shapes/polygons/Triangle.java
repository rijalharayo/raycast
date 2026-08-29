package main.math.shapes.polygons;

import main.math.algebra.Vector2;

// A mathematical object representing a triangle
public class Triangle extends Polygon {

     // Constructors
     public Triangle(Vector2 ...localVertices) {
          if(localVertices.length != 3) {
               throw new IllegalArgumentException("A triangle must have 3 vertices");
          }

          super(localVertices);
     }

     public Triangle(float rotation, Vector2 ...localVertices) {
          if(localVertices.length != 3) {
               throw new IllegalArgumentException("A triangle must have 3 vertices");
          }

          super(rotation, localVertices);
     }
}
