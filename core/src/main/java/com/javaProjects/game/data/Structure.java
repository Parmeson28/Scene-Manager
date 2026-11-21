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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override 
    public String getName() {
        return name;
    }

    @Override 
    public void setName(String name) {
        this.name = name;
    }

    @Override 
    public float getX() {
        return x;
    }

    @Override 
    public void setX(float x) {
        this.x = x;
    }

    @Override 
    public float getZ() {
        return z;
    }

    @Override 
    public void setZ(float z) {
        this.z = z;
    }
}
