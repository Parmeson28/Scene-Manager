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
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;


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
    public Vector2 selectedTile = new Vector2();


    //Changing texture variables
    public Rectangle tileRec = new Rectangle();


    
    //UI Variables
    private Skin skin;
    private Stage stage;



    @Override
    public void create() {

        camera.cameraConfig();

        tileGrid.takeTileTextures();

        spriteBatch = new SpriteBatch();
        

        spriteBatch.begin();

        
        tileGrid.renderTiles(spriteBatch, selectedTile);
        

        spriteBatch.end();


        //UI CODE
        skin = new Skin(Gdx.files.internal("assets/metalui/metal-ui.json"));

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        TextButton textButton = new TextButton("Hello", skin);
        textButton.setPosition(stage.getWidth()/2, stage.getHeight()/2, Align.center);
        
        root.add(textButton);

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


        //Handling mouse clicks (getting the point where it is clicked)
        if(Gdx.input.isButtonJustPressed(0)){
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.camera.unproject(touchPos);
            
            touchPoint = new Vector2(touchPos.x, touchPos.y);
        }   

        for(Vector2 v:tileGrid.tilesInfo){
            tileRec.set(v.x, v.y, 32, 32);
            
            if(tileRec.contains(touchPoint)){
                selectedTile = new Vector2(v.x, v.y);
                
            }
        }

        tileGrid.tilesInfo.clear();


        spriteBatch.begin();

        
        tileGrid.renderTiles(spriteBatch, selectedTile);
        

        spriteBatch.end();

        stage.act();
        stage.draw();


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