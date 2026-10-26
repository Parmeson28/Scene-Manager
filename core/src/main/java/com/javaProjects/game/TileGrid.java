package com.javaProjects.game;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class TileGrid {

    private final CameraHandle camera = new CameraHandle();


    public  final int tileSize = 32;
    private final int gridWidth = 32;
    private final int gridHeight = 32;
 
    private Array<Texture> tilesTextures = new Array<>();

    private BufferedReader br;

    private String folder;
    private String[] selection;

    private HashMap<Vector2, Integer> placedTiles = new HashMap<>();
    private final Vector2 tmp = new Vector2();
    
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

    private Vector2 snap(Vector2 world){
        return new Vector2(
            MathUtils.floor(world.x / tileSize) * tileSize,
            MathUtils.floor(world.y / tileSize) * tileSize
        );
    }

    public void placeTile(Vector2 world, int textureIndex){
        placedTiles.put(snap(world), textureIndex);
    }

    public void removeTile(Vector2 world) {
        placedTiles.remove(snap(world));
    }

    public void renderTiles(SpriteBatch spriteBatch) {
        for (int x = -(camera.cameraW / tileSize); x < gridWidth; x++) {
            for (int y = -(camera.cameraH / tileSize); y < gridHeight; y++) {
                tmp.set(x * tileSize, y * tileSize);
                Integer idx = placedTiles.get(tmp);
                Texture t = tilesTextures.get(idx != null ? idx : 0);
                spriteBatch.draw(t, x * tileSize, y * tileSize, tileSize, tileSize);
            }
        }
    }
    
    public void disposeTextures(){
        for(Texture t:tilesTextures){
            t.dispose();
        }
    }

}
