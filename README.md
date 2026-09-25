# 🔦 Ray Cast

> A custom 2D ray-shooting game and physics engine built from scratch in Java.

Ray Cast is a 2D physics-based puzzle/shooter project focused on **light, geometry, optics, and custom engine development**.

Instead of relying heavily on pre-built physics or geometry libraries, the project implements its own mathematical and rendering systems for vectors, matrices, lines, shapes, collision detection, raycasting, reflection, refraction, portals, lenses, and interactive objects.

---

# ✨ Features

* 🔦 Custom laser and raycasting system
* 💡 Light rays with configurable direction and origin
* 🪞 Reflection using surface normals
* 🔬 Refraction using **Snell's Law**
* 🔺 Critical-angle and total internal reflection handling
* 🔍 Convex and concave optical lenses
* 🌀 Curved optical surfaces
* 🌀 Portals and linked portal systems
* 📐 Custom 2D geometry system
* 📦 Custom shapes and colliders
* 🎯 Ray-object intersection detection
* 🖱️ Interactive object dragging
* 🔄 Interactive object rotation
* 🎮 Custom keyboard and mouse input
* 🖼️ Sprite rendering
* ✨ Layered portal glow effects
* 🎨 Custom UI components
* 📐 Resolution-independent world rendering
* 🖥️ Virtual world resolution of **1603 × 902**
* ⚡ Fixed game/update loop
* 🧩 Scene-based game architecture
* 🗺️ Multiple puzzle levels
* 🔧 Custom-built engine architecture

---

# 🧠 What Makes This Project Different?

The main goal of Ray Cast is not simply to make a game.

It is also an experiment in **building a small 2D game/physics engine from the ground up**.

The project implements many systems manually rather than hiding them behind existing abstractions.

For example:

* Vectors are implemented manually.
* Matrix transformations are implemented manually.
* Lines and intersections are implemented manually.
* Polygon geometry is implemented manually.
* Raycasting is implemented manually.
* Reflection is calculated from surface normals.
* Refraction is calculated using Snell's Law.
* Curved surfaces are approximated mathematically.
* Collision/intersection logic is handled by custom shapes.
* Rendering coordinates are transformed from world space to screen space.

The project therefore combines **game development, computer graphics, geometry, and physics**.

---

# 📐 Mathematics Used

Ray Cast makes extensive use of mathematics for both gameplay and engine functionality.

## 1. Trigonometry

Trigonometry is used throughout the project for:

* Angles
* Rotations
* Ray directions
* Surface normals
* Circular geometry
* Arc construction
* Lens construction
* Direction vectors
* Reflection
* Refraction
* Coordinate transformations

Important functions include:

```text
sin(θ)
cos(θ)
tan(θ)
asin(x)
acos(x)
atan2(y, x)
```

Angles are represented internally in **radians** where mathematical calculations require them.

Degrees are primarily used where values are easier to visualize or interact with.

---

## 2. Vectors

The project contains a custom `Vector2` implementation for 2D mathematics.

Vectors are used for:

* Positions
* Directions
* Ray origins
* Ray directions
* Surface normals
* Object movement
* Geometry calculations
* Distance calculations
* Dot products

Basic vector operations include:

```text
addition
subtraction
scalar multiplication
normalization
magnitude
dot product
angle calculation
```

The dot product is particularly important for optical calculations.

For two vectors:

```text
a · b = ax bx + ay by
```

The dot product is used to determine relationships between directions and normals.

---

## 3. Matrices

Custom 2×2 matrices are used for transformations.

The matrix system is primarily used for:

* Rotation
* Transforming vectors
* Rotating geometry
* Rotating portal coordinate systems
* Surface transformations

A 2D rotation matrix has the form:

```text
[ cos(θ)  -sin(θ) ]
[ sin(θ)   cos(θ) ]
```

Matrices allow geometry to be transformed without manually calculating every rotated coordinate separately.

---

## 4. Coordinate Geometry

Coordinate geometry forms the foundation of the engine.

It is used for:

* Point positions
* Lines
* Intersections
* Distances
* Circles
* Arcs
* Polygons
* Object positioning
* Raycasting
* Collision detection

