package com.javaProjects.game.data;

public class Waypoint extends GenericObject{
    public String name;
    public float x, z;

    public Waypoint(){

    }

    public Waypoint(String name, float x, float z) {
        this.name = name;
        this.x = x;
        this.z = z;
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
