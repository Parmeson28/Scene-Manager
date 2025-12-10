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

    public OrthographicCamera camera;
    public int cameraW, cameraH;
    public int cameraSpd = 5;

    public int maxW = 1920, maxH = 1080;

    public Sprite catSprite;
    public Texture catTexture;

    Vector3 touchPoint = new Vector3();

    //Exclusive variables for the level editore

    private BufferedReader br;

    private Texture defaultTexture;

    private Array<Texture> textures = new Array<>();

    private SpriteBatch spriteBatch;

    //Tile variables
    private int tileSize = 32;
    private int gridWidth = 20;
    private int gridHeight = 11;
 
    private Texture tileTexture;

    Viewport viewport;

    @Override
    public void create() {


        //Configuring camera and viewport for the application
        cameraW = 1920;
        cameraH = 1080;       

        camera = new OrthographicCamera();  

        viewport = new FitViewport(cameraW, cameraH, camera);

        camera.position.set(cameraW/2f, cameraH/2f, 0f);
        camera.update();


        //Putting all the textures/sprites inside of the textures array
        try {

            br = new BufferedReader(new FileReader("assets/assets.txt"));
            String line = br.readLine();

            while(line != null){
                Texture t = new Texture("assets/" + line);
                textures.add(t);

                line = br.readLine();
            }

        } catch (IOException e) {

            e.printStackTrace();

        }

        System.out.println(textures);

        //Defining some textures for testing
        defaultTexture = new Texture(Gdx.files.internal("assets/white.jpg"));
        tileTexture = textures.get(0);

        spriteBatch = new SpriteBatch();

        catSprite = new Sprite(defaultTexture);

        catSprite.setPosition(0f, 0f);
        
        catSprite.setSize(50f, 50f);

    }

    @Override
    public void render() {
        
        spriteBatch.setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor(0, 0, 0, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Camera Movement
        if(Gdx.input.isKeyPressed(Input.Keys.W)) camera.position.y += cameraSpd;
        if(Gdx.input.isKeyPressed(Input.Keys.S)) camera.position.y -= cameraSpd;
        if(Gdx.input.isKeyPressed(Input.Keys.A)) camera.position.x -= cameraSpd;
        if(Gdx.input.isKeyPressed(Input.Keys.D)) camera.position.x += cameraSpd;

        //Limiting the camera movement
        if(camera.position.y + (cameraH * 1.15) > cameraH) camera.position.y = camera.position.y - cameraSpd;

        if(camera.position.y + (cameraH/2) < 0) camera.position.y = camera.position.y + cameraSpd;

        if(camera.position.x + (cameraW * 1.15) > cameraW)  camera.position.x = camera.position.x - cameraSpd;
        
        if(camera.position.x - (cameraW * 0.47) < -cameraW) camera.position.x = camera.position.x + cameraSpd;


        //Fullscreen
        if(Gdx.input.isKeyJustPressed(Input.Keys.B)){
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        }

        //Changes the texture from the sprite (improve later)
        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)){
            catSprite.setTexture(textures.get(1));
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)){
            catSprite.setTexture(textures.get(2));
        }

        spriteBatch.begin();

        for(int x = (int) (0 - (maxW/tileSize)); x < gridWidth; x++){
            for(int y = (int) (0 - (maxH/tileSize)); y < gridHeight; y++){
                spriteBatch.draw(textures.get(0), x * tileSize, y * tileSize, tileSize, tileSize);
            }
        }
                
        
        catSprite.draw(spriteBatch);

        spriteBatch.end();

        camera.update();

    }

    @Override
    public void resize(int width, int height) {
        if (viewport != null) {
            viewport.update(width, height, false);
        }

        camera.position.set(cameraW/2f, cameraH/2f, 0f);
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();

        for(Texture i:textures){
            i.dispose();
        }
    }
}