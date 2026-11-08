package com.javaProjects.game.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

public class SaveAndLoadMap {

    private final String savesPath = "assets/saves/";
    private final Json json = new Json();
    private GameMap map;
    
    public SaveAndLoadMap(){
        this.map = new GameMap();
        json.setOutputType(JsonWriter.OutputType.json); 
        json.setIgnoreUnknownFields(true); 
    }

    public void writeNewSave(String fileName){
        FileHandle file = Gdx.files.local(savesPath + fileName + ".json");
        
        String text = 
        "{\n" +
            "\t\"groundTexture:\",\n" +

            "\t\"sizeX:\" 0" +
            
            "\t\"sizeZ:\" 0" +

            "\t\"structures\":[\n" +
            "\t],\n"+

            "\t\"characters\":[\n" +
            "\t],\n"+

            "\t\"waypoints\":[\n" +
            "\t]\n"+
        "}";
        file.writeString(text, false);
    }

    public void saveToExistingFile(GameMap map, String fileName) {
        FileHandle file = Gdx.files.local(savesPath + fileName + ".json");
        file.writeString(json.prettyPrint(json.toJson(map, GameMap.class)), false);
    }

    public GameMap loadSave(String save){
        String saveFile = savesPath + save;



        return new GameMap();
    }
}   
