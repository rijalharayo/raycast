# Rendering

This document explains the rendering architecture used by Ray Shooter, including world coordinates, screen coordinates, resolution scaling, Java2D rendering, sprites, and visual effects.

---

## 1. Overview

Ray Shooter separates the game world's logical coordinates from the physical screen resolution.

The world uses a fixed virtual resolution:

```text
1603 × 902
```

The actual window or display may have a different resolution.

The renderer converts between these coordinate systems when drawing.

The basic pipeline is:

```text
World Object
    ↓
World Coordinates
    ↓
World → Screen Transformation
    ↓
Screen Coordinates
    ↓
Java2D
    ↓
Display
```

---

## 2. Virtual World Resolution

The game uses:

```java
WORLD_WIDTH = 1603
WORLD_HEIGHT = 902
```

These values define the logical size of the game world.

Game objects therefore do not need to know the physical resolution of the player's monitor.

For example, an object positioned at a particular world coordinate remains at that coordinate regardless of whether the game is rendered at:

```text
1280 × 720
1920 × 1080
2560 × 1440
```

---

## 3. World Coordinates

The game uses a world coordinate system in which the center of the virtual world is:

$$
C=
\left(
\frac{W}{2},
\frac{H}{2}
\right)
$$

For the current virtual resolution:

$$
C=
\left(
\frac{1603}{2},
\frac{902}{2}
\right)
$$

World coordinates are centered around this point.

---

## 4. Coordinate Orientation

The mathematical/game world uses an upward-positive \(Y\)-axis.

Screen coordinates, however, use a downward-positive \(Y\)-axis.

Therefore:

```text
World                     Screen

      +Y                      -Y
       ↑                       ↓
       │                       │
       │                       │
       └──────→ +X             └──────→ +X
```

The renderer must therefore invert the vertical coordinate during conversion.

---

## 5. World to Screen

The world coordinate is first converted into virtual screen coordinates.

Conceptually:

$$
x_v=x+C_x
$$

$$
y_v=C_y-y
$$

where:

* \(x,y\) are world coordinates
* \(C_x,C_y\) are the virtual world center
* \(x_v,y_v\) are virtual screen coordinates

The result is then scaled to the actual screen.

---

## 6. Resolution Scaling

The renderer calculates separate horizontal and vertical scale factors:

$$
s_x=\frac{W_s}{W_w}
$$

$$
s_y=\frac{H_s}{H_w}
$$

where:

* \(W_s,H_s\) are the actual screen dimensions
* \(W_w,H_w\) are the virtual world dimensions

To preserve the aspect ratio, the smaller scale is selected:

$$
\boxed{
s=\min(s_x,s_y)
}
$$

This produces uniform scaling.

---

## 7. Letterboxing

Because the actual screen may have a different aspect ratio from the virtual world, uniform scaling may leave unused space.

The renderer calculates offsets:

$$
o_x=
\frac{W_s-W_ws}{2}
$$

$$
o_y=
\frac{H_s-H_ws}{2}
$$

The final screen position is:

$$
x_s=x_vs+o_x
$$

$$
y_s=y_vs+o_y
$$

This keeps the world centered while preserving its aspect ratio.

---

## 8. Screen to World

Mouse coordinates need to be converted back into world coordinates.

The transformation is reversed:

$$
x_v=\frac{x_s-o_x}{s}
$$

$$
y_v=\frac{y_s-o_y}{s}
$$

Then:

$$
x=x_v-C_x
$$

$$
y=C_y-y_v
$$

This allows mouse interaction to remain consistent regardless of the display resolution.

---

## 9. Physics Does Not Use Screen Scaling

World geometry should not be modified when the resolution changes.

For example, the following should remain in world units:

* collider positions
* ray positions
* shape dimensions
* lens curvature
* mirror geometry
* portal dimensions
* ray directions

Scaling occurs only during rendering.

Conceptually:

```text
Physics
   │
   └── World Units
          ↓
       Renderer
          ↓
       Scaling
          ↓
      Screen Pixels
```

This prevents the physics system from becoming dependent on the player's resolution.

---

## 10. Shape Rendering

Shapes are represented mathematically in world coordinates.

Examples include:

```text
Circle
Arc
Polygon
Rectangle
Triangle
```

During rendering, their positions and dimensions are converted into screen coordinates.

For polygons, each world-space vertex is transformed individually.

For circles and arcs, their radii are multiplied by the world scale when rendered.

Angles themselves are not scaled.

---

## 11. Sprite Rendering

Sprites are handled separately from mathematical shapes.

A sprite has an image with a logical size and a separate scale.

The renderer converts the sprite's logical dimensions into screen dimensions using the world scale.

Conceptually:

$$
W_s=W_ws
$$

$$
H_s=H_ws
$$

The sprite can therefore have its own logical scale while still remaining resolution independent.

---

## 12. Background Rendering

The background is treated differently from world objects.

A background image can be rendered directly across the actual screen:

```text
Screen
┌──────────────────────────────┐
│                              │
│          Background          │
│                              │
│                              │
└──────────────────────────────┘
```

This prevents letterboxing from producing unwanted gaps around the background.

The background therefore belongs to the screen-space rendering layer rather than the physical world.

---

## 13. Rendering Order

The scene controls the order in which objects are drawn.

A typical rendering order is:

```text
Background
    ↓
World Objects
    ↓
Optical Objects
    ↓
Ray Effects
    ↓
Sprites
    ↓
UI
```

Objects rendered later appear above objects rendered earlier.

The exact ordering can depend on the scene and object type.

---

## 14. Portal Glow

Portal glow is implemented using layered geometry rather than a real blur operation.

Several increasingly large shapes are drawn behind the actual portal frame.

Conceptually:

```text
Large transparent glow
        ↓
Medium transparent glow
        ↓
Small transparent glow
        ↓
Actual portal frame
```

Each layer becomes smaller and more opaque toward the center.

This creates the appearance of a glow while keeping the effect compatible with the Java2D rendering pipeline.

---

## 15. UI Rendering

UI components use the same virtual coordinate philosophy.

UI layout is calculated using logical/world-style dimensions rather than physical pixels.

Rendering then applies the appropriate screen scaling.

This keeps UI elements consistent across different resolutions.

---

## 16. Java2D

Ray Shooter uses Java2D and `Graphics2D` as its rendering backend.

The renderer uses Java's built-in drawing functionality for:

* lines
* shapes
* images
* text
* transformations
* compositing

This avoids depending on a separate graphics engine.

---

## 17. Buffering

Rendering uses `BufferStrategy` to present completed frames.

The general process is:

```text
Game State
    ↓
Graphics2D
    ↓
Back Buffer
    ↓
BufferStrategy
    ↓
Screen
```

Multiple buffers reduce the possibility of visible tearing and partially drawn frames.

---

## 18. Rendering and Physics Separation

Rendering does not define the physical world.

Instead:

```text
Physics
   ↓
World State
   ↓
Renderer
   ↓
Visual Representation
```

The same world object can therefore be rendered at different screen resolutions without changing its physical properties.

---

## 19. Summary

The rendering architecture can be summarized as:

$$
\text{World Coordinates}
\rightarrow
\text{Coordinate Conversion}
\rightarrow
\text{Resolution Scaling}
\rightarrow
\text{Screen Coordinates}
\rightarrow
\text{Java2D}
$$

The key design principle is:

> **World geometry belongs to the simulation; scaling belongs to the renderer.**

This keeps Ray Shooter resolution independent while allowing the physics, optics, collision, and raycasting systems to operate entirely in stable world coordinates.
