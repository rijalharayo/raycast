package main.math.shapes.polygons;

import java.util.List;
import java.util.ArrayList;

import main.math.Line;
import main.math.Vector2;
import main.math.shapes.Shape;
import main.models.IntersectionData;

public class Polygon extends Shape {
     // Vertices are local to the shape
     protected Vector2[] localVertices = new Vector2[] {};

     // Edges are stored in local coordinates
     private Line[] localEdges = new Line[] {};

     // Flag to check if the vertices have been modified or not
     private boolean dirty = false;


     // Constructors
     protected Polygon() {} // Default implicit constructor

     public Polygon(Vector2... localVertices) {
          // Only build edges & vertices if there are a minimum of 3 vertices given
          if(localVertices.length < 3) throw new IllegalArgumentException("A polygon must have at least 3 vertices");

          this.localVertices = localVertices.clone();

          // Builds the edges
          rebuildEdges();
     }

     public Polygon(float rotation, Vector2... localVertices) {
          // Only build edges & vertices if there are a minimum of 3 vertices given
          if(localVertices.length < 3) throw new IllegalArgumentException("A polygon must have at least 3 vertices");

          this.localVertices = localVertices.clone();

          // Rotates all vertices
          rotate(rotation);

          // Builds the edges
          rebuildEdges();
     }


     // Getters
     public Vector2[] getLocalVertices() { 
          return localVertices.clone();
     }

     public Vector2[] getWorldVertices(Vector2 parentPosition) {
          List<Vector2> wVertices = new ArrayList<>();

          // Converts all local vertices to world vertices
          for(Vector2 vertex : localVertices) {
               wVertices.add(
                    getWorldCoordinateVertex(parentPosition, vertex)
               );
          }

          return wVertices.toArray(new Vector2[0]);
     }

     public Vector2 getLocalVertex(int index) {
          if(index > localVertices.length) throw new IndexOutOfBoundsException("Polygon only has " + localVertices.length + " vertices");
          return localVertices[index - 1];
     }

     public Vector2 getWorldVertex(int index, Vector2 parentPosition) {
          if(index > localVertices.length) throw new IndexOutOfBoundsException("Polygon only has " + localVertices.length + " vertices");

          return getWorldCoordinateVertex(
               parentPosition,
               localVertices[index - 1]
          );
     }

     // Converts polygon local vertex to world coordinate vertex
     public static Vector2 getWorldCoordinateVertex(Vector2 parentPosition, Vector2 v) {
          return parentPosition.add(v);
     }

     // Rebuilds edges based on vertices
     private void rebuildEdges() {
          List<Line> faces = new ArrayList<>();

          // Edge from last to first vertex
          faces.add(
               new Line(
                    localVertices[localVertices.length - 1],
                    localVertices[0]
               )
          );

          // Builds the polygon's edges from consecutive vertices
          for(int i = 0; i < localVertices.length - 1; i++) {
               // Gets the consecutive vertices in local coordinates
               Vector2 v1 = localVertices[i];
               Vector2 v2 = localVertices[i + 1];

               // Adds the edge
               faces.add(
                    new Line(v1, v2)
               );
          }

          // Convert list to array
          this.localEdges = faces.toArray(new Line[0]);
     }


     // Returns the edges converted to world coordinates
     public Line[] getWorldEdges(Vector2 parentPosition) {
          Line[] worldEdges = new Line[localEdges.length];

          for(int i = 0; i < localEdges.length; i++) {

               Line edge = localEdges[i];

               worldEdges[i] = new Line(
                    getWorldCoordinateVertex(parentPosition, edge.getStart()),
                    getWorldCoordinateVertex(parentPosition, edge.getEnd())
               );
          }

          return worldEdges;
     }


     @Override
     public void rotate(float theta) {
          // Rotates all vertices
          for(int i = 0; i < localVertices.length; i++) {
               localVertices[i] = localVertices[i].rotate(theta);
          }

          // Vertices have been modified
          dirty = true;
     }


     @Override
     public void rotateAround(Vector2 localCenter, float theta) {
          // Rotates all vertices
          for(int i = 0; i < localVertices.length; i++) {
               localVertices[i] = localVertices[i].rotateAround(localCenter, theta);
          }

          // Vertices have been modified
          dirty = true;
     }


     @Override
     public Line[] getEdges() {
          // If the vertices have been modified, rebuild edges before returning
          if(dirty) {
               rebuildEdges();
               dirty = false;
          }

          return localEdges.clone();
     }


     @Override
     public IntersectionData intersects(Line line, Vector2 parentPostion) {
          // Checks intersection of the line between all edges
          for(Line edge : getWorldEdges(parentPostion)) {

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