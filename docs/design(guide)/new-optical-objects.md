# Adding a New Optical Object

This guide explains how to create a new optical object in Ray Shooter.

Optical objects are objects that can interact with light rays. They inherit the common behavior provided by `OpticalObject` and only need to implement the optical interaction specific to the object.

---

## 1. Create the Optical Object Class

Create the class inside the appropriate environment package.

For example, a new reflector would belong under:

```text
src/main/models/environment/reflectors/
```

while a new refractor would belong under:

```text
src/main/models/environment/refractors/
```

The class should extend `OpticalObject`.

```java
public class NewOpticalObject extends OpticalObject {

}
```

---

## 2. Call the `OpticalObject` Constructor

`OpticalObject` provides two constructors.

The first allows an object without a name:

```java
public OpticalObject(
     Vector2 position,
     Collider collider,
     OpticalObjectType oType
)
```

The second also provides a name:

```java
public OpticalObject(
     String name,
     Vector2 position,
     Collider collider,
     OpticalObjectType oType
)
```

A new optical object should call one of these constructors using `super(...)`.

For example:

```java
public NewOpticalObject(
     Vector2 position,
     Collider collider
) {
     super(
          "New Object",
          position,
          collider,
          OpticalObjectType.REFLECTOR
     );
}
```

The constructor establishes the common `GameObject` state, collider, position, and optical object type.

---

## 3. Choose the Optical Object Type

Every `OpticalObject` has an `OpticalObjectType`.

This identifies the general category of optical interaction performed by the object.

The type is supplied when calling the superclass constructor.

```text
New Optical Object
        ↓
OpticalObject
        ↓
OpticalObjectType
```

The appropriate type should be selected according to the object's optical behavior.

---

## 4. Provide a Collider

Every optical object requires a `Collider`.

The collider represents the physical geometry that rays can intersect.

For example:

```text
OpticalObject
      │
      └── Collider
             │
             └── Shape
```

The collider is responsible for geometric intersection testing, while the optical object is responsible for deciding what happens when a ray interacts with that geometry.

This keeps geometry and optical behavior separate.

---

## 5. Implement `interactWithRay()`

The most important method when creating a new optical object is:

```java
protected abstract LightRay interactWithRay(
     LightRay ray,
     IntersectionData intersectionData
);
```

Every concrete optical object must implement it.

This method defines what happens when a light ray hits the object.

For example:

```java
@Override
protected LightRay interactWithRay(
     LightRay ray,
     IntersectionData intersectionData
) {
     // Calculate optical interaction
     // Return resulting ray
}
```

The method receives:

* the incoming `LightRay`
* information about the intersection

and returns the resulting `LightRay`.

---

## 6. Ray Interaction Flow

The general interaction pipeline is:

```text
LightRay
    ↓
Ray intersection
    ↓
OpticalObject
    ↓
interact()
    ↓
CollisionData
    ↓
IntersectionData
    ↓
interactWithRay()
    ↓
New LightRay
```

`OpticalObject.interact()` acts as the bridge between collision detection and the object's optical behavior.

It extracts the `IntersectionData` from the collision:

```java
@Override
public LightRay interact(
     LightRay ray,
     CollisionData collisionData
) {
     if(collisionData == null)
          return null;

     return this.interactWithRay(
          ray,
          collisionData.getIntersectionData()
     );
}
```

Therefore, subclasses normally work with `IntersectionData` rather than directly handling collision detection.

---

## 7. Surface Data

Optical objects also provide surface information through:

```java
calculateSurfaceData()
```

This produces a `SurfaceData` containing the surface tangent and normal at the intersection.

The default implementation uses the collider's target line.

```text
Collider
   ↓
Target Line
   ├── Tangent
   └── Normal
          ↓
    SurfaceData
```

The normal is automatically oriented against the incoming ray.

This means optical-object implementations can use the resulting normal directly when calculating reflection or refraction.

---

## 8. Implementing the Optical Mathematics

The actual ray interaction belongs inside `interactWithRay()`.

For example, a reflecting object can use:

