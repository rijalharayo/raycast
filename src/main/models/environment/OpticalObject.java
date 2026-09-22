package main.models.environment;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import main.input.KeyboardInput;
import main.input.MouseInput;
import main.math.Line;
import main.math.algebra.Vector2;
import main.models.GameObject;
import main.models.data.IntersectionData;
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
     private boolean isRotatable = true;
     private Vector2 dragOffset;
     private boolean dragging = false;
     private boolean rotating = false;

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

     public void setRotatable(boolean v) {
          this.isRotatable = v;
     }

     public void setModifiable(boolean v) {
          this.isDraggable = v;
          this.isRotatable = v;
     }

     // Getters

     public OpticalObjectType getOpticType() {
          return opticalObjectType;
     }

     @Override
     public LightRay interact(LightRay ray, CollisionData collisionData) {
          if(collisionData == null) return null;
          return this.interactWithRay(ray, collisionData.getIntersectionData());
     }

     @Override
     public SurfaceData calculateSurfaceData(IntersectionData intersectionData) {
          Line targetLine = intersectionData.getTargetLine();

          Vector2 normal = targetLine.getNormal().getNormalized();
          Vector2 incidentVector = intersectionData.getIncomningLine().getLineVector();

          // If the incident ray & normal face the same direction, invert it
          if(incidentVector.dot(normal) > 0) {
               normal = normal.multiply(-1f);
          }

          // The object's surface is the collider's edge
          SurfaceData objectSurface = new SurfaceData(targetLine.getLineVector().getNormalized(), normal);

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

               // Print the current postion if ';' is pressed while hovering
               if(KeyboardInput.isPressed(KeyEvent.VK_F3)) {
                    System.out.println(name + " position: " + this.position);
               }
               
               // Dragging & rotation can't be done at the same time
               if(MouseInput.isPressed(MouseEvent.BUTTON1) && isDraggable && !dragging && !rotating) {
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

          // Rotate only if it's being hovered over & not dragged
          if(isHoveringOver && !dragging && isRotatable) {
               boolean rotatingAntiClockwise = KeyboardInput.isHeld(KeyEvent.VK_Q);
               boolean rotatingClockwise = KeyboardInput.isHeld(KeyEvent.VK_E);
               rotating = rotatingAntiClockwise || rotatingClockwise;

               if(rotatingClockwise) {
                    rotateOpticalObject(1f);
               }

               if(rotatingAntiClockwise) {
                    rotateOpticalObject(-1f);
               }
          }
          else {
               rotating = false;
          }
     }

     // Drags optical object along mouse
     private void drag() {
          if(isDraggable) {
               // The environment has been modified
               this.getObjectLevelScene().setDirtyEnvironment(true);

               Vector2 targetPos = MouseInput.getMousePosition().subtract(dragOffset);
               setPosition(position.lerp(targetPos, 0.15f));
          }
     }

     // Rotates the optical object
     private void rotateOpticalObject(float sign) {
          if(isRotatable) {
               // The environment has been modified
               this.getObjectLevelScene().setDirtyEnvironment(true);

               rotate(Math.signum(sign) * 1.15f);
          }
     }
     
     // Abstract methods

     // Interacts & returns data when a ray collides
     protected abstract LightRay interactWithRay(LightRay ray, IntersectionData intersectionData);
}
