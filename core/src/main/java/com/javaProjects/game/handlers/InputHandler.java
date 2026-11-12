package com.javaProjects.game.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import com.javaProjects.game.TileGrid;
import com.javaProjects.game.ui.EditorUi;

public class InputHandler {

    public void InputHandler(){

    }

    public int selectTileSC(int currentT){
        int v = currentT;
        if(Gdx.input.isKeyJustPressed(Keys.NUM_1)){
            v = 1;
        }
        if(Gdx.input.isKeyJustPressed(Keys.NUM_2)){
            v = 2;
        }
        return v;
    }
    
    public void fullScreenMode(CameraHandle camera){
        if(Gdx.input.isKeyJustPressed(Input.Keys.B)){
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            Gdx.graphics.setWindowedMode(camera.cameraW, camera.cameraH);
        }
    }

    public void rightClickOptions(CameraHandle camera, EditorUi ui, TileGrid tileGrid){
        if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
            
            Vector2 p = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            camera.viewport.unproject(p);

            ui.openObjectMenu(type -> {
                switch(type){
                    case "structure":
                        ui.openForm("Structure info", new String[]{"name", "model"},
                            v -> tileGrid.placeObjectTile(p, 0));
                        break;
                    case "waypoint":
                        ui.openForm("Waypoint info", new String[]{"waypoint name"},
                            v -> tileGrid.placeObjectTile(p, 1));
                        break;
                }
            });
        }
    }

    public void placeTiles(CameraHandle camera, TileGrid tileGrid, int tileTexture, boolean overUI){
        if (!overUI && Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            Vector2 p = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            camera.viewport.unproject(p);
            tileGrid.placeTile(p, tileTexture);
        }
    }
}