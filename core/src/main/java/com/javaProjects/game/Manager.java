package com.javaProjects.game;

import java.io.BufferedReader;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
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

    private SpriteBatch spriteBatch;

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

    @Override
    public void create() {

        camera.cameraConfig();

        tileGrid.takeTileTextures();

        spriteBatch = new SpriteBatch();

        currentT = 1;
        tileTexture = currentT;

        ui = new EditorUi(
            id -> tileTexture = id,
            (w, h) -> {
                tileGrid.setGridSize(w, h);
                camera.centerOn(tileGrid.getWorldWidth(), tileGrid.getWorldHeight());
            }
        );
        ui.bindInput();

    }

    @Override
    public void render() {

        camera.cameraMovement(spriteBatch);

        Gdx.gl.glClearColor(0, 0, 0, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Fullscreen
        if(Gdx.input.isKeyJustPressed(Input.Keys.B)){
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            Gdx.graphics.setWindowedMode(camera.cameraW, camera.cameraH);
        }

        if (ui.isSetupDone()) {
            int st = inputHandler.selectTileSC(currentT);
            if (st != currentT) {
                currentT = st;
                ui.selectTile(st);
            }
        }

        if (ui.isSetupDone()) {
            spriteBatch.begin();
            tileGrid.renderTiles(spriteBatch);
            spriteBatch.end();
        }

        //Handling mouse clicks (ignored when the pointer is over the UI / during setup)
        boolean overUI = ui.isPointerOverUI();

        if (!overUI && Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            Vector2 p = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            camera.viewport.unproject(p);
            tileGrid.placeTile(p, tileTexture);
        }
        if (!overUI && Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
            Vector2 p = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            camera.viewport.unproject(p);
            tileGrid.removeTile(p);
        }

        ui.render();

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
        spriteBatch.dispose();

        tileGrid.disposeTextures();

        ui.dispose();
    }
}