The engine maintains a **world coordinate system** separate from the actual screen pixel coordinates.

This allows the game world to remain mathematically consistent regardless of the monitor resolution.

---

## 5. Lines and Intersections

The custom `Line` implementation is used heavily by the raycasting system.

Line mathematics is used to determine:

* Whether a ray intersects an object
* Where an intersection occurs
* Which surface was hit
* Surface direction
* Surface normal
* Distance to an intersection

Line intersections are calculated mathematically rather than through the rendering API.

The implementation uses systems such as **Cramer's rule** for solving line-intersection equations.

---

# 🔬 Physics & Optics

The optical system is one of the main parts of Ray Shooter.

## Reflection

Reflection is calculated using the incoming ray direction and the surface normal.

The core vector relationship is:

```text
R = I - 2(I · N)N
```

Where:

* `I` = incident direction
* `N` = normalized surface normal
* `R` = reflected direction

This allows rays to bounce naturally from:

* Flat surfaces
* Polygon surfaces
* Curved surfaces
* Optical objects

---

## Refraction

Refraction is calculated using **Snell's Law**.

```text
n₁ sin(θ₁) = n₂ sin(θ₂)
```

Where:

* `n₁` = refractive index of the first medium
* `n₂` = refractive index of the second medium
* `θ₁` = angle of incidence
* `θ₂` = angle of refraction

The ray direction and surface normal are used to determine the correct refracted direction.

---

## Critical Angle

The engine also handles the critical-angle condition.

When light travels from a medium with a higher refractive index into a lower refractive index, refraction is only possible up to a certain angle.

The critical angle satisfies:

```text
sin(θc) = n₂ / n₁
```

when:

```text
n₁ > n₂
```

If the incident angle exceeds the critical angle, the ray undergoes:

**Total Internal Reflection (TIR).**

This allows the game to simulate optical behavior rather than simply redirecting rays arbitrarily.

---

# 🔍 Lenses

Ray Shooter contains custom lens geometry.

## Convex Lenses

Convex lenses are constructed from curved surfaces based on circles.

The lens geometry uses:

* Radius of curvature
* Center thickness
* Aperture diameter
* Circular arcs
* Coordinate geometry
* Trigonometry

For a symmetric biconvex lens, the thin-lens approximation can be related to the lensmaker equation:

```text
1/f = 2(n - 1) / R
```

where `R` represents the surface radius of curvature.

---

## Concave Lenses

Concave lenses use opposing curved surfaces.

Their geometry is constructed from circular regions and calculated arc angles.

The construction involves:

* Radius of curvature
* Aperture radius
* Center thickness
* `acos`
* Circle geometry
* Arc approximation
* Polygon construction

The curved surfaces are represented internally using custom `Arc` objects.

---

# 🌀 Curved Geometry

Curved surfaces are approximated using multiple vertices.

For example, an arc can be represented as a sequence of points along a circle:

```text
x = r cos(θ)
y = r sin(θ)
```

The resulting points form an approximated polygon.

This allows the same geometric infrastructure used by polygonal objects to also participate in:

* Intersection tests
* Raycasting
* Collision calculations
* Surface detection

Rendering can still use smooth Java2D arcs where appropriate.

---

# 🎯 Raycasting

The raycasting system searches through the scene's ray-interactable objects and determines where a ray intersects them.

A typical ray-processing flow is:

```text
Laser
  │
  ▼
Ray
  │
  ▼
Scene Ray Interactables
  │
  ▼
Intersection Tests
  │
  ▼
Closest Intersection
  │
  ▼
Surface Data
  │
  ▼
Reflection / Refraction
  │
  ▼
New Ray
```

The closest valid intersection becomes the next point at which the ray interacts with the environment.

---

# 🧱 Custom Geometry System

The project contains custom geometric shapes rather than relying exclusively on Java's built-in geometry classes.

Current concepts include:

* `Shape`
* `Polygon`
* `Rectangle`
* `Circle`
* `Arc`
* `Line`
* `Vector2`
* `Matrix2x2`

These classes provide the mathematical foundation for the game's physics and interaction systems.

