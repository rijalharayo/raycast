package main.models;

import main.math.Line;
import main.math.Vector2;

// Stores the data of a intersection of lines
public class IntersectionData {
     private Vector2 intersectionPoint;
     private Line targetLine;
     private Line subjectLine;

     // Constructors
     public IntersectionData(Vector2 intersectionPoint, Line targetLine, Line subjectLine) {
          this.intersectionPoint = intersectionPoint;
          this.targetLine = targetLine;
          this.subjectLine = subjectLine;
     }

     // Getters
     public Vector2 getIntersectionPoint() {
          return intersectionPoint;
     }

     public Line getTargetLine() {
          return targetLine;
     }

     public Line getSubjectLine() {
          return subjectLine;
     }
}
