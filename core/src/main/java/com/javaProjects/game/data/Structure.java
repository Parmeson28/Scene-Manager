package com.javaProjects.game.data;

public class Structure extends GenericObject{
    public String type, name;
    public float x, z;

    public Structure(){

    }

    public Structure(String type, String name, String model, float x, float z){
        this.type = type;
        this.name = name;
        this.x = x;
        this.z = z;
    }

    public Structure(String name, String type, float x, float z) {
        this.name = name;
        this.type = type;
        this.x = x;
        this.z = z;
    }

    public Structure(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