---

# 🎮 Interaction

Objects in the environment can be interacted with using the mouse and keyboard.

## Dragging

Objects can be moved by holding the mouse button while interacting with them.

The engine converts the mouse's screen position back into world coordinates before performing interaction calculations.

This prevents resolution scaling from breaking interaction accuracy.

---

## Rotation

Interactive objects can also be rotated.

Rotation uses:

```text
degrees → radians → rotation matrix
```

depending on which system is being accessed.

The game-facing rotation API uses degrees for easier visualization, while mathematical matrix operations use radians.

---

# 🖥️ Resolution Independence

Ray Shooter uses a virtual world resolution of:

```text
1603 × 902
```

This acts as the game's design/world coordinate system.

The actual monitor resolution does not directly determine the game's physics geometry.

Instead:

```text
World Coordinates
       │
       ▼
Virtual Resolution
       │
       ▼
Uniform Scale
       │
       ▼
Screen Coordinates
```

The engine calculates a uniform scale using:

```text
scaleX = screenWidth / worldWidth
scaleY = screenHeight / worldHeight

scale = min(scaleX, scaleY)
```

This keeps the aspect ratio of the game world consistent.

---

## Letterboxing

If the screen's aspect ratio differs from the virtual world, unused space can appear on one axis.

This is intentional.

The world is scaled uniformly instead of being stretched independently along X and Y.

This prevents:

* Circles becoming ellipses
* Objects changing proportions
* Lens geometry becoming distorted
* Ray angles changing visually

---

# 🎨 Rendering Architecture

The game separates **world-space calculations** from **screen-space rendering**.

Physics remains in world coordinates.

Rendering converts those coordinates into actual pixels.

For example:

```text
World:
(100, 200)

        ↓

Screen transformation

        ↓

Actual pixel position
```

This means the same collider can remain:

```text
50 world units
```

while appearing as different numbers of pixels depending on the screen scale.

The collider itself is never resized for resolution.

Only its rendered representation changes.

---

# 🖼️ Background Rendering

The game background is rendered separately from the world.

World objects use the virtual-resolution coordinate system, while the background can be drawn directly across the actual screen.

This prevents letterboxing from creating unwanted gaps around the background.

Conceptually:

```text
┌───────────────────────────────────────┐
│              BACKGROUND               │
│                                       │
│     ┌───────────────────────────┐     │
│     │                           │     │
│     │       GAME WORLD          │     │
│     │       1603 × 902          │     │
│     │                           │     │
│     └───────────────────────────┘     │
│                                       │
└───────────────────────────────────────┘
```

---

# ✨ Portal Effects

Portals use layered rendering to create a glow effect.

Instead of relying on expensive blur operations, the effect is created using multiple progressively larger and more transparent shapes.

Conceptually:

```text
Large transparent glow
        ↓
Medium transparent glow
        ↓
Small bright glow
        ↓
Actual portal frame
```

This produces a lightweight fake glow while remaining compatible with the custom rendering system.

---

# 🧩 UI System

Ray Shooter contains a custom UI system separate from the game-world objects.

UI concepts include:

* `UIComponent`
* `UILayout`
* `UIWrapLayout`
* `UIButton`
* `UIText`

The UI layout system uses virtual/world-style dimensions so that UI elements can also participate in resolution-independent rendering.

---

# 📦 Sprites

Sprites are handled through a custom `Sprite` class.

Sprites support:

* Image loading
* Scaling
* Rotation
* Width/height management
* World-space positioning
* Resolution-independent rendering
* Background rendering

Sprite scaling is separated from the global world scale.

For example:

```text
Sprite scale
      ×
World screen scale
      =
Final rendered size
```

The sprite's logical dimensions remain independent of the monitor's resolution.

---

# 🏗️ Project Structure

