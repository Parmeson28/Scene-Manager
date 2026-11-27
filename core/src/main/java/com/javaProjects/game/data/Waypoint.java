package com.javaProjects.game.data;

public class Waypoint extends GenericObject{
    public String name;

    public Waypoint(){

    }

    public Waypoint(String name, float x, float z) {
        this.name = name;
        this.x = x;
        this.z = z;
    }

    public Waypoint(String name) {
        this.name = name;
    }

}
