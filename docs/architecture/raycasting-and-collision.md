# Raycasting

This document explains the raycasting architecture used by Ray Shooter to trace light through the game world.

---

## 1. Overview

Raycasting is the process used to determine where a light ray travels and which object it encounters first.

The system connects the laser, rays, geometric intersection calculations, and optical interactions.

The basic pipeline is:

```text
Laser
  ↓
LightRay
  ↓
Find Intersections
  ↓
Nearest Hit
  ↓
Optical Interaction
  ↓
New Ray
  ↓
Repeat
```

---

## 2. Ray Representation

A ray is represented using its origin and direction.

The ray data is stored using `RayData`.

Conceptually:

$$
R(t)=P+tD
$$

where:

* \(P\) is the ray origin
* \(D\) is the direction
* \(t\) represents distance along the ray

The direction is normalized when appropriate so that the parameter \(t\) corresponds to a consistent distance scale.

---

## 3. Light Rays

`LightRay` represents an actual ray travelling through the scene.

A ray contains information such as:

* origin
* direction
* current medium
* interaction state
* ray data

The current medium is important for refractive interactions because Snell's Law depends on both the medium being exited and the medium being entered.

---

## 4. Ray Interactable Objects

Only objects that implement the ray-interaction interface need to be tested by the raycasting system.

The scene maintains:

```java
List<RayInteractable>
```

rather than forcing the raycaster to inspect every game object.

This creates a direct relationship:

```text
Scene
  ↓
RayInteractable[]
  ↓
Raycaster
```

---

## 5. Finding the First Intersection

A ray may intersect several objects.

For example:

```text
Laser
  |
  | ---------> Object A -----> Object B
  |
```

The raycaster must select the closest valid intersection.

Conceptually:

```text
All intersections
       ↓
Remove invalid hits
       ↓
Compare distances
       ↓
Closest hit
```

The closest hit determines what the ray interacts with first.

---

## 6. Geometric Intersection

Raycasting relies heavily on the geometric systems provided by the `Line` and collider classes.

Depending on the object, the ray may be tested against:

* line segments
* polygon edges
* circles
* arcs
* other geometric boundaries

The geometric subsystem determines whether an intersection exists and where it occurs.

---

## 7. Ray Hit

When a ray intersects an object, the result is represented by `RayHit`.

A hit provides the information required to continue processing the ray.

Conceptually:

```text
Ray
 ↓
Intersection
 ↓
RayHit
 ├── intersection point
 ├── object
 └── surface information
```

The exact interaction depends on the object that was hit.

---

## 8. Optical Interaction

After finding a hit, the ray is passed to the object's optical interaction logic.

Different objects can produce different results.

For example:

```text
Mirror
   ↓
Reflected Ray

Refractor
   ↓
Refracted Ray / TIR Ray

Absorber
   ↓
Ray Terminates

Portal
   ↓
Ray Transformed
```

The raycasting system therefore does not need to know the physical formula for every object.

It only needs to process the result of the interaction.

---

## 9. Reflection

A mirror produces a new ray whose direction is calculated from the surface normal.

The reflection system decomposes the incident vector into normal and tangent components:

$$
I=I_N+I_T
$$

and reverses the normal component:

$$
R=I_T-I_N
$$

The resulting vector becomes the direction of the new ray.

---

## 10. Refraction

A refracting object uses the current medium and target medium.

The refracted angle is calculated using Snell's Law:

$$
n_1\sin\theta_i=n_2\sin\theta_r
$$

If total internal reflection occurs, the ray is reflected instead.

The ray's current medium is then updated when appropriate.

---

## 11. Ray Chaining

A ray interaction can produce another ray.

This creates a chain:

```text
Initial Ray
    ↓
Intersection
    ↓
Interaction
    ↓
New Ray
    ↓
Intersection
    ↓
Interaction
    ↓
New Ray
    ↓
...
```

This is what allows a single laser to produce complex optical paths.

For example:

```text
Laser
  ↓
Mirror
  ↓
Glass
  ↓
Mirror
  ↓
Target
```

---

## 12. Virtual Rays

`VirtualRay` can represent ray information that is useful for calculations without necessarily representing a directly rendered physical ray.

This allows the optical system to separate mathematical ray calculations from the visual representation of rays.

---

## 13. Ray Termination

A ray should eventually stop when it reaches a terminating condition.

Examples include:

* hitting an absorber
* reaching a target
* exceeding a permitted interaction depth
* having no further intersection
* reaching a system-defined termination condition

This prevents infinite interaction chains.

---

## 14. Raycasting Architecture

The overall architecture is:

```text
Laser
  │
  ▼
LightRay
  │
  ▼
Scene.getSceneRayInteractables()
  │
  ▼
Intersection Testing
  │
  ▼
Nearest RayHit
  │
  ▼
RayInteractable
  │
  ├── Reflect
  ├── Refract
  ├── Absorb
  └── Transform
  │
  ▼
New LightRay
  │
  └───────────────► Continue
```

---

## 15. Environment Changes

Raycasting is recalculated when the optical environment changes.

For example:

```text
Object moved
     ↓
Environment dirty
     ↓
Laser recasts
```

Without this system, the laser would need to recalculate its complete path every frame even when the scene has not changed.

The dirty-state mechanism therefore reduces unnecessary raycasting work.

---

## 16. Cached Ray Interactables

The scene caches the array of ray-interactable objects.