```text
src/
│   App.java
│
├── main/
│   │
│   ├── audio/
│   │   └── SoundEffect.java
│   │
│   ├── game/
│   │   ├── Game.java
│   │   ├── LevelManager.java
│   │   ├── Scene.java
│   │   ├── SceneManager.java
│   │   ├── ShapeRender.java
│   │   ├── Window.java
│   │   │
│   │   └── scenes/
│   │       ├── LevelScene.java
│   │       ├── MenuScene.java
│   │       │
│   │       ├── levels/
│   │       │   ├── Level1.java
│   │       │   ├── Level2.java
│   │       │   ├── Level3.java
│   │       │   ├── Level4.java
│   │       │   ├── Level5.java
│   │       │   ├── Level6.java
│   │       │   ├── Level7.java
│   │       │   └── TestLevel.java
│   │       │
│   │       └── menus/
│   │           ├── LevelSelectionMenu.java
│   │           └── MainMenu.java
│   │
│   ├── input/
│   │   ├── KeyboardInput.java
│   │   └── MouseInput.java
│   │
│   ├── math/
│   │   ├── Line.java
│   │   │
│   │   ├── algebra/
│   │   │   ├── Matrix2x2.java
│   │   │   └── Vector2.java
│   │   │
│   │   └── shapes/
│   │       ├── Arc.java
│   │       ├── Circle.java
│   │       ├── Shape.java
│   │       │
│   │       └── polygons/
│   │           ├── Polygon.java
│   │           ├── Rectangle.java
│   │           ├── RightTriangle.java
│   │           └── Triangle.java
│   │
│   ├── models/
│   │   ├── GameObject.java
│   │   │
│   │   ├── data/
│   │   │   ├── IntersectionData.java
│   │   │   ├── RayData.java
│   │   │   └── SurfaceData.java
│   │   │
│   │   ├── entities/
│   │   │   ├── Laser.java
│   │   │   └── LightRay.java
│   │   │
│   │   └── environment/
│   │       ├── OpticalObject.java
│   │       ├── TargetEnergyOrb.java
│   │       │
│   │       ├── absorbers/
│   │       │   ├── Absorber.java
│   │       │   └── BlackAbsorber.java
│   │       │
│   │       ├── reflectors/
│   │       │   ├── Reflector.java
│   │       │   │
│   │       │   └── mirrors/
│   │       │       ├── CircularMirror.java
│   │       │       ├── CurvedMirror.java
│   │       │       ├── Mirror.java
│   │       │       └── PlaneMirror.java
│   │       │
│   │       ├── refractors/
│   │       │   ├── Refractor.java
│   │       │   │
│   │       │   ├── fluids/
│   │       │   │   └── WaterTank.java
│   │       │   │
│   │       │   └── glasses/
│   │       │       ├── GlassBlock.java
│   │       │       ├── GlassDisc.java
│   │       │       ├── OpticalFibre.java
│   │       │       ├── Prism.java
│   │       │       │
│   │       │       └── lens/
│   │       │           ├── ConcaveLens.java
│   │       │           ├── ConvexLens.java
│   │       │           └── Lens.java
│   │       │
│   │       └── supernatural/
│   │           └── Portal.java
│   │
│   ├── physics/
│   │   ├── colliders/
│   │   │   ├── ArcCollider.java
│   │   │   ├── BoxCollider.java
│   │   │   ├── CircleCollider.java
│   │   │   ├── Collider.java
│   │   │   ├── CollisionData.java
│   │   │   ├── CollisionType.java
│   │   │   └── PolygonCollider.java
│   │   │
│   │   ├── optics/
│   │   │   ├── Material.java
│   │   │   ├── Medium.java
│   │   │   ├── OpticalObjectType.java
│   │   │   └── RayInteractable.java
│   │   │
│   │   └── rays/
│   │       ├── Ray.java
│   │       ├── RayHit.java
│   │       └── VirtualRay.java
│   │
│   ├── sprites/
│   │   └── Sprite.java
│   │
│   └── ui/
│       ├── Fonts.java
│       ├── UIComponent.java
│       ├── UILayout.java
│       │
│       ├── components/
│       │   ├── UIButton.java
│       │   └── UIText.java
│       │
│       └── layouts/
│           └── UIWrapLayout.java
│
└── resources/
    ├── fonts/
    │   └── Slackey-Regular.ttf
    │
    ├── images/
    │   ├── background1.png
    │   ├── circular-mirror.png
    │   ├── laser-pointer.png
    │   └── plane-mirror.png
    │
    └── sounds/
        ├── button-click.wav
        ├── button-hover.wav
        ├── laser-enable-disable.wav
        ├── level-complete.wav
        ├── light-off.wav
        └── light-on.wav
```

