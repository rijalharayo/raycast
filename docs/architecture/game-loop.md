# Game Loop

This document explains the main execution loop used by Ray Shooter and how game updates and rendering are coordinated.

---

## 1. Overview

Ray Shooter uses a traditional real-time game loop with two main stages:

```text
Update
  ↓
Render
  ↓
Update
  ↓
Render
  ↓
...
```

The update stage changes the state of the game, while the render stage draws the current state to the screen.

The loop is implemented in the `Game` class.

---

## 2. Update

The update stage is responsible for changing the state of the game.

It includes operations such as:

* processing input
* updating the current scene
* updating game objects
* updating optical objects
* updating rays
* handling interactions
* processing queued object changes

The current scene is responsible for updating its own objects:

```java
SceneManager.getCurrentScene().update();
```

The game loop therefore does not need to know how individual objects behave.

---

## 3. Render

After updating the game state, the current scene is rendered.

The rendering stage receives a `Graphics2D` context and draws:

* the background
* game objects
* optical objects
* rays
* sprites
* UI components
* visual effects

The scene controls the order in which these elements are rendered.

---

## 4. Fixed Update and Render Rate

Ray Shooter uses a fixed target update/render rate.

The current target is:

```text
100 updates per second
100 frames per second
```

The loop uses `System.nanoTime()` to measure elapsed time.

A time accumulator is used to determine when another update should occur.

This keeps the simulation independent from small variations in frame execution time.

---

## 5. Time Accumulator

The loop measures the time elapsed since the previous iteration.

That time is added to an accumulator.

Conceptually:

$$
A=A+\Delta t
$$

where:

* \(A\) is the accumulated time
* \(\Delta t\) is the elapsed time since the previous iteration

When the accumulator reaches the duration of one update step, an update is performed.

Afterward:

$$
A=A-\Delta t_{\text{update}}
$$

This allows the loop to maintain a stable update rate even when individual iterations take slightly different amounts of time.

---

## 6. Why `nanoTime()` Is Used

`System.nanoTime()` provides a high-resolution monotonic timer.

The important property is that it is intended for measuring elapsed time rather than representing wall-clock time.

This makes it suitable for:

* frame timing
* update timing
* elapsed-time calculations

The loop therefore does not depend on the system clock being adjusted.

---

## 7. Input Processing

Input is collected through the input systems:

```text
KeyboardInput
MouseInput
```

The current scene and its objects use this input to determine their behavior.

At the end of an update cycle, temporary input states can be reset so that events such as mouse movement or clicks are processed correctly during the next cycle.

---

## 8. Scene Delegation

The `Game` class does not directly update every object in the game.

Instead:

```text
Game
  ↓
SceneManager
  ↓
Current Scene
  ↓
Game Objects
```

This keeps the main game loop independent from individual levels and objects.

---

## 9. Rendering Pipeline

The overall execution flow is:

```text
Game Loop
    │
    ├── Input
    │
    ├── Update Current Scene
    │       │
    │       ├── Game Objects
    │       ├── Optical Objects
    │       ├── Rays
    │       └── UI
    │
    └── Render Current Scene
            │
            ├── Background
            ├── World Objects
            ├── Rays
            ├── Effects
            └── UI
```

---

## 10. BufferStrategy

Rendering uses Java's `BufferStrategy`.

The game uses multiple buffers so that drawing can occur on an off-screen buffer before the completed frame is presented.

This reduces visible tearing and prevents the player from seeing a partially rendered frame.

The current configuration uses:

```java
canvas.createBufferStrategy(3);
```

This creates a triple-buffered rendering setup.

---

## 11. Separation of Update and Render

The update stage changes state.

The render stage reads that state and draws it.

Conceptually:

```text
Input
  ↓
Update
  ↓
Game State
  ↓
Render
  ↓
Screen
```

Rendering should therefore not be responsible for changing the physical state of the world.

This separation is particularly important for Ray Shooter because raycasting and optical interactions modify game state independently of how the scene is displayed.

---

## 12. Summary

The Ray Shooter game loop provides a central execution cycle:

$$
\text{Input}
\rightarrow
\text{Update}
\rightarrow
\text{Render}
$$

The `Game` class controls the timing, while `SceneManager` determines which scene is currently active.

The scene then delegates updates and rendering to the objects it contains.

This keeps the main loop small while allowing individual systems to manage their own behavior.

---

# Scene System

This document explains how scenes organize game objects, levels, ray-interactable objects, and UI components in Ray Shooter.

---

## 1. Overview

A scene represents a complete game state.

Examples include:

* the main menu
* level selection
* an individual game level
* test environments

The main scene classes include:

```text
Scene
├── MenuScene
├── LevelScene
│   ├── Level1
│   ├── Level2
│   ├── Level3
│   ├── ...
│   └── TestLevel
└── other scene implementations
```

---

## 2. Scene Base Class

`Scene` provides the common functionality required by all scenes.

A scene maintains collections for its different types of content.

Conceptually:

```text
Scene
├── Game Objects
├── Ray Interactables
├── UI Components
├── Objects To Add
└── Objects To Remove
```

This allows different scene types to share the same object-management system.

---

## 3. Game Objects

Game objects represent objects that exist within the scene.

They can include:

* optical objects
* lasers
* targets
* other environment objects

The scene maintains these objects in:

```java
protected final List<GameObject> gameObjects =
     new ArrayList<>();
```

Objects are therefore owned by the scene rather than by the global game loop.

---

## 4. Queued Object Changes

Objects are not always added or removed immediately.

The scene maintains separate queues:

```java
protected final List<GameObject> objectsToAdd =
     new ArrayList<>();

protected final List<GameObject> objectsToRemove =
     new ArrayList<>();
```

This prevents modifying the main object collection while it is being iterated.

The general process is:

```text
Request Add/Remove
        ↓
Queue Operation
        ↓
Finish Current Processing
        ↓
Apply Changes
```

This makes object lifecycle changes safer during updates and raycasting.

---

## 5. Ray Interactable Objects

Not every game object needs to participate in raycasting.

Objects that can interact with light rays are represented through the `RayInteractable` abstraction.

The scene therefore maintains a dedicated collection:

```java
protected final List<RayInteractable> rayInteractables =
     new ArrayList<>();
```

This avoids testing every game object against every ray.

The raycasting system can directly query the objects capable of interacting with rays.

---

## 6. Cached Ray Interactables

The scene also maintains a cached array:

```java
private RayInteractable[] cachedRayInteractables;
```

The array is rebuilt only when necessary.

Conceptually:

```text
Ray Interactable List
        ↓
   Cache Invalidated
        ↓
   Rebuild Array
        ↓
     Raycasting
```

The getter creates the array when the cache is empty:

```java
public RayInteractable[] getSceneRayInteractables() {
     if (cachedRayInteractables == null) {
          cachedRayInteractables =
               rayInteractables.toArray(new RayInteractable[0]);
     }

     return cachedRayInteractables;
}
```

This avoids repeatedly allocating a new array during raycasting.

---

## 7. Environment Dirty State

The scene also tracks whether its optical environment has changed.

This is useful because the laser does not need to recalculate its entire ray path every frame if nothing relevant has changed.

Changes such as:

* moving an optical object
* rotating an optical object
* adding an optical object
* removing an optical object

can mark the environment as dirty.

Conceptually:

```text
Environment changes
        ↓
Environment marked dirty
        ↓
Laser detects change
        ↓
Ray path recalculated
```

This allows the raycasting system to avoid unnecessary calculations.

---

## 8. Scene Manager

`SceneManager` controls which scene is currently active.

The game loop asks the scene manager for the current scene:

```java
SceneManager.getCurrentScene();
```

The relationship is:

```text
Game
  ↓
SceneManager
  ↓
Current Scene
```

This allows the game to switch between menus, levels, and other scenes without changing the main game loop.

---

## 9. Level Scenes

`LevelScene` provides functionality specific to playable levels.

Individual levels extend this system:

```text
LevelScene
   │
   ├── Level1
   ├── Level2
   ├── Level3
   ├── Level4
   ├── ...
   └── TestLevel
```

Each level defines its own collection and arrangement of objects while reusing the common scene infrastructure.

---

## 10. Scene Lifecycle

A scene generally follows this lifecycle:

```text
Create
  ↓
Initialize
  ↓
Update
  ↓
Render
  ↓
Switch / Destroy
```

The scene owns the objects while it is active.

When another scene becomes active, the scene manager changes the current scene.

---

## 11. Scene Responsibility

The scene is responsible for organizing the world.

It should know:

* what objects exist
* which objects interact with rays
* which UI elements exist
* which objects need to be added or removed
* whether the optical environment changed

The scene does not need to implement the internal physics of each object.

That responsibility belongs to the corresponding object or subsystem.

---

## 12. Summary

The scene system provides the structural layer between the game loop and individual objects:

$$
\text{Game}
\rightarrow
\text{SceneManager}
\rightarrow
\text{Scene}
\rightarrow
\text{Objects}
$$

Dedicated ray-interactable collections and caching allow the optical system to access only the objects relevant to raycasting.

Queued object modifications provide safe object lifecycle management.

Together, these systems allow levels and menus to share a common scene architecture while remaining independent from the main game loop.
