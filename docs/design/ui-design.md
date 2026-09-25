# UI Design

Ray Shooter uses a lightweight custom UI system designed around the same virtual coordinate system used by the game world.

The UI is built from reusable components and layouts rather than being tightly coupled to individual game scenes.

---

## UI Structure

The main UI system is organized around three core concepts:

```text
UIComponent
     │
     ├── UIText
     └── UIButton

UILayout
     │
     └── UIWrapLayout
```

`UIComponent` provides the base functionality shared by UI elements, while `UIText` and `UIButton` provide specific visual and interactive components.

`UILayout` handles the positioning and arrangement of UI components.

---

## Scene UI

Each `Scene` maintains its own collection of UI components.

A scene can add UI components in the same way it adds game objects.

```text
Scene
 ├── Game Objects
 ├── Ray Interactables
 └── UI Components
```

This keeps the UI associated with the scene currently being displayed.

When the scene changes, its UI changes with it.

---

## Level UI

`LevelScene` provides a default UI for every level through its `loadUI()` method.

The default level interface contains:

* the current level label
* a Back button

Conceptually:

```text
┌──────────────────────────────────────────┐
│ Level X                         [ Back ] │
│                                          │
│                                          │
│              GAME WORLD                  │
│                                          │
│                                          │
└──────────────────────────────────────────┘
```

This common interface means individual levels do not need to recreate basic navigation elements.

---

## Default Level UI

The default UI is created inside `LevelScene.loadUI()`.

The level label displays the level index:

```java
UIText levelLabel = new UIText("Level " + this.levelIndex);
```

The Back button returns to the level-selection menu:

```java
UIButton backButton = new UIButton("Back", 150, 80);
```

The positions and sizes are defined using the game's virtual coordinate system.

---

## Custom Level UI

Levels can provide additional UI when their mechanics require it.

A level can override `loadUI()`:

```java
@Override
public void loadUI() {
     super.loadUI();

     // Add custom UI
}
```

Calling `super.loadUI()` preserves the standard level interface.

Custom components can then be added on top of it.

This allows the common UI to remain consistent while still allowing individual levels to introduce level-specific controls or information.

---

## UI Components

### UIText

`UIText` is responsible for displaying text.

It provides functionality such as:

* text content
* font size
* position
* rendering

For example:

```java
UIText text = new UIText("Level 1");
text.setSize(40f);
text.setPosition(-648.5f, 397f);
```

Text size is specified in virtual units and scaled during rendering.

---

### UIButton

`UIButton` provides an interactive button containing text.

A button can define:

* width
* height
* text size
* position
* click behavior

For example:

```java
UIButton button = new UIButton("Back", 150, 80);

button.setTextSize(30f);

button.setOnClick(
     () -> SceneManager.setScene(MenuScene.LEVEL_MENU())
);
```

The button therefore combines visual rendering with user interaction.

---

## UI Positioning

UI components use the same virtual coordinate system as the rest of the game.

The game's virtual resolution is:

```text
1603 × 902
```

The UI is therefore designed against this fixed logical coordinate space rather than directly against the physical monitor resolution.

This allows the same UI layout to work across different screen resolutions.

```text
Virtual UI coordinates
        ↓
World/screen transformation
        ↓
Actual screen pixels
```

---

## Resolution Independence

The physical screen resolution does not directly determine UI coordinates.

Instead, the UI is rendered using the game's world scale.

For example, a component with a virtual width of `150` remains logically `150` units wide regardless of whether the game is rendered at a smaller or larger resolution.

The renderer converts the virtual dimensions into screen pixels.

This keeps UI proportions consistent with the rest of the game.

---

## UI Layouts

`UILayout` provides a way to organize multiple UI components without manually positioning every element.

Layouts operate using virtual coordinates.

This is important because layout calculations should remain independent of the physical screen resolution.

For example:

```text
Virtual layout
      ↓
Calculate positions
      ↓
Apply world scale
      ↓
Render to screen
```

`UIWrapLayout` provides wrapping behavior for groups of components, allowing elements to move onto subsequent rows when the available virtual width is exceeded.

---

## Rendering and Interaction

UI rendering and UI interaction are separate responsibilities.

Rendering converts the UI's virtual position and dimensions into screen coordinates.

Interaction converts the mouse position into the same coordinate system used by the UI.

This allows hit detection to remain consistent when the window is resized or rendered at a different resolution.

```text
Mouse position
      ↓
Screen → virtual coordinates
      ↓
UI hit detection
      ↓
Button action
```

---

## UI and Game World Separation

Although UI and game objects use the same virtual coordinate system, they serve different purposes.

```text
Game World
├── Physics
├── Collisions
├── Raycasting
└── Optical Objects

UI
├── Text
├── Buttons
├── Layouts
└── User Interaction
```

UI components do not participate in the game's physics or raycasting systems.

Likewise, game objects do not depend on UI components to perform their gameplay behavior.

This keeps the gameplay systems and interface systems independent.

---

## Scene-Specific UI

Because each scene owns its UI components, different scenes can have completely different interfaces.

For example:

```text
Main Menu
    ↓
Menu UI

Level Selection
    ↓
Level Selection UI

Level
    ↓
Level UI

Gameplay
    ↓
Level-specific UI
```

When `SceneManager` switches scenes, the active scene's UI becomes the visible interface.

---

## UI Design Philosophy

The UI system follows a few basic principles:

### Reusable

Common elements such as buttons and text are implemented once and reused throughout the game.

### Scene-Owned

UI belongs to the scene that uses it rather than being globally attached to the game.

### Resolution Independent

UI is designed using virtual coordinates and scaled during rendering.

### Extensible

Scenes can use the default UI or add custom components when necessary.

### Separated From Gameplay

UI interaction does not become part of the physics, collision, or raycasting systems.

---

## UI Workflow

Adding UI to a scene follows a simple process:

```text
Create UI Component
        ↓
Configure text / size / position
        ↓
Configure interaction if needed
        ↓
Add component to Scene
        ↓
Scene renders and handles the UI
```

For levels, the default UI is already provided by `LevelScene`.

A level only needs to add custom UI when its design requires it.

---

## Design Goal

The UI system is intentionally small.

Rather than introducing a large external UI framework, Ray Shooter uses a custom collection of components and layouts that are sufficient for the game's needs.

The goal is to keep the UI:

* reusable
* resolution independent
* easy to extend
* independent from gameplay systems
* consistent across scenes

This allows the interface to remain part of the engine's architecture without becoming tightly coupled to the game logic.
