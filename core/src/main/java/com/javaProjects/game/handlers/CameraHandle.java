package com.javaProjects.game.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class CameraHandle{

    public OrthographicCamera camera;
    public int cameraW = 1920, cameraH = 1080;
    public float cameraSpd;

    public Viewport viewport;

    public void cameraConfig(){

        camera = new OrthographicCamera();

        
        cameraSpd = 5f * camera.zoom;

        viewport = new FitViewport(cameraW, cameraH, camera);
    }

    public void centerOn(float worldW, float worldH) {
        camera.position.set(worldW / 2f, worldH / 2f, 0f);
        camera.update();
    }

    public void cameraMovement(SpriteBatch spriteBatch){

        spriteBatch.setProjectionMatrix(camera.combined);

        //Camera Movement
        if(Gdx.input.isKeyPressed(Input.Keys.W)) camera.position.y += cameraSpd;
        if(Gdx.input.isKeyPressed(Input.Keys.S)) camera.position.y -= cameraSpd;
        if(Gdx.input.isKeyPressed(Input.Keys.A)) camera.position.x -= cameraSpd;
        if(Gdx.input.isKeyPressed(Input.Keys.D)) camera.position.x += cameraSpd;

        camera.update();
    }

    public void zoom(){
        System.out.println(camera.zoom);
        if(Gdx.input.isKeyPressed(Keys.NUM_9) && camera.zoom > 0.1){
			camera.zoom -= .05f;
		}
        if(Gdx.input.isKeyPressed(Keys.NUM_0)){
			camera.zoom += .05f;
		}
    }

}