package main.models;

import main.math.Line;
import main.math.algebra.Vector2;

// Stores the data of a intersection of lines
public class IntersectionData {
     private Vector2 intersectionPoint;
     private Line incomingLine;
     private Line targetLine;

     // Constructors
     public IntersectionData(Vector2 intersectionPoint, Line incomingLine, Line targetLine) {
          this.intersectionPoint = intersectionPoint;
          this.targetLine = targetLine;
          this.incomingLine = incomingLine;
     }

     // Getters
     public Vector2 getIntersectionPoint() {
          return intersectionPoint;
     }

     public Line getTargetLine() {
          return targetLine;
     }

     public Line getIncomningLine() {
          return incomingLine;
     }
}
