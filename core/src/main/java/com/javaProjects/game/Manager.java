package com.javaProjects.game;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Manager extends ApplicationAdapter {

    private CameraHandle camera = new CameraHandle();

    private TileGrid tileGrid = new TileGrid();

    public Sprite catSprite;
    public Texture catTexture;


    //Exclusive variables for the level editore

    private BufferedReader br;

    private Texture defaultTexture;

    private Array<Texture> textures = new Array<>();

    private SpriteBatch spriteBatch;


    @Override
    public void create() {

        camera.cameraConfig();
        
    }

    @Override
    public void render() {
        
        camera.cameraMovement(spriteBatch);

        //Fullscreen
        if(Gdx.input.isKeyJustPressed(Input.Keys.B)){
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        }



        spriteBatch.begin();

        
        tileGrid.renderTiles(spriteBatch);
        

        spriteBatch.end();


    }

    @Override
    public void resize(int width, int height) {
        if (camera.viewport != null) {
            camera.viewport.update(width, height, false);
        }
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();

        tileGrid.disposeTextures();
    }
}