---

## 📂 Directory Overview

| Directory                          | Purpose                                                                   |
| ---------------------------------- | ------------------------------------------------------------------------- |
| `game/`                            | Core game loop, scenes, level management, rendering and window management |
| `game/scenes/`                     | Different game states and scenes                                          |
| `game/scenes/levels/`              | Individual playable and testing levels                                    |
| `game/scenes/menus/`               | Main menu and level-selection interfaces                                  |
| `math/`                            | Mathematical and geometric foundations                                    |
| `math/algebra/`                    | Vectors and matrix operations                                             |
| `math/shapes/`                     | Geometric shape definitions                                               |
| `math/shapes/polygons/`            | Polygon-based shapes                                                      |
| `models/`                          | Game objects and world entities                                           |
| `models/data/`                     | Data structures used by physics and ray calculations                      |
| `models/entities/`                 | Active entities such as lasers and light rays                             |
| `models/environment/`              | Objects that make up the optical environment                              |
| `models/environment/absorbers/`    | Objects that absorb light                                                 |
| `models/environment/reflectors/`   | Reflective optical objects                                                |
| `models/environment/refractors/`   | Refractive optical objects                                                |
| `models/environment/supernatural/` | Non-standard optical/gameplay objects such as portals                     |
| `physics/`                         | Physics, optics, raycasting and collision systems                         |
| `physics/colliders/`               | Collision geometry and collision detection                                |
| `physics/optics/`                  | Materials, media and optical interaction interfaces                       |
| `physics/rays/`                    | Ray representation and ray-hit calculations                               |
| `sprites/`                         | Image/sprite handling and rendering                                       |
| `input/`                           | Keyboard and mouse input                                                  |
| `ui/`                              | Custom user-interface components and layouts                              |
| `audio/`                           | Sound-effect handling                                                     |
| `resources/`                       | Fonts, images and audio assets                                            |
| `App.java`                         | Application entry point                                                   |

---

## 🧩 Major System Relationships

```text
                         ┌─────────────┐
                         │   App.java  │
                         └──────┬──────┘
                                │
                                ▼
                         ┌─────────────┐
                         │    Game     │
                         └──────┬──────┘
                                │
                                ▼
                         ┌─────────────┐
                         │   Scenes    │
                         └──────┬──────┘
                                │
                  ┌─────────────┼─────────────┐
                  ▼             ▼             ▼
              Levels         Menus          UI
                  │
                  ▼
            Game Objects
                  │
        ┌─────────┴─────────┐
        ▼                   ▼
    Colliders          Ray Interactables
        │                   │
        └─────────┬─────────┘
                  ▼
              Raycasting
                  │
                  ▼
           Optical Physics
           ┌──────┴──────┐
           ▼             ▼
      Reflection     Refraction
           │             │
           └──────┬──────┘
                  ▼
             Light Rays
```

---

## 🔢 Mathematical Foundation

```text
Vector2
   │
   ├── Position
   ├── Direction
   ├── Normal
   └── Dot Product
          │
          ▼
       Matrix2x2
          │
          └── Rotation
                │
                ▼
         Coordinate Geometry
                │
       ┌────────┼────────┐
       ▼        ▼        ▼
     Lines    Circles   Polygons
       │        │        │
       └────────┼────────┘
                ▼
          Intersection
                │
                ▼
             Rays
                │
       ┌────────┴────────┐
       ▼                 ▼
   Reflection         Refraction
       │                 │
       └────────┬────────┘
                ▼
           Optical Physics
```

---

# 📦 Resources

Game resources are kept separately from Java source code.

### Fonts

```text
resources/fonts/
└── Slackey-Regular.ttf
```

### Images

```text
resources/images/
├── background1.png
├── circular-mirror.png
├── laser-pointer.png
└── plane-mirror.png
```

### Sounds