This prevents repeated conversion of the `List` into an array during raycasting.

The architecture is:

```text
List<RayInteractable>
        ↓
      Cache
        ↓
Raycasting
```

The cache is invalidated whenever the ray-interactable collection changes.

---

## 17. Summary

Raycasting connects the game's geometry and optics systems.

The overall process is:

$$
\text{Ray}
\rightarrow
\text{Intersection}
\rightarrow
\text{Hit}
\rightarrow
\text{Interaction}
\rightarrow
\text{New Ray}
$$

The raycaster therefore acts as the bridge between:

* geometric intersection
* collision data
* optical physics
* scene objects
* visual ray rendering

This allows the same ray system to support reflection, refraction, portals, absorption, and target detection.

---

# Collision System

This document explains the collision and geometric intersection architecture used by Ray Shooter.

---

## 1. Overview

The collision system provides the geometric layer used to determine whether objects and rays intersect.

The main components are:

```text
Collider
├── PolygonCollider
├── BoxCollider
├── CircleCollider
└── ArcCollider
```

The system also uses:

```text
CollisionData
CollisionType
```

to describe collision results.

---

## 2. Collider Responsibility

A collider represents the geometric boundary used for intersection testing.

The collider is separate from the higher-level game object.

Conceptually:

```text
GameObject
    ↓
Collider
    ↓
Shape
```

This separation allows an object to use geometric collision functionality without mixing collision calculations into its gameplay logic.

---

## 3. Base Collider

`Collider` provides common collider functionality.

It stores information such as:

* shape
* position
* rotation

Specific collider types then implement geometry appropriate for their shape.

---

## 4. Polygon Collider

`PolygonCollider` handles polygon-based geometry.

A polygon consists of a sequence of vertices:

$$
P_1,P_2,\ldots,P_n
$$

Adjacent vertices form line segments:

$$
P_1P_2,\;
P_2P_3,\;
\ldots,\;
P_nP_1
$$

These edges can then be tested for intersection.

---

## 5. Box Collider

`BoxCollider` provides collision geometry for rectangular objects.

A box can be represented using its four edges.

Rotation changes the positions of these edges while preserving the rectangle's dimensions.

---

## 6. Circle Collider

A circle is represented by:

* center \(C\)
* radius \(r\)

A point \(P\) lies on the circle when:

$$
|P-C|=r
$$

and inside the circle when:

$$
|P-C|\leq r
$$

Circle geometry is useful for circular optical objects and other round game elements.

---

## 7. Arc Collider

`ArcCollider` represents a section of a circle rather than a complete circle.

An arc requires additional information such as:

* center
* radius
* angular range
* thickness where applicable

This is particularly useful for curved optical surfaces such as lenses and curved mirrors.

---

## 8. Line Intersection

Line intersection is provided by the `Line` mathematical system.

Two infinite lines are represented as:

$$
A_1x+B_1y=C_1
$$

and:

$$
A_2x+B_2y=C_2
$$

Their intersection can be calculated using Cramer's Rule.

However, collision detection must distinguish between infinite lines and finite segments.

Therefore, the intersection point must also be checked against the segment boundaries.

---

## 9. Collision Data

When a collision occurs, the result can contain information about the intersection.

Conceptually:

```text
Collision
├── Intersection Point
├── Collision Type
└── Additional Geometry
```

This allows higher-level systems to respond without having to repeat the geometric calculation.

---

## 10. Collision Types

`CollisionType` identifies the type of collision or geometric interaction that occurred.

This allows different systems to distinguish between different collision situations without depending directly on the implementation of the collider.

---

## 11. Optical Objects

Optical objects use collision geometry to determine where a ray reaches their surface.

For example:

```text
LightRay
    ↓
Collider
    ↓
Intersection
    ↓
Surface Data
    ↓
Optical Interaction
```

The collider therefore forms part of the optical pipeline rather than being limited to traditional physical collision.

---

## 12. Surface Geometry

After an intersection is found, the optical system may need a surface normal and tangent.

For a line segment with normalized direction:

$$
\hat{v}=
\begin{bmatrix}
x\\
y
\end{bmatrix}
$$

a normal can be obtained through a \(90^\circ\) counter-clockwise rotation:

$$
N=
\begin{bmatrix}
-y\\
x
\end{bmatrix}
$$

Curved surfaces instead obtain their normals from their geometric curvature.

---

## 13. Geometry and Physics Separation

The collision system determines **where** an interaction occurs.

The optical system determines **what happens afterward**.

For example:

```text
Collision System
      ↓
Ray hits glass at P
      ↓
Optics System
      ↓
Calculate refraction
      ↓
New Ray
```

This separation keeps geometric calculations independent from physical behavior.

---

## 14. Collision Pipeline

The general collision pipeline is:

```text
Object Geometry
      ↓
Collider
      ↓
Intersection Test
      ↓
Collision Data
      ↓
Surface Information
      ↓
Higher-Level Response
```

Different systems can therefore reuse the same geometric collision infrastructure.

---

## 15. Summary

The collision system provides the geometric foundation used by:

* raycasting
* optical surfaces
* mirrors
* lenses
* portals
* object interaction

Its main responsibility is determining geometric relationships.

The physics and gameplay systems then decide what those relationships mean.

This separation can be summarized as:

$$
\text{Geometry}
\rightarrow
\text{Collision}
\rightarrow
\text{Interaction}
$$
