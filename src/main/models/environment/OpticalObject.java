package main.models.environment;

import java.awt.event.MouseEvent;

import main.input.MouseInput;
import main.math.Line;
import main.math.algebra.Vector2;
import main.models.GameObject;
import main.models.data.IntersectionData;
import main.models.data.RayData;
import main.models.data.SurfaceData;
import main.models.entities.LightRay;
import main.physics.colliders.Collider;
import main.physics.colliders.CollisionData;
import main.physics.optics.OpticalObjectType;
import main.physics.optics.RayInteractable;

// Class representing optical objects that can alter light rays
public abstract class OpticalObject extends GameObject implements RayInteractable {
     private final OpticalObjectType opticalObjectType;
     private boolean isDraggable = true;
     private Vector2 dragOffset;
     private boolean dragging = false;

     // Constructors
     public OpticalObject(Vector2 position, Collider collider, OpticalObjectType oType) {
          super("", position, collider);
          this.opticalObjectType = oType;
     }

     public OpticalObject(String name, Vector2 position, Collider collider, OpticalObjectType oType) {
          super(name, position, collider);
          this.opticalObjectType = oType;
     }

     // Setters
     public void setDraggable(boolean v) {
          this.isDraggable = v;
     }

     // Getters

     public OpticalObjectType getOpticType() {
          return opticalObjectType;
     }

     @Override
     public RayData interact(LightRay ray, CollisionData collisionData) {
          if(collisionData == null) return null;
          return this.interactWithRay(ray, collisionData.getIntersectionData());
     }

     @Override
     public SurfaceData calculateSurfaceData(IntersectionData intersectionData) {
          Line targetLine = intersectionData.getTargetLine();

          Vector2 normal = targetLine.getNormal();
          Vector2 incidentVector = intersectionData.getIncomningLine().getLineVector();

          // If the incident ray & normal face the same direction, invert it
          if(incidentVector.dot(normal) > 0) {
               normal = normal.multiply(-1f);
          }

          // The object's surface is the collider's edge
          SurfaceData objectSurface = new SurfaceData(targetLine.getLineVector(), normal);

          return objectSurface;
     }

     @Override
     public void update() {
          Vector2 mousePosition = MouseInput.getMousePosition();
          // Flag to check if the mouse is hovering over
          boolean isHoveringOver = collider.containsPoint(mousePosition);

          // Hover behaviour
          if(isHoveringOver) {
               showCollider();

               if(MouseInput.isPressed(MouseEvent.BUTTON1) && isDraggable && !dragging) {
                    dragOffset = mousePosition.subtract(position);
                    dragging = true;
               }
          }
          else {
               hideCollider();
          }

          if(dragging) {
               // Drag behaviour
               if(MouseInput.isHeld(MouseEvent.BUTTON1) && dragOffset != null) {
                    drag();
               }

               // Mouse release
               if (MouseInput.isReleased(MouseEvent.BUTTON1)) {
                    dragOffset = null;
                    dragging = false;
               }
          }
     }

     // Drags optical object along mouse
     private void drag() {
          if(isDraggable) {
               Vector2 targetPos = MouseInput.getMousePosition().subtract(dragOffset);
               setPosition(position.lerp(targetPos, 0.15f));
          }
     }
     
     // Abstract methods

     // Interacts & returns data when a ray collides
     protected abstract RayData interactWithRay(LightRay ray, IntersectionData intersectionData);
}
