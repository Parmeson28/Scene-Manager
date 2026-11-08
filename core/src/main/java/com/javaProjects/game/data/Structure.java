package com.javaProjects.game.data;

public class Structure {
    public String type, name, model;
    public float x, z;

    public Structure(){

    }

    public Structure(String type, String name, String model, float x, float z){
        this.type = type;
        this.name = name;
        this.model = model;
        this.x = x;
        this.z = z;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
