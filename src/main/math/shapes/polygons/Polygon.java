package main.math.shapes.polygons;

import java.util.List;
import java.util.ArrayList;

import main.math.Line;
import main.math.Vector2;
import main.math.shapes.IntersectionData;
import main.math.shapes.Shape;

public class Polygon extends Shape {
     // Vertices are local to the position
     private Vector2[] localVertices = new Vector2[] {};
     // Edge's are stored in world coordinates
     private Line[] edges = new Line[] {};
     // Flag to check if the vertices have been modifed or not
     private boolean dirty = false;

     // Constructors
     protected Polygon() {} // Default implicit constructor

     public Polygon(Vector2 position, Vector2... localVertices) {
          super(position);
          
          // Only build edges & vertices if there are a minimum of 3 vertices given
          if(localVertices.length < 3) throw new IllegalArgumentException("A polygon must have at least 3 vertices");

          // Builds the edges
          rebuildEdges();
     }

     public Polygon(Vector2 position, float rotation, Vector2... localVertices) {
          super(position, rotation);
          
          // Only build edges & vertices if there are a minimum of 3 vertices given
          if(localVertices.length < 3) throw new IllegalArgumentException("A polygon must have at least 3 vertices");

          // Builds the edges
          rebuildEdges();
     }

     // Getters
     public Vector2[] getLocalVertices() { 
          return localVertices.clone();
     }

     public Vector2[] getWorldVertices() {
          List<Vector2> wVertices = new ArrayList<>();
          // Converts all local vertices to global vertices
          for(Vector2 vertex : localVertices) {
               wVertices.add(getWorldCoordinateVertex(vertex));
          }

          return wVertices.toArray(new Vector2[0]);
     }

     public Vector2 getLocalVertex(int index) {
          if(index > localVertices.length) throw new IndexOutOfBoundsException("Polygon only has " + localVertices.length + " vertices");
          return localVertices[index - 1];
     }

     public Vector2 getGlobalVertex(int index) {
          if(index > localVertices.length) throw new IndexOutOfBoundsException("Polygon only has " + localVertices.length + " vertices");
          return getWorldCoordinateVertex(localVertices[index - 1]);
     }

     // Converts polygon local vertex to world coordinate vertex
     public Vector2 getWorldCoordinateVertex(Vector2 v) {
          return this.position.add(v);
     }

     // Rebuilds edges based on vertices
     private void rebuildEdges() {
          List<Line> faces = new ArrayList<>();
          // Get last & first vertices in world/global coordinate
          Vector2 lastVertex = getWorldCoordinateVertex(localVertices[localVertices.length - 1]);
          Vector2 firstVertex = getWorldCoordinateVertex(localVertices[0]);
          // Edge from last to first vertex
          Line lastFirstEdge = new Line(lastVertex, firstVertex);
          faces.add(lastFirstEdge);

          // Builds the polygon's edges from consecutive vertices
          for(int i = 0; i < localVertices.length - 1; i++) {
               // Gets the consequitive vertices in world coordinates
               Vector2 v1 = getWorldCoordinateVertex(localVertices[i]);
               Vector2 v2 = getWorldCoordinateVertex(localVertices[i + 1]);
               // Adds the edge
               faces.add(
                    new Line(v1, v2)
               );
          }

          // Convert list to array
          this.edges = faces.toArray(new Line[0]);
     }

     @Override
     public void rotate(float theta) {
          // Rotates all vertices
          for(int i = 0; i < localVertices.length; i++) {
               localVertices[i] = localVertices[i].rotate(theta);
          }
          // Vertices have been modifed
          dirty = true;
     }

     @Override
     public void rotateAround(Vector2 localCenter, float theta) {
          // Rotates all vertices
          for(int i = 0; i < localVertices.length; i++) {
               localVertices[i] = localVertices[i].rotateAround(localCenter, theta);
          }
          // Vertices have been modifed
          dirty = true;
     }

     @Override
     public Line[] getEdges() {
          // If the vertices have been modifed, rebuild edges before returing
          if(dirty) {
               rebuildEdges();
               dirty = false;
          }

          return edges.clone();
     }

     @Override
     public IntersectionData intersects(Line line) {
          // Checks intersection of the line between all edges
          for(Line edge : edges) {
               IntersectionData iData = edge.intersects(line);
               // If the data isn't null, the lines have intersected somewhere
               if(iData != null) {
                    return iData;
               }
          }
          // Returns null if there is no intersection
          return null;
     }    
}
