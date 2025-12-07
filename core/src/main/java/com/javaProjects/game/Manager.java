package com.javaProjects.game;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;


/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Manager extends ApplicationAdapter {

    public OrthographicCamera camera;

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
    private int gridHeight = 10;
 
    private Texture tileTexture;

    @Override
    public void create() {

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

        defaultTexture = new Texture(Gdx.files.internal("assets/white.jpg"));
        tileTexture = textures.get(0);

        spriteBatch = new SpriteBatch();

        catSprite = new Sprite(defaultTexture);

        catSprite.setPosition(0f, 0f);
        catSprite.setSize(50f, 50f);

        camera = new OrthographicCamera(900, 720);

        camera.position.set(-10f, -10f, 0f);
        camera.update();

    }

    @Override
    public void render() {
        camera.update();
        spriteBatch.setProjectionMatrix(camera.combined);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Changes the texture from the sprite (improve later)
        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)){
            catSprite.setTexture(textures.get(1));
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)){
            catSprite.setTexture(textures.get(2));
        }

        spriteBatch.begin();

        for(int x = 0; x < gridWidth; x++){
            for(int y = 0; y < gridHeight; y++){
                spriteBatch.draw(textures.get(0), x * tileSize, y * tileSize, tileSize, tileSize);
            }
        }
        
        
        catSprite.draw(spriteBatch);

        spriteBatch.end();
    }

    @Override
    public void resize(int width, int height) {
       
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();

        for(Texture i:textures){
            i.dispose();
        }
    }
}