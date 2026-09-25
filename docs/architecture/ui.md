# UI

This document explains the user-interface architecture used by Ray Shooter.

---

## 1. Overview

The UI system provides menus, buttons, text, and other interface elements used to interact with the game.

The main UI classes include:

```text
UIComponent
├── UIButton
└── UIText
```

Layout functionality is provided through:

```text
UILayout
└── UIWrapLayout
```

---

## 2. UI Components

`UIComponent` provides the common foundation for interface elements.

Individual components then implement their own rendering and interaction behavior.

The main components currently include:

* `UIButton`
* `UIText`

---

## 3. Buttons

`UIButton` represents an interactive UI button.

A button can provide:

* displayed text
* position
* dimensions
* visual state
* mouse interaction
* click behavior

Conceptually:

```text
Mouse
  ↓
Button Bounds
  ↓
Hover / Click
  ↓
Action
```

---

## 4. Button States

Buttons can respond differently depending on the mouse state.

For example:

```text
Normal
  ↓
Hovered
  ↓
Pressed
```

Hover and click sounds can also provide feedback.

---

## 5. Text

`UIText` is responsible for displaying text.

It provides a reusable component for things such as:

* menu titles
* labels
* instructions
* status information

Font rendering is scaled according to the current world/display scale so that text remains visually consistent across resolutions.

---

## 6. Fonts

The project contains custom fonts in:

```text
resources/
└── fonts/
    └── Slackey-Regular.ttf
```

The `Fonts` class centralizes font handling so that UI components do not need to independently load or manage font resources.

---

## 7. UI Layout

`UILayout` provides the foundation for arranging UI components.

Instead of manually positioning every component independently, layout systems can determine where components should be placed.

`UIWrapLayout` provides wrapping behavior when multiple UI elements need to fit within a defined width.

Conceptually:

```text
Available Width
      ↓
Add Component
      ↓
Fits?
 ┌────┴────┐
Yes        No
 ↓          ↓
Same Row   New Row
```

---

## 8. Virtual UI Coordinates

The UI uses logical dimensions rather than directly depending on physical screen pixels.

This follows the same resolution-independent philosophy as the game world.

```text
UI Layout
   ↓
Virtual Coordinates
   ↓
Rendering Scale
   ↓
Screen
```

The layout itself should therefore not need to know the physical display resolution.

---

## 9. Menus

The UI system is used by menu scenes such as:

```text
MainMenu
LevelSelectionMenu
```

The relationship is:

```text
Menu Scene
    ↓
UI Components
    ↓
Player Input
    ↓
Menu Action
    ↓
Scene Change
```

For example, a level-selection button can trigger a change to a specific level through the scene/level management system.

---

## 10. Gameplay UI

UI can also be used during gameplay for information and controls.

Examples include:

* laser controls
* level information
* completion feedback
* gameplay instructions

The UI remains separate from the physical game objects.

---

## 11. UI and Input

UI components receive input information and determine whether the input applies to them.

For example:

```text
Mouse Position
      ↓
UI Button Bounds
      ↓
Hovered
      ↓
Mouse Click
      ↓
Button Action
```

This prevents the main game loop from having to understand the internal behavior of every button.

---

## 12. UI Rendering

UI is rendered after the relevant world/gameplay elements so that interface elements remain visible above the game world.

A simplified rendering order is:

```text
Background
    ↓
World
    ↓
Optical Objects
    ↓
Rays
    ↓
UI
```

---

## 13. UI and Resolution Independence

UI coordinates are maintained in the logical coordinate system.

Rendering applies the appropriate scale when converting them to screen coordinates.

This means a button with a logical size remains proportionally consistent when the window resolution changes.

---

## 14. Design Principle

The UI system separates three responsibilities:

```text
Component
   ↓
Appearance

Layout
   ↓
Positioning

Input
   ↓
Interaction
```

This prevents individual UI components from becoming responsible for the entire interface system.

---

## 15. Summary

The UI architecture provides reusable components and layouts while remaining independent from the underlying gameplay and physics systems.

The general flow is:

$$
\text{Input}
\rightarrow
\text{UI Component}
\rightarrow
\text{Action}
\rightarrow
\text{Game System}
$$

This allows menus and gameplay interfaces to use the same underlying UI infrastructure.
