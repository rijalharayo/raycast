# Interaction

This document explains how the player interacts with objects and the game environment in Ray Shooter.

---

## 1. Overview

Player interaction is primarily based around the mouse and keyboard.

The player can interact with objects by:

* hovering over them
* dragging them
* rotating them
* interacting with UI
* controlling the laser

The interaction system is implemented through the input and object systems rather than directly inside the game loop.

---

## 2. Mouse Input

`MouseInput` provides the information required for mouse-based interaction.

The mouse position is converted from screen coordinates into world coordinates before being used by game objects.

```text
Mouse Position
      ↓
Screen Coordinates
      ↓
Screen → World
      ↓
World Position
      ↓
Interaction Test
```

This ensures interaction remains consistent at different resolutions.

---

## 3. Hovering

Objects can determine whether the mouse is currently positioned over their collider.

Conceptually:

```text
Mouse
  ↓
World Position
  ↓
Collider Test
  ↓
Hovered
```

Hovering can then be used to determine whether an object is available for interaction.

---

## 4. Object Interaction Modes

Interactive optical objects use an interaction mode:

```text
NONE
DRAGGING
ROTATING
```

Only one interaction mode is active at a time.

Conceptually:

```text
          ┌── DRAGGING
          │
NONE ─────┤
          │
          └── ROTATING
```

---

## 5. Dragging

Dragging moves an object according to the mouse position.

The object continues being dragged after the interaction begins even if the cursor later leaves the object's collider.

This prevents interaction from unexpectedly stopping when the mouse moves slightly outside the object's original boundary.

---

## 6. Rotation

Objects can also be rotated while interacting with them.

The current control scheme uses:

```text
Q → rotate one direction
E → rotate the other direction
```

Rotation is performed while the appropriate interaction mode is active.

The object's geometry and collider are then updated to match its new orientation.

---

## 7. Interaction Priority

When the player begins interacting with an object, the system determines the intended interaction mode.

Dragging and rotation are mutually exclusive.

Conceptually:

```text
Mouse + Q/E
     ↓
Rotating

Mouse only
     ↓
Dragging
```

Once the mode begins, the interaction continues until the required input is released.

---

## 8. Optical Environment Updates

Moving or rotating an optical object changes the environment seen by the laser.

The object therefore marks the environment as dirty.

```text
Player Input
    ↓
Object Moves/Rotates
    ↓
Geometry Changes
    ↓
Environment Dirty
    ↓
Laser Recasts
```

This allows interaction and optical simulation to remain synchronized.

---

## 9. Keyboard Input

`KeyboardInput` handles keyboard state used by gameplay systems.

Keyboard input can control:

* object rotation
* laser controls
* menu navigation
* other gameplay actions

The input system provides state information rather than directly modifying game objects.

---

## 10. Separation of Input and Gameplay

The input system detects what the player is doing.

The object or gameplay system decides what that input means.

```text
Keyboard / Mouse
       ↓
Input System
       ↓
Gameplay System
       ↓
Object State
```

This prevents hardware-specific input logic from being embedded into individual objects.

---

## 11. Interaction and Physics

Interaction changes the physical state of objects, but does not directly perform raycasting.

For example:

```text
Mouse Drag
    ↓
Move Mirror
    ↓
Update Collider
    ↓
Environment Dirty
    ↓
Laser Recasts
```

This keeps the interaction system separate from the optical simulation.

---

## 12. Design Principle

The interaction system is intentionally simple:

$$
\text{Input}
\rightarrow
\text{Object State}
\rightarrow
\text{Environment Change}
\rightarrow
\text{Optical Update}
$$

The player manipulates the physical puzzle rather than directly manipulating the ray itself.

---

# Audio

This document explains the audio system used by Ray Shooter.

---

## 1. Overview

Ray Shooter uses short sound effects to provide feedback for important gameplay events.

The project stores sound resources in:

```text
resources/
└── sounds/
    ├── button-click.wav
    ├── button-hover.wav
    ├── laser-enable-disable.wav
    ├── level-complete.wav
    ├── light-off.wav
    └── light-on.wav
```

---

## 2. SoundEffect

`SoundEffect` provides the common interface for loading and playing sound effects.

The rest of the game does not need to directly manage low-level audio playback for every sound.

Conceptually:

```text
Gameplay Event
      ↓
SoundEffect
      ↓
Audio Playback
```

---

## 3. UI Feedback

UI interactions can trigger sounds such as:

```text
Button Hover
    ↓
button-hover.wav

Button Click
    ↓
button-click.wav
```

This provides immediate feedback when navigating menus.

---

## 4. Laser Feedback

The laser has audio feedback for state changes.

For example:

```text
Laser Enabled
    ↓
light-on.wav

Laser Disabled
    ↓
light-off.wav
```

The laser can also use:

```text
laser-enable-disable.wav
```

for its enable/disable interaction.

---

## 5. Level Completion

Successful completion of a level produces:

```text
level-complete.wav
```

This provides audio confirmation that the gameplay objective has been achieved.

---

## 6. Audio and Gameplay Separation

Gameplay systems trigger sound effects, but the audio implementation remains separate.

For example:

```text
Laser State
    ↓
Gameplay Event
    ↓
SoundEffect
    ↓
Audio Output
```

This prevents gameplay classes from needing to manage the underlying audio mechanism themselves.

---

## 7. Design Principle

Audio is treated as feedback rather than as part of the physical simulation.

A sound represents an event that occurred:

$$
\text{Gameplay Event}
\rightarrow
\text{Audio Feedback}
$$

The event remains meaningful even if the audio implementation changes later.
