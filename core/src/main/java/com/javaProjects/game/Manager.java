package com.javaProjects.game;

import java.io.BufferedReader;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.javaProjects.game.data.SaveAndLoadMap;
import com.javaProjects.game.data.Structure;
import com.javaProjects.game.handlers.CameraHandle;
import com.javaProjects.game.handlers.InputHandler;
import com.javaProjects.game.ui.EditorUi;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Manager extends ApplicationAdapter {

    private CameraHandle camera = new CameraHandle();

    private TileGrid tileGrid = new TileGrid();

    private InputHandler inputHandler = new InputHandler();

    public Sprite catSprite;
    public Texture catTexture;

    //Exclusive variables for the level editor

    private BufferedReader br;

    private Texture defaultTexture;

    private Array<Texture> textures;

    private SpriteBatch tilesSpriteBatch;
    private SpriteBatch objectsSpriteBatch;

    private int tileTexture;

    int currentT;

    //Handling mouse click variables
    private Vector2 deleteTouchPoint = new Vector2();
    public Vector2 selectedTile = new Vector2();
    public Vector2 deleteSelectedTile = new Vector2();

    //Changing texture variables
    public Rectangle tileRec = new Rectangle();

    //UI
    private EditorUi ui;
    private boolean gridReady = false;

    //Save system
    SaveAndLoadMap saveAndLoad = new SaveAndLoadMap();

    @Override
    public void create() {
        camera.cameraConfig();

        tileGrid.takeTileTextures();

        tilesSpriteBatch = new SpriteBatch();
        objectsSpriteBatch = new SpriteBatch();

        currentT = 1;
        tileTexture = currentT;

        ui = new EditorUi();
        ui.setupScreen(); 
        
    }

    @Override
    public void render() {
  
        camera.zoom();

        if (ui.isSetupDone() && !gridReady) {
            tileGrid.setGridSize(ui.getWidth(), ui.getHeight());
            gridReady = true;
        }
        
        camera.cameraMovement(objectsSpriteBatch);
        
        camera.cameraMovement(tilesSpriteBatch);


        Gdx.gl.glClearColor(0, 0, 0, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Fullscreen
        inputHandler.fullScreenMode(camera);
        
        if (ui.isSetupDone()) {
            int st = inputHandler.selectTileSC(currentT);
            if (st != currentT) {
                    currentT = st;
            }
        }
        
        if (ui.isSetupDone()) {
            tilesSpriteBatch.begin();
            tileGrid.renderTiles(tilesSpriteBatch, 0);
            tilesSpriteBatch.end();

            objectsSpriteBatch.begin();
            tileGrid.renderObjects(objectsSpriteBatch);
            objectsSpriteBatch.end();
        }

        // No editing while the setup window is open or while the mouse is over the UI
        boolean overUI = !ui.isSetupDone() || ui.isPointerOverUI();

        if (ui.isSetupDone()) {
            inputHandler.rightClickOptions(camera, ui, tileGrid);
        }
        inputHandler.placeTiles(camera, tileGrid, tileTexture, overUI);

        ui.drawUi();
    }
    
    public Structure createNewStructure(String type, String name, String model, float x, float z){
        return new Structure(type, name, model, x, z);
    }

    @Override
    public void resize(int width, int height) {
        if (camera.viewport != null) {
            camera.viewport.update(width, height, false);
        }
        ui.resize(width, height);
    }

    @Override
    public void dispose() {
        tilesSpriteBatch.dispose();

        tileGrid.disposeTextures();

        ui.dispose();
    }
}