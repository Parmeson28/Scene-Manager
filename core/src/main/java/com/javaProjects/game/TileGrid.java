package com.javaProjects.game;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class TileGrid {

    private CameraHandle camera = new CameraHandle();


    public int tileSize = 32;
    private int gridWidth = 20;
    private int gridHeight = 11;
 
    private Array<Texture> tilesTextures = new Array<>();

    private BufferedReader br;

    private String folder;
    private String[] selection;

    private Vector2 tilesPositions = new Vector2();
    public Array<Vector2> tilesInfo = new Array<>();
    
    public Array<Vector2> selectedTiles = new Array<>();
    
    public void takeTileTextures(){
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

    public void renderTiles(SpriteBatch spriteBatch, Vector2 chosenTile, Vector2 deleteTile){
        
        if(!selectedTiles.contains(chosenTile, false)){
            selectedTiles.add(chosenTile);
        
        }else{
            for(int x = 0; x < selectedTiles.size; x++){
                if(selectedTiles.get(x).x == deleteTile.x && selectedTiles.get(x).y == deleteTile.y){
                    selectedTiles.removeIndex(x);
                    System.out.println("Removido");
                }
            }
        }

        for(int x = (int) (0 - (camera.cameraW/tileSize)); x < gridWidth; x++){
            for(int y = (int) (0 - (camera.cameraH/tileSize)); y < gridHeight; y++){
                
                tilesPositions = new Vector2((float) x * tileSize, (float) y * tileSize);
                tilesInfo.add(tilesPositions);


                if(selectedTiles.contains(tilesPositions, false)){
                    spriteBatch.draw(tilesTextures.get(1), x * tileSize, y * tileSize, tileSize, tileSize);
                }else{
                    spriteBatch.draw(tilesTextures.get(0), x * tileSize, y * tileSize, tileSize, tileSize);
                }
                
            }
        }
    
  
    }
    

    public void disposeTextures(){
        for(Texture t:tilesTextures){
            t.dispose();
        }
    }

}
