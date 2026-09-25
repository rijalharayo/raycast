# Levels

This document explains how playable levels are structured and managed in Ray Shooter.

---

## 1. Overview

A level represents a playable environment containing the objects, optical systems, targets, and layout required to solve a particular puzzle.

Levels are built on top of `LevelScene`.

```text
LevelScene
├── Level1
├── Level2
├── Level3
├── Level4
├── Level5
├── Level6
├── Level7
└── TestLevel
```

Each level defines its own arrangement of game objects while reusing the common scene and gameplay systems.

---

## 2. Level Structure

A level can contain:

* lasers
* mirrors
* lenses
* absorbers
* portals
* targets
* other environmental objects
* UI elements

The level itself is primarily responsible for constructing and arranging these objects.

The individual objects remain responsible for their own behavior.

---

## 3. Level Initialization

A level is initialized by creating the objects required for that level.

Conceptually:

```text
Level Creation
      ↓
Create Objects
      ↓
Configure Objects
      ↓
Add Objects To Scene
      ↓
Playable Level
```

This allows each level to define a different puzzle without changing the underlying engine systems.

---

## 4. Object-Based Level Design

Levels are constructed from reusable objects.

For example:

```text
Laser
  +
Mirror
  +
Lens
  +
Target
```

can form a complete puzzle.

Another level can use:

```text
Laser
  +
Portal
  +
Curved Mirror
  +
Target
```

without requiring a different raycasting implementation.

---

## 5. Level Manager

`LevelManager` handles level-related state and progression.

It provides a central place for the game to determine which level is currently active and how levels are progressed.

Conceptually:

```text
Level Selection
      ↓
LevelManager
      ↓
Selected Level
      ↓
LevelScene
```

---

## 6. Test Levels

`TestLevel` is used for testing engine systems independently from the normal progression of the game.

This is useful for testing:

* raycasting
* mirrors
* lenses
* portals
* collisions
* rendering
* new mechanics

A test level allows new systems to be verified without modifying an existing puzzle.

---

## 7. Gameplay Flow

A typical level follows:

```text
Load Level
    ↓
Create Environment
    ↓
Create Laser
    ↓
Create Optical Objects
    ↓
Create Target
    ↓
Player Interacts
    ↓
Rays Recalculate
    ↓
Target Receives Energy
    ↓
Level Completion
```

---

## 8. Level Completion

`TargetEnergyOrb` acts as a gameplay target for the optical system.

The player's objective is generally to manipulate the environment so that the laser reaches the target with the required optical conditions.

This connects the gameplay system directly to the raycasting system.

```text
Player
  ↓
Manipulates Objects
  ↓
Optical Environment Changes
  ↓
Ray Path Changes
  ↓
Target
  ↓
Level Completion
```

---

## 9. Level Progression

When a level is completed, the game can transition to another level through the level management system.

The level itself does not need to control the entire game loop.

Instead:

```text
Level
 ↓
Completion State
 ↓
LevelManager
 ↓
Next Level
```

This keeps level progression separate from the physical and optical systems.

---

## 10. Design Principle

Levels describe **what exists in a puzzle**, while the engine determines **how those objects behave**.

Therefore:

$$
\text{Level Data}
\rightarrow
\text{Reusable Systems}
\rightarrow
\text{Gameplay}
$$

This allows new puzzles to be created without rewriting the underlying engine.

---

# Optical Objects

This document explains the gameplay role and architecture of objects that interact with light.

---

## 1. Overview

Optical objects are environmental objects that modify the path or behavior of light rays.

They are represented through the `OpticalObject` hierarchy and interact with the raycasting system through `RayInteractable`.

```text
OpticalObject
├── Reflectors
├── Refractors
├── Absorbers
└── Other Optical Objects
```

---

## 2. Optical Object Pipeline

An optical object participates in ray interaction through:

```text
LightRay
    ↓
Intersection
    ↓
Optical Object
    ↓
Calculate Interaction
    ↓
New Ray / Termination
```

The object's geometry determines where the ray hits it, while its optical behavior determines what happens afterward.

---

## 3. Reflectors

Reflectors redirect incoming rays.

Examples include:

```text
Mirror
PlaneMirror
CurvedMirror
CircularMirror
```

A reflector calculates the reflected direction using the surface normal.

The result is a new ray travelling away from the surface.

---

## 4. Refractors

Refractors change the direction of a ray when it enters or exits a different optical medium.

Examples include:

```text
GlassBlock
GlassDisc
OpticalFibre
Prism
Lens
WaterTank
```

The refractor determines the new medium and applies the appropriate optical calculation.

---

## 5. Lenses

Lenses combine curved optical surfaces with refraction.

Examples include:

```text
ConvexLens
ConcaveLens
```

Their geometry is constructed from mathematical arcs rather than approximating the entire lens using a simple rectangle or polygon.

This allows rays to interact with the actual curved surfaces.

---

## 6. Absorbers

Absorbers terminate rays instead of redirecting them.

For example:

```text
BlackAbsorber
```

can prevent a ray from continuing through the environment.

Conceptually:

```text
Ray
 ↓
Absorber
 ↓
Terminate
```

---

## 7. Portals

Portals transform a ray from one location to another.

A portal interaction involves:

* finding the intersection point
* transforming the point into the source portal's local coordinates
* transforming it into the destination portal's coordinates
* transforming the ray direction
* placing the new ray at the destination
* offsetting it slightly to prevent immediate re-collision

This allows portals to act as optical teleportation surfaces.

---

## 8. Optical Object Hierarchy

The general structure is:

```text
GameObject
    ↓
OpticalObject
    ↓
RayInteractable
    ↓
Specific Optical Behavior
```

The exact inheritance relationships vary between object types, but the important separation is that optical behavior is exposed to the raycasting system without requiring the raycaster to understand each object's implementation.

---

## 9. Environment Changes

Optical objects can modify the ray path when they are:

* moved
* rotated
* added
* removed

When their geometry changes, the environment is marked dirty.

```text
Object Changed
      ↓
Environment Dirty
      ↓
Laser Recasts
      ↓
New Optical Path
```

This connects player interaction directly to the raycasting system.

---

## 10. Gameplay Role

Optical objects are not simply physics demonstrations.

They are the main puzzle elements of Ray Shooter.

The player manipulates the environment to control the laser.

```text
Player
  ↓
Optical Objects
  ↓
Ray Path
  ↓
Target
```

The gameplay therefore emerges from the interaction between geometry, optics, and player manipulation.

---

## 11. Design Principle

Each optical object is responsible for its own optical behavior.

The raycasting system is responsible for:

* finding intersections
* determining the next object
* continuing the ray

The optical object is responsible for:

* determining how the ray interacts with it
* producing the appropriate result

This keeps the raycasting system independent from individual optical implementations.
