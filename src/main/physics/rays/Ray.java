package main.physics.rays;

import main.math.algebra.Vector2;
import main.models.data.RayData;

// Interface for rays
public interface Ray {

     RayData getRayData();

     default Vector2 getStart() {
          return getRayData().getStart();
     }

     default Vector2 getEnd() {
          return getRayData().getEnd();
     }

     default void setPoints(Vector2 start, Vector2 end) {
          getRayData().setPoints(start, end);
     }

     default void setPoints(float x1, float y1, float x2, float y2) {
          getRayData().setPoints(x1, y1, x2, y2);
     }

     default void setFromDirection(Vector2 start, float angle, float length) {
          getRayData().setFromDirection(start, angle, length);
     }

     default void setFromDirection(Vector2 start, float angle) {
          getRayData().setFromDirection(start, angle);
     }
}