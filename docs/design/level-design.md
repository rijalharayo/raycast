# Level Creation Guide

This document explains how to create and register a playable level in Ray Shooter.

Levels inherit the common functionality provided by `LevelScene`. A level mainly defines its objects and, when necessary, its custom UI.

---

## 1. Create the Level Class

Create a new class inside:

```text
src/main/game/scenes/levels/
```

The class must extend `LevelScene`.

For example:

```java
public class Level8 extends LevelScene {

}
```

---

## 2. Call the `LevelScene` Constructor

`LevelScene` provides overloaded constructors for levels.

The basic constructor is:

```java
public LevelScene(String levelName, int levelIndex)
```

A custom background can also be supplied:

```java
public LevelScene(
     String levelName,
     int levelIndex,
     Sprite backgroundImage
)
```

Therefore, a level normally calls `super(...)` from its constructor.

For example:

```java
public Level8() {
     super("Level 8", 8);
}
```

Or with a custom background:

```java
public Level8() {
     super("Level 8", 8, customBackground);
}
```

The `LevelScene` constructor automatically performs the common level initialization.

```text
Level Constructor
      ↓
super(...)
      ↓
LevelScene Constructor
      ├── Set level information
      ├── Set background
      ├── loadObjects()
      └── loadUI()
```

---

## 3. Implement `loadObjects()`

`LevelScene` declares `loadObjects()` as an abstract method.

Every level must therefore implement it.

```java
@Override
public void loadObjects() {

}
```

This method defines the physical contents of the level.

For example:

```java
@Override
public void loadObjects() {
     add(new Laser(...));
     add(new PlaneMirror(...));
     add(new TargetEnergyOrb(...));
}
```

The level does not need to manually register these objects with the raycasting system.

Calling `add()` allows `Scene` to handle normal object registration.

---

## 4. `add()` Automatically Handles Important Level Objects

`LevelScene` overrides `add(GameObject)`.

When an object is added, the level checks whether it is a:

* `TargetEnergyOrb`
* `Laser`

The first target is stored as:

```java
mainTarget
```

and the first laser is stored as:

```java
levelLaser
```

This gives the level direct access to its main gameplay objects.

Conceptually:

```text
add(GameObject)
      │
      ├── TargetEnergyOrb
      │       ↓
      │   mainTarget
      │
      └── Laser
              ↓
          levelLaser
```

The level therefore does not need to separately assign these objects after creating them.

---

## 5. One Main Laser and One Main Target

A level is designed around one main laser and one main target.

`LevelScene` enforces this by checking when objects are added.

If another target is added after the main target has already been assigned, the level throws an exception.

Likewise, a level should not contain multiple main lasers.

This gives every level a clearly defined:

```text
Laser
  ↓
Optical Puzzle
  ↓
Target
```

gameplay structure.

---

## 6. Default UI

Levels automatically receive default UI from `LevelScene`.

The inherited `loadUI()` method creates:

* the level label
* the default Back button

The level label displays the level index.

The Back button returns to the level-selection menu.

Therefore, a basic level does **not** need to create this UI itself.

```text
LevelScene
   │
   └── loadUI()
         ├── Level Label
         └── Back Button
```

---

## 7. Custom Level UI

The default `loadUI()` method can be overridden when a level needs additional or different UI.

For example:

```java
@Override
public void loadUI() {
     super.loadUI();

     // Add custom level UI
}
```

Calling `super.loadUI()` preserves the standard level UI while allowing the level to add its own components.

A level that does not need custom UI can simply use the inherited implementation.

---

## 8. Object Loading and UI Loading Are Separate

The level has two separate initialization responsibilities:

```text
loadObjects()
      ↓
Physical/Game Objects

loadUI()
      ↓
User Interface
```

`loadObjects()` defines the actual puzzle environment.

`loadUI()` defines the interface presented to the player.

---

## 9. Environment Dirty State

`LevelScene` also tracks whether the optical environment has changed.

```java
private boolean dirtyEnvironment = false;
```

When an object is moved or rotated, the environment can be marked dirty.

During `update()`, the level checks this flag:

```text
Environment changed
       ↓
dirtyEnvironment = true
       ↓
LevelScene.update()
       ↓
Laser.update()
       ↓
Ray path recalculated
       ↓
dirtyEnvironment = false
```

This allows the laser to update when the level's optical environment changes without unnecessarily recalculating its path every frame.

---

## 10. Level Update

`LevelScene` overrides `update()`.

It first performs the normal scene update:

```java
super.update();
```

Then, if the environment is dirty, the level updates its laser.

Afterward, the dirty flag is reset.

This makes the level responsible for coordinating changes to its optical environment.

---

## 11. Register the Level in `LevelManager`

Creating the class does not automatically make the level available to the game.

The new level must also be registered with `LevelManager`.

The overall relationship is:

```text
Level8
   ↓
LevelManager
   ↓
Level Selection
   ↓
Level8
```

After adding the level to the manager, it can be selected and loaded like the other levels.

---

## 12. Complete Level Creation Workflow

The actual workflow is:

```text
Create Level8.java
        ↓
extends LevelScene
        ↓
Create constructor
        ↓
super("Level 8", 8)
        ↓
Implement loadObjects()
        ↓
add(...) game objects
        ↓
(Optional) override loadUI()
        ↓
Register Level8 in LevelManager
        ↓
Level becomes available
```

---

## 13. Minimal Level

A level with only the required components can therefore be very small:

```java
public class Level8 extends LevelScene {

     public Level8() {
          super("Level 8", 8);
     }

     @Override
     public void loadObjects() {
          add(new Laser(...));
          add(new TargetEnergyOrb(...));
          add(new PlaneMirror(...));
     }
}
```

The rest is inherited from `LevelScene`.

The level automatically receives:

* scene functionality
* background handling
* default UI
* laser tracking
* target tracking
* environment dirty-state handling
* object registration

---

## 14. Design Principle

A level should describe **the composition of the puzzle**, not implement the engine systems used by that puzzle.

The level defines:

```text
What objects exist
        +
How they are arranged
        +
Optional custom UI
```

`LevelScene` provides the common level behavior.

The engine provides the underlying:

```text
Physics
Raycasting
Collision
Rendering
Input
Optical interactions
```

Therefore:

$$
\text{Level Definition}
\rightarrow
\text{LevelScene}
\rightarrow
\text{Engine Systems}
\rightarrow
\text{Gameplay}
$$

This allows new levels to be created primarily by composing existing objects and mechanics.
