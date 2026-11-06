package com.javaProjects.game.data;

public class SaveAndLoadMap {

    private String savesPath = "assets/saves/";
    private Map map;
    

    public SaveAndLoadMap(){
        this.map = new Map();
    }

    public void writeNewSave(){

    }

    public void saveToExistingFile(){

    }

    public Map loadSave(String save){
        savesPath = savesPath + save;



        return new Map();
    }
}   
