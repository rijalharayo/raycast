# Adding a New Lens

This guide explains how to create a new lens type in Ray Shooter.

Lenses are specialized `Refractor` objects. The base `Lens` class provides the common physical and optical properties of a lens, while each lens subclass is responsible for generating its own geometry.

---

## Lens Class Structure

The inheritance structure is:

```text
GameObject
    ↓
OpticalObject
    ↓
Refractor
    ↓
Lens
    ├── ConvexLens
    └── ConcaveLens
```

A new lens should therefore extend `Lens`.

```java
public class NewLens extends Lens {

}
```

---

## 1. Define the Lens Geometry

The most important difference between lens types is their geometry.

`Lens` does not directly define a particular lens shape. Instead, subclasses provide their own static `createShape()` implementation.

```java
protected static Polygon createShape(
     float radiusOfCurvature,
     float centerThickness,
     float apertureDiameter
)
```

The method must return the `Polygon` representing the lens.

Conceptually:

```text
Lens parameters
      ↓
createShape()
      ↓
Polygon
      ↓
PolygonCollider
      ↓
Lens
```

This allows different lens types to use completely different geometric constructions while sharing the same base class.

---

## 2. Calculate the Arc Angle

Lens surfaces are represented using curved arcs.

Each lens type therefore also defines its own `calculateArcAngle()` method:

```java
protected static float calculateArcAngle(
     float radiusOfCurvature,
     float centerThickness,
     float apertureDiameter
)
```

The method determines the minor angle of the arcs forming the lens surfaces.

The calculation depends on the geometry of the particular lens type.

For example, a convex and concave lens do not use the same geometric relationship, so each subclass calculates its own angle.

---

## 3. Use the Lens Constructor

`Lens` provides constructors that handle the common setup.

The main constructor accepts:

```text
position
lens shape
radius of curvature
center thickness
aperture diameter
arc angle
rotation
```

The base class then creates the required `PolygonCollider` and initializes the lens as a dense flint glass refractor.

```text
NewLens
   ↓
Lens constructor
   ├── PolygonCollider
   ├── Dense flint glass medium
   ├── Radius of curvature
   ├── Center thickness
   ├── Aperture diameter
   └── Arc angle
```

A subclass therefore does not need to recreate this setup.

---

## 4. Typical Lens Constructor

A lens subclass can calculate its geometry and arc angle before passing them to `super(...)`.

For example:

```java
public NewLens(
     Vector2 position,
     float radiusOfCurvature,
     float centerThickness,
     float apertureDiameter,
     float rotation
) {
     super(
          position,
          createShape(
               radiusOfCurvature,
               centerThickness,
               apertureDiameter
          ),
          radiusOfCurvature,
          centerThickness,
          apertureDiameter,
          calculateArcAngle(
               radiusOfCurvature,
               centerThickness,
               apertureDiameter
          ),
          rotation
     );
}
```

The exact constructor can vary depending on the geometry and parameters required by the new lens.

---

## 5. Lens Geometry Parameters

The base `Lens` class stores four important geometric properties.

### Radius of Curvature

```java
radiusOfCurvature
```

This determines the radius of the circles from which the curved lens surfaces are constructed.

### Center Thickness

```java
centerThickness
```

This determines the thickness of the lens through its center.

### Aperture Diameter

```java
apertureDiameter
```

This determines the usable diameter of the lens opening.

### Arc Angle

```java
arcAngle
```

This determines how much of each circular surface is used to form the lens.

Together:

```text
Radius of Curvature
        +
Center Thickness
        +
Aperture Diameter
        ↓
Lens Geometry
        ↓
Arc Angle
```

---

## 6. Centers of Curvature

`Lens` also provides methods for obtaining the centers of curvature of its two faces.

```java
getLeftFaceCenterOfCurvature()
getRightFaceCenterOfCurvature()
```

The centers are positioned relative to the lens and rotated according to the collider's current rotation.

For the left face:

```text
Lens center ─────────────→ Center of curvature
             r
```

For the right face:

```text
Center of curvature ←───────────── Lens center
                       r
```

These methods allow the curved surfaces to be treated geometrically rather than relying on hard-coded world coordinates.

---

## 7. Rotation

The centers of curvature are calculated using the lens's current collider rotation.

Conceptually:

```text
Local curvature center
        ↓
Rotate by lens rotation
        ↓
Translate by lens position
        ↓
World-space curvature center
```

Therefore, the geometry remains correct when the lens is rotated.

The same principle should be followed when implementing the geometry of a new lens type.

---

## 8. Building the Polygon

The shape returned by `createShape()` must be a `Polygon`.

The polygon approximates the curved lens surfaces using line segments.

Conceptually:

```text
Circular Arc
     ↓
Arc segments
     ↓
Polygon vertices
     ↓
Polygon
     ↓
PolygonCollider
```

The number of segments determines how closely the polygon approximates the actual circular surfaces.

The lens can then use those edges for collision and ray-intersection calculations.

---

## 9. Geometry and Optical Behavior

The geometry of the lens and its optical behavior are separate responsibilities.

```text
Lens
├── Geometry
│   ├── createShape()
│   └── calculateArcAngle()
│
└── Optical behavior
    └── Refractor
```

`createShape()` determines **where the surfaces are**.

`Refractor` determines **how light behaves when it reaches those surfaces**.

This means a new lens does not need to implement Snell's Law, refraction, or total internal reflection again.

---

## 10. Creating a New Lens Type

When creating a new lens type, the main workflow is:

```text
Create class
      ↓
extends Lens
      ↓
Determine lens geometry
      ↓
Implement createShape()
      ↓
Derive calculateArcAngle()
      ↓
Create constructor
      ↓
Call super(...)
      ↓
Test ray refraction
```

The geometry should be derived from the desired physical lens shape rather than manually approximating coordinates.

---

## 11. Example Structure

A new lens can therefore have a structure similar to:

```java
public class NewLens extends Lens {

     public NewLens(
          Vector2 position,
          float radiusOfCurvature,
          float centerThickness,
          float apertureDiameter,
          float rotation
     ) {
          super(
               position,
               createShape(
                    radiusOfCurvature,
                    centerThickness,
                    apertureDiameter
               ),
               radiusOfCurvature,
               centerThickness,
               apertureDiameter,
               calculateArcAngle(
                    radiusOfCurvature,
                    centerThickness,
                    apertureDiameter
               ),
               rotation
          );
     }

     private static Polygon createShape(
          float radiusOfCurvature,
          float centerThickness,
          float apertureDiameter
     ) {
          // Construct lens geometry
     }

     private static float calculateArcAngle(
          float radiusOfCurvature,
          float centerThickness,
          float apertureDiameter
     ) {
          // Calculate lens arc angle
     }
}
```

The actual geometry and mathematical relationships depend on the lens being created.

---

## 12. What `Lens` Already Provides

A new lens does **not** need to recreate the functionality already provided by `Lens`.

The base class already provides:

* radius of curvature
* center thickness
* aperture diameter
* arc angle
* dense flint glass medium
* polygon collider creation
* left curvature center
* right curvature center
* common refractor behavior inherited from `Refractor`

The subclass primarily defines:

* lens geometry
* arc-angle calculation
* constructor parameters specific to the lens

---

## Design Principle

The lens hierarchy separates **general lens properties** from **specific lens geometry**.

```text
                 Lens
                  │
        ┌─────────┴─────────┐
        │                   │
 Common lens data       Lens geometry
        │                   │
        │             createShape()
        │             calculateArcAngle()
        │                   │
        └─────────┬─────────┘
                  ↓
            Specific Lens
```

This makes it possible to add new lens geometries without modifying the existing refraction system.

The general rule is:

> **`Lens` defines what all lenses have; the subclass defines what makes that particular lens shape different.**