```text
resources/sounds/
├── button-click.wav
├── button-hover.wav
├── laser-enable-disable.wav
├── level-complete.wav
├── light-off.wav
└── light-on.wav
```

This keeps source code and game assets separated while allowing the engine to load resources independently.

The exact package structure may evolve as development continues.

---

# ⚙️ Engine Design

The engine is built around a few major concepts.

## Scene

A `Scene` contains the objects that currently exist in the game world.

It manages:

* Game objects
* UI components
* Ray interactables
* Backgrounds
* Object addition/removal
* Scene updates
* Scene rendering

---

## Game Loop

The game uses a fixed update/render loop.

The current target is:

```text
100 FPS / UPS
```

The loop uses elapsed time to determine when an update should occur rather than relying purely on a fixed `sleep()` duration.

---

# 🚀 Performance

Some systems are designed with repeated raycasting in mind.

Raycasting can query scene objects many times per frame, so unnecessary allocations are avoided where possible.

For example, the scene maintains a dedicated list of objects that can interact with rays.

The resulting array is cached and only recreated when the list changes.

Conceptually:

```text
Scene changes
     │
     ▼
Invalidate cache
     │
     ▼
Next ray query
     │
     ▼
Create cached array
     │
     ▼
Reuse array
```

This reduces unnecessary object creation during frequent raycasting operations.

---

# 🧪 Technology

| Technology     | Version / Usage       |
| -------------- | --------------------- |
| Java           | **25**                |
| Java2D         | Rendering             |
| Swing          | Windowing and UI      |
| AWT            | Graphics and input    |
| BufferedImage  | Sprite/image handling |
| BufferStrategy | Rendering buffers     |

The project is intentionally kept close to Java's built-in graphics and input APIs.

---

# 💻 Requirements

Before running the project, make sure you have:

* **Java 25 or newer**
* A desktop environment capable of running Java Swing/AWT
* The project's resource files
* A Java compiler available through the command line

Check your Java installation with:

```bash
java -version
```

and:

```bash
javac -version
```

Both should report Java 25.

---

# ▶️ Running the Game

Open a terminal in the project directory.

Navigate to the source directory:

```bash
cd src
```

Then run:

```bash
java App.java
```

So the basic workflow is:

```bash
cd src
java App.java
```

If Java cannot find the class or resources, make sure the terminal is positioned at the correct project directory and that the project's resource structure is intact.

---

# 🎮 Controls

Controls may evolve as levels are added.

Current interaction concepts include:

| Input      | Action                         |
| ---------- | ------------------------------ |
| Mouse      | Interact / drag objects        |
| Left Mouse | Select / interact              |
| Right Mouse| Laser on/off                   |
| Q          | Rotate interactive object      |
| E          | Rotate interactive object      |
| R          | Disabled/enable laser          |
| P          | Debug / coordinate information |
| F3         | Debug of selected object       |

Additional controls may be introduced as more levels and mechanics are implemented.

---

# 🧮 Core Mathematical Topics

The project currently makes use of the following mathematical and physical concepts:

### Mathematics

* Trigonometry
* Sine and cosine
* Inverse trigonometric functions
* `atan2`
* Vectors
* Dot products
* Vector normalization
* Vector magnitude
* Matrices
* 2×2 rotation matrices
* Coordinate geometry
* Cartesian coordinates
* Line equations
* Line intersections
* Circle geometry
* Arc geometry
* Polygon geometry
* Distance calculations
* Surface normals
* Coordinate transformations

### Physics

* Reflection
* Surface normals
* Refraction
* Snell's Law
* Refractive indices
* Critical angle
* Total internal reflection
* Ray propagation

---

# 🔬 Physics Pipeline

The optical interaction system can be viewed as:

```text
             ┌──────────────┐
             │     LASER    │
             └──────┬───────┘
                    │
                    ▼
             ┌──────────────┐
             │     RAY      │
             └──────┬───────┘
                    │
                    ▼
          ┌─────────────────────┐
          │ Intersection Test   │
          └──────────┬──────────┘
                     │
                     ▼
              ┌────────────┐
              │  Surface   │
              │   Normal   │
              └─────┬──────┘
                    │
             ┌──────┴───────┐
             │              │
             ▼              ▼
       Reflection       Refraction
             │              │
             └──────┬───────┘
                    │
                    ▼
              New Ray Direction
                    │
                    ▼
               Continue Ray
```

