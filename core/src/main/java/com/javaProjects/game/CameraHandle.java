package com.javaProjects.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class CameraHandle {

    public OrthographicCamera camera;
    public int cameraW = 1920, cameraH = 1080;
    public int cameraSpd = 5;

    public int maxW = 1920, maxH = 1080;

    Viewport viewport;


    //Put this inside Create()
    public void cameraConfig(){ 

        camera = new OrthographicCamera();  

        viewport = new FitViewport(cameraW, cameraH, camera);

        camera.position.set(cameraW/2f, cameraH/2f, 0f);
        camera.update();
    }

    //Put this inside Render()
    public void cameraMovement(SpriteBatch spriteBatch){

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
    
        
        camera.update();
    }

    
    
}
