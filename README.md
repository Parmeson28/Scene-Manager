# Scene Manager

A small tile-based scene editor for 2D games, built with [libGDX](https://libgdx.com/) and Java.

Pan around a grid, pick a tile texture, and paint or erase tiles with the mouse. It is the foundation of a level editor for a simple 2D game, and it is still in early development.

## Features

- **Tile painting**: hold the left mouse button and drag to paint tiles across the grid.
- **Tile erasing**: hold the right mouse button to remove tiles.
- **Multiple tile textures**: switch the active texture with the number keys.
- **Camera panning**: move around the scene with `W` `A` `S` `D`.
- **Resolution independent**: the scene is rendered through a 1920×1080 `FitViewport`, and mouse clicks are converted to world coordinates through that viewport, so painting stays accurate when the window is resized or goes fullscreen.
- **Automatic texture loading**: every image in `assets/tilesFolder` is picked up at startup, with no code changes needed.

## Controls

| Input | Action |
| --- | --- |
| Left mouse button (hold) | Paint tiles with the selected texture |
| Right mouse button (hold) | Erase tiles |
| `1` / `2` | Select tile texture 1 / 2 |
| `W` `A` `S` `D` | Move the camera |
| `B` | Fullscreen |
| `Esc` | Return to windowed mode |

## Getting started

### Requirements

- JDK 8 or newer (the project compiles to Java 8 bytecode)
- Gradle is not required; the included wrapper (`gradlew` / `gradlew.bat`) downloads it

### Run

1. Clone the repository:

   ```bash
   git clone https://github.com/Parmeson28/Scene-Manager.git
   cd Scene-Manager
   ```

2. Run any Gradle task once so the asset list is generated (this creates `assets/assets.txt`):

   ```bash
   ./gradlew build        # Windows: gradlew.bat build
   ```

3. Start `com.javaProjects.game.lwjgl3.Lwjgl3Launcher` from your IDE, with the **working directory set to the project root**. The code loads files with paths such as `assets/assets.txt`, so they must resolve from the root.

### Build a runnable jar

```bash
./gradlew lwjgl3:jar
```

The jar is written to `lwjgl3/build/libs`.

## Adding your own tiles

1. Put an image in `assets/tilesFolder/`.
2. Run a Gradle task again to regenerate `assets/assets.txt`.
3. The new texture is loaded automatically at startup.

Textures are indexed in the order they appear in `assets.txt`, which is alphabetical:

| Index | File | Used for |
| --- | --- | --- |
| 0 | `aatile.jpg` | The default tile that fills every empty cell |
| 1 | `firstTile.jpg` | Key `1` |
| 2 | `grass.jpg` | Key `2` |

A new file changes the numbering if it sorts before the existing ones. Only keys `1` and `2` are mapped at the moment.

## Project structure

```
Scene-Manager/
├── assets/
│   ├── tilesFolder/      Tile textures loaded by the grid
│   ├── spritesFolder/    Sprite images
│   └── metalui/          UI skin (Metal UI) for scene2d widgets
├── core/                 Shared game logic
│   └── src/main/java/com/javaProjects/game/
│       ├── Manager.java       Main application: input, rendering loop, UI stage
│       ├── TileGrid.java      Tile storage, texture loading and drawing
│       └── CameraHandle.java  Camera, viewport and WASD movement
└── lwjgl3/               Desktop launcher (LWJGL3)
```

### How it works

- `TileGrid` keeps placed tiles in a `HashMap<Vector2, Integer>` that maps a grid position to a texture index. Empty cells are drawn with texture 0.
- A click is converted from screen to world coordinates with `Viewport.unproject`, then snapped to the 32 px grid with `MathUtils.floor`, so negative coordinates land in the right tile.
- Painting a tile that already exists replaces its texture. Tiles placed earlier keep their texture when you change the selected one.

## Tech stack

- [libGDX](https://libgdx.com/) 1.13.1 (project generated with [gdx-liftoff](https://github.com/libgdx/gdx-liftoff))
- LWJGL3 desktop backend
- scene2d UI with the Metal UI skin
- Gradle

## Status

Early work in progress. Not implemented yet:

- Saving and loading scenes
- Texture selection beyond keys `1` and `2`
- A working in-game UI (the scene2d stage currently only holds a placeholder button)

## Gradle tasks

The project uses the standard gdx-liftoff setup, so the usual tasks are available: `build`, `clean`, `lwjgl3:jar`, `idea`, `eclipse`. Prefix a task with a module name (for example `core:clean`) to run it for that module only.