```text
Incident Ray
      ↓
Surface Normal
      ↓
Decompose Incident Vector
      ↓
Reverse Normal Component
      ↓
Reflection Vector
      ↓
New LightRay
```

A refracting object may instead perform:

```text
Incident Ray
      ↓
Surface Normal
      ↓
Determine Media
      ↓
Snell's Law
      ↓
Check TIR
      ↓
Refracted / Reflected Ray
      ↓
New LightRay
```

The optical mathematics should remain inside the object's interaction implementation or the relevant optical base system.

---

## 9. Movement and Rotation

`OpticalObject` already provides interaction behavior for movable optical objects.

By default:

```java
private boolean isDraggable = true;
private boolean isRotatable = true;
```

Therefore, a newly created optical object is draggable and rotatable unless those behaviors are disabled.

They can be controlled with:

```java
setDraggable(...)
setRotatable(...)
setModifiable(...)
```

For example:

```java
setModifiable(false);
```

disables both dragging and rotation.

---

## 10. Dragging

When an optical object is dragged, `OpticalObject` automatically:

1. tracks the mouse offset
2. moves the object toward the target position
3. marks the level environment as dirty

The movement uses interpolation:

```java
position.lerp(targetPos, 0.15f)
```

This provides smooth movement instead of instantly snapping the object to the mouse.

The subclass does not need to implement dragging itself.

---

## 11. Rotation

Rotation is also handled by `OpticalObject`.

While the object is hovered:

```text
Q → Rotate counter-clockwise
E → Rotate clockwise
```

The object is rotated through the inherited interaction system.

The subclass therefore does not need to implement keyboard handling for rotation.

---

## 12. Environment Dirty State

Moving or rotating an optical object modifies the optical environment.

`OpticalObject` therefore marks the containing `LevelScene` as dirty whenever its position or rotation changes.

```text
Optical Object
      ↓
Moved / Rotated
      ↓
Environment modified
      ↓
dirtyEnvironment = true
      ↓
LevelScene updates laser
      ↓
Ray path recalculated
```

This allows raycasting to be recalculated only when the optical environment has changed.

---

## 13. Collider Visibility

When the mouse hovers over an optical object, its collider can be displayed through the inherited hover behavior.

When the mouse leaves the object, the collider is hidden again.

This provides visual feedback during interaction without requiring every optical object to implement its own hover system.

---

## 14. What a New Optical Object Must Implement

The common interaction system is already provided by `OpticalObject`.

A new subclass primarily needs to provide:

```text
┌──────────────────────────────┐
│ New Optical Object           │
├──────────────────────────────┤
│ Constructor                  │
│      ↓                       │
│ OpticalObject configuration  │
│                              │
│ Collider                     │
│      ↓                       │
│ Physical geometry            │
│                              │
│ interactWithRay()            │
│      ↓                       │
│ Optical behavior             │
└──────────────────────────────┘
```

The subclass does **not** normally need to implement:

* mouse dragging
* keyboard rotation
* hover detection
* environment dirty-state handling
* collision detection
* collider visibility
* basic surface-normal calculation

These are already handled by the existing engine architecture.

---

## 15. Recommended Creation Workflow

When adding a new optical object:

```text
Create class
      ↓
Place it in the appropriate environment package
      ↓
Extend OpticalObject
      ↓
Create the required collider
      ↓
Call super(...)
      ↓
Select OpticalObjectType
      ↓
Implement interactWithRay()
      ↓
Implement the object's optical mathematics
      ↓
Add the object to a level
      ↓
Test ray interaction
      ↓
Test dragging / rotation
```

---

## Design Principle

Creating a new optical object should primarily involve defining **what the object does to a ray**, rather than rebuilding the systems required to make the object interact with the game.

The architecture separates these responsibilities:

```text
OpticalObject
      │
      ├── Interaction
      │   ├── Dragging
      │   ├── Rotation
      │   └── Hover
      │
      ├── Collider
      │   └── Geometry / Intersection
      │
      └── Optical Interaction
          └── interactWithRay()
```

This allows new mirrors, refractors, absorbers, and other optical objects to reuse the same interaction and collision infrastructure while implementing their own ray behavior.
