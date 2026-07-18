package main.math.shapes.polygons;

import java.util.List;
import java.util.ArrayList;

import main.math.Line;
import main.math.Shape;
import main.math.Vector2;
import main.math.shapes.IntersectionData;

public class Polygon extends Shape {
     // Vertices are local to the position
     private Vector2[] vertices;
     // Edge's are stored in world coordinates
     private Line[] edges;
     // Flag to check if the vertices have been modifed or not
     private boolean dirty = false;

     public Polygon(Vector2 position, Vector2... localVertices) {
          super(position);
          
          // Only build edges & vertices if there are a minimum of 3 vertices given
          if(localVertices.length < 3) throw new IllegalArgumentException("A polygon must have at least 3 vertices");

          // Builds the edges
          rebuildEdges();
     }

     // Getters
     public Vector2[] getVertices() { 
          return vertices.clone();
     }

     // Converts polygon local vertex to world coordinate vertex
     private Vector2 getWorldCoordinateVertex(Vector2 v) {
          return this.position.add(v);
     }

     // Rebuilds edges based on vertices
     private void rebuildEdges() {
          List<Line> faces = new ArrayList<>();
          // Get last & first vertices in world/global coordinate
          Vector2 lastVertex = getWorldCoordinateVertex(vertices[vertices.length - 1]);
          Vector2 firstVertex = getWorldCoordinateVertex(vertices[0]);
          // Edge from last to first vertex
          Line lastFirstEdge = new Line(lastVertex, firstVertex);
          faces.add(lastFirstEdge);

          // Builds the polygon's edges from consecutive vertices
          for(int i = 0; i < vertices.length - 1; i++) {
               // Gets the consequitive vertices in world coordinates
               Vector2 v1 = getWorldCoordinateVertex(vertices[i]);
               Vector2 v2 = getWorldCoordinateVertex(vertices[i + 1]);
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
          for(int i = 0; i < vertices.length; i++) {
               vertices[i] = vertices[i].rotate(theta);
          }
          // Vertices have been modifed
          dirty = true;
     }

     @Override
     public void rotateAround(Vector2 localCenter, float theta) {
          // Rotates all vertices
          for(int i = 0; i < vertices.length; i++) {
               vertices[i] = vertices[i].rotateAround(localCenter, theta);
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