This is the core loop behind the optical gameplay.

---

# 📏 World Units vs Screen Pixels

One important design principle of the engine is that **world units are not pixels**.

For example, an object might have:

```text
Radius = 50 world units
```

At one resolution it might render at:

```text
75 pixels
```

while at another resolution it could render at:

```text
92 pixels
```

The physical/logical radius is still:

```text
50 world units
```

Only the rendering scale changes.

This keeps physics consistent across different resolutions.

---

# 🔄 Coordinate Systems

Ray Shooter uses two primary coordinate spaces.

## World Space

Used by:

* Physics
* Colliders
* Rays
* Shapes
* Object positions
* Optical calculations

## Screen Space

Used by:

* Java2D rendering
* Mouse screen coordinates
* Actual pixel positions

The engine converts between them when necessary.

```text
World Space
    │
    │ worldToScreen()
    ▼
Screen Space


Screen Space
    │
    │ screenToWorld()
    ▼
World Space
```

This separation is essential for resolution independence.

---

# 🛠️ Development Philosophy

Ray Shooter is being developed as both a game and a learning project.

The project prioritizes understanding how the underlying systems work instead of immediately replacing them with external libraries.

The major goal is to understand the mathematics behind:

```text
Geometry
   ↓
Vectors
   ↓
Matrices
   ↓
Raycasting
   ↓
Collision Detection
   ↓
Surface Normals
   ↓
Reflection
   ↓
Refraction
   ↓
Optical Gameplay
```

---

# 📚 What This Project Demonstrates

Ray Shooter demonstrates practical applications of mathematics and physics in software.

### Mathematics → Game Engine

```text
Trigonometry
     ↓
Rotation & Angles

Vectors
     ↓
Directions & Positions

Matrices
     ↓
Transformations

Coordinate Geometry
     ↓
Intersections & Shapes

Geometry
     ↓
Collision & Raycasting
```

### Physics → Gameplay

```text
Reflection
     ↓
Laser bouncing

Refraction
     ↓
Light bending

Critical Angle
     ↓
Total Internal Reflection

Lenses
     ↓
Curved optical surfaces

Portals
     ↓
Spatial gameplay mechanics
```

---

# 🗺️ Levels

The game is designed around puzzle-style levels where the player manipulates the environment to control the path of light.

Potential puzzle elements include:

* Reflective surfaces
* Refractive materials
* Optical lenses
* Portals
* Moving objects
* Rotatable objects
* Light-routing challenges

Each level is intended to use the same underlying physics and rendering systems rather than implementing separate physics logic for individual puzzles.

---

# 🔧 Current Development

The engine is actively evolving.

Current development focuses on:

* Expanding levels
* Improving optical interactions
* Refining rendering
* Improving resolution independence
* Expanding interactive objects
* Improving UI
* Optimizing raycasting
* Refining the custom geometry system

---

# 📌 Design Goals

The project aims to maintain:

* **Mathematically consistent physics**
* **Resolution-independent rendering**
* **Reusable geometry**
* **Separated engine systems**
* **Custom raycasting**
* **Custom optical calculations**
* **Simple and understandable architecture**
* **Minimal unnecessary dependencies**

---

# 📜 License

This project is currently a personal development and learning project.

License information can be added here when the project is prepared for public distribution.

---

# 👤 Author

**Swarnim Rijal**

Built as a personal exploration of:

> **Java + Game Development + Computer Graphics + Mathematics + Optics**

---

# ⭐ Final Note

Ray Shooter started as a simple ray-shooting experiment and has evolved into a small custom engine combining programming, geometry, graphics, and physics.

The project is ultimately about taking mathematical ideas such as:

```text
vectors
matrices
trigonometry
coordinate geometry
reflection
refraction
```

and turning them into something interactive.

**The light isn't simulated by magic — it's calculated.** 🔦
