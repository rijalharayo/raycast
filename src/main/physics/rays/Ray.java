package main.physics.rays;

import main.math.Vector2;
import main.models.RayData;

// Interface for rays
public interface Ray {

     RayData getRayData();

     default Vector2 getStart() {
          return getRayData().getStart();
     }

     default Vector2 getEnd() {
          return getRayData().getEnd();
     }

     default InternalRayData internalData() {
          return (InternalRayData) getRayData();
     }

     default void setPoints(Vector2 start, Vector2 end) {
          internalData().set(start, end);
     }

     default void setPoints(float x1, float y1, float x2, float y2) {
          internalData().set(x1, y1, x2, y2);
     }

     default void setFromDirection(Vector2 start, float angle, float length) {
          internalData().set(start, angle, length);
     }
}

// An internal ray data class
// Its only visible within the rays package
// Used to hide methods of RayData publicly to other packages
class InternalRayData extends RayData {
     void set(Vector2 start, Vector2 end) {
          setPoints(start, end);
     }

     void set(float x1, float y1, float x2, float y2) {
          setPoints(x1, y1, x2, y2);
     }

     void set(Vector2 start, float angle, float length) {
          setFromDirection(start, angle, length);
     }
}