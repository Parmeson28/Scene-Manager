package com.javaProjects.game;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class TileGrid {

    private CameraHandle camera = new CameraHandle();


    private int tileSize = 32;
    private int gridWidth = 20;
    private int gridHeight = 11;
 
    private Array<Texture> tilesTextures = new Array<>();

    private BufferedReader br;

    private String folder;
    private String[] selection;
    
    private void takeTileTextures(){
        try {

            br = new BufferedReader(new FileReader("assets/assets.txt"));
            String line = br.readLine();

            while(line != null){

                folder = line.replace("/", " ");
                selection = folder.split(" ");

                if(selection[0].equals("tilesFolder")){
                    Texture t = new Texture("assets/" + line);
                    tilesTextures.add(t);
                }
                
                line = br.readLine();
            }

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    public void renderTiles(SpriteBatch spriteBatch){

        takeTileTextures();

        for(int x = (int) (0 - (camera.maxW/tileSize)); x < gridWidth; x++){
            for(int y = (int) (0 - (camera.maxH/tileSize)); y < gridHeight; y++){
                spriteBatch.draw(tilesTextures.get(0), x * tileSize, y * tileSize, tileSize, tileSize);
            }
        }
    }
    

    public void disposeTextures(){
        for(Texture t:tilesTextures){
            t.dispose();
        }
    }

}
