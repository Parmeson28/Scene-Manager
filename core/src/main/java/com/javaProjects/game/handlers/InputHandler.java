package com.javaProjects.game.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

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
    
}
