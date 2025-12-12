package com.javaProjects.game;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

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
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
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


    //Handling mouse click variables
    private Vector2 touchPoint = new Vector2();


    //Changing texture variables
    public Rectangle tileRec = new Rectangle();


    @Override
    public void create() {

        camera.cameraConfig();

        tileGrid.takeTileTextures();

        spriteBatch = new SpriteBatch();
        

        spriteBatch.begin();

        
        tileGrid.renderTiles(spriteBatch);
        

        spriteBatch.end();
    }

    @Override
    public void render() {
        
        camera.cameraMovement(spriteBatch);

        //Fullscreen
        if(Gdx.input.isKeyJustPressed(Input.Keys.B)){
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            Gdx.graphics.setWindowedMode(camera.cameraW, camera.cameraH);
        }


        //Handling mouse clicks (getting the point where it is clicked)
        if(Gdx.input.isButtonJustPressed(0))    touchPoint = new Vector2(Gdx.input.getX(), Gdx.input.getY());

        for(Vector2 v:tileGrid.tilesInfo){
            tileRec.set(v.x, v.y, 32, 32);
            
            if(tileRec.contains(touchPoint)){
                System.out.println("tp " + touchPoint);
                System.out.println("x, v" + v.x + v.y); 
            }

        }

        tileGrid.tilesInfo.clear();


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