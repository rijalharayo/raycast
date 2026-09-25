# Menu Design

Ray Shooter uses `MenuScene` as the base class for scenes whose primary purpose is presenting and organizing user interface elements.

Unlike gameplay levels, menu scenes do not load game objects. Their main responsibility is managing UI components and layouts.

---

## Menu Scene Structure

All menu scenes inherit from `MenuScene`.

```text
Scene
  ↓
MenuScene
  ├── MainMenu
  └── LevelSelectionMenu
```

`MenuScene` provides the common functionality required by menus, including:

* menu naming
* background handling
* UI loading
* UI component updating
* layout management
* layout rendering
* screen-relative positioning

Individual menus only need to define their own UI.

---

## Creating a Menu

A new menu is created by extending `MenuScene`.

```java
public class ExampleMenu extends MenuScene {

     public ExampleMenu() {
          super("Example Menu");
     }

     @Override
     public void loadUI() {
          // Add menu UI
     }
}
```

The constructor calls the `MenuScene` constructor, which sets the menu name, assigns the default background, and calls `loadUI()`.

The initialization flow is therefore:

```text
ExampleMenu
     ↓
MenuScene constructor
     ↓
Set menu name
     ↓
Set background
     ↓
loadUI()
     ↓
Menu ready
```

A custom background can also be supplied through the overloaded constructor.

---

## UI Components and Layouts

`MenuScene` supports two ways of organizing UI:

```text
MenuScene
├── UI Components
└── UI Layouts
       └── UI Components
```

Direct UI components are stored by the base `Scene` class.

Layouts are stored separately inside `MenuScene`.

This allows simple UI elements to be placed directly while more structured groups of components can be managed through a `UILayout`.

---

## Direct UI Components

A menu can add individual UI components directly through the functionality inherited from `Scene`.

For example:

```java
@Override
public void loadUI() {
     UIText title = new UIText("Ray Shooter");
     add(title);
}
```

These components are updated automatically by `MenuScene.update()`.

---

## Layouts

Menus can also use `UILayout` when several UI components need to be treated as a group.

A layout is registered using:

```java
addLayout(layout);
```

The layout is then responsible for updating and rendering the components it contains.

Conceptually:

```text
MenuScene
     ↓
UILayout
     ↓
UI Components
```

This is useful for things such as:

* groups of menu buttons
* option lists
* level-selection grids
* vertically or horizontally arranged controls

---

## Layout Update

`MenuScene.update()` first performs the normal scene update.

It then updates directly registered UI components:

```java
for(UIComponent uiComponent : uiComponents) {
     uiComponent.update();
}
```

After that, layouts are updated:

```java
for(UILayout layout : layouts) {
     layout.update();
}
```

This keeps UI behavior centralized within the menu scene.

---

## Layout Rendering

Layouts are also rendered by `MenuScene`.

During rendering:

```text
MenuScene.render()
       ↓
Scene.render()
       ↓
Render layouts
```

Each registered layout receives the `Graphics2D` object and renders its contained UI.

Direct UI components are handled by the normal `Scene` rendering system.

---

## Default Menus

Ray Shooter currently provides two primary menu scenes:

```text
MenuScene
├── MainMenu
└── LevelSelectionMenu
```

They are stored as static instances inside `MenuScene`.

```java
private static MainMenu MAIN_MENU;
private static LevelSelectionMenu LEVEL_MENU;
```

These instances are initialized through:

```java
MenuScene.initailize();
```

The method creates both menu instances:

```java
MenuScene.LEVEL_MENU = new LevelSelectionMenu();
MenuScene.MAIN_MENU = new MainMenu();
```

This provides persistent menu instances that can be retrieved whenever the game needs to switch back to a menu.

---

## Accessing Menus

The initialized menus can be retrieved through:

```java
MenuScene.MAIN_MENU()
```

and:

```java
MenuScene.LEVEL_MENU()
```

For example, another scene can return to the level-selection menu through the scene manager.

```text
Gameplay
    ↓
MenuScene.LEVEL_MENU()
    ↓
Level Selection Menu
```

This avoids creating a new menu instance every time the player returns to it.

---

## Menu Initialization

Menu initialization is performed before the menus are used.

The initialization process is:

```text
MenuScene.initailize()
        ↓
Create LevelSelectionMenu
        ↓
Create MainMenu
        ↓
Menus become available
        ↓
SceneManager can switch between them
```

The menu instances are therefore created once and reused.

---

## Screen-Relative Positioning

`MenuScene` provides helper methods for positioning UI relative to the edges of the screen.

These methods represent the virtual screen boundaries:

```java
SCREEN_LEFT()
SCREEN_RIGHT()
SCREEN_TOP()
SCREEN_BOTTOM()
```

Conceptually:

```text
                 SCREEN_TOP
                     ↑
                     │
        ┌─────────────────────────┐
        │                         │
SCREEN  │          MENU           │  SCREEN
LEFT ←  │                         │  → RIGHT
        │                         │
        └─────────────────────────┘
                     │
                     ↓
               SCREEN_BOTTOM
```

This allows menu components to be positioned relative to screen boundaries rather than relying entirely on hard-coded coordinates.

For example, a component can be placed near the right side using `SCREEN_RIGHT()` and then applying an offset.

---

## Virtual Coordinate System

The screen-boundary helpers use the game's virtual dimensions.

```java
Game.WIDTH
Game.HEIGHT
```

The coordinate system is centered around the origin.

Therefore:

```text
SCREEN_LEFT()   → -WIDTH / 2
SCREEN_RIGHT()  →  WIDTH / 2

SCREEN_TOP()    →  HEIGHT / 2
SCREEN_BOTTOM() → -HEIGHT / 2
```

This makes it possible to reason about menu positions relative to the center and edges of the game rather than physical pixel coordinates.

---

## Menu and Level Differences

Menus and levels have different responsibilities.

```text
LevelScene
├── Game Objects
├── Laser
├── Target
├── Optical Environment
└── Gameplay UI

MenuScene
├── UI Components
├── UI Layouts
└── Navigation
```

A `LevelScene` is primarily responsible for gameplay.

A `MenuScene` is primarily responsible for presenting and organizing the interface.

This separation prevents menu logic from becoming mixed with gameplay systems.

---

## Menu Design Workflow

Creating a new menu generally follows this process:

```text
Create menu class
       ↓
Extend MenuScene
       ↓
Call super(...)
       ↓
Implement loadUI()
       ↓
Create UI components
       ↓
Organize components into layouts if needed
       ↓
Add layouts with addLayout()
       ↓
Register/initialize the menu
       ↓
Make it accessible through SceneManager
```

---

## Design Philosophy

The menu system is designed to keep menus lightweight and reusable.

`MenuScene` handles the common menu behavior while individual menu classes describe their actual interface.

The architecture can therefore be summarized as:

```text
MenuScene
   │
   ├── Common menu behavior
   │
   ├── UI component management
   │
   ├── Layout management
   │
   └── Screen positioning helpers
            │
            ↓
     Individual Menus
            │
            ├── MainMenu
            └── LevelSelectionMenu
```

The goal is to keep menu scenes focused on **UI composition and navigation**, while reusable UI components and layouts handle the details of rendering and interaction.
