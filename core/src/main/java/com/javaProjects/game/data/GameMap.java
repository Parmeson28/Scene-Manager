package com.javaProjects.game.data;

import java.util.ArrayList;
import java.util.List;

public class GameMap {

    public List<Structure> structures;
    public List<Waypoint> waypoints;
    public String texture;
    public float sizeX, sizeZ;

    public GameMap(){
        this.structures = new ArrayList<>();
        this.waypoints = new ArrayList<>();
    }
    public GameMap(List<Structure> s, List<Waypoint> w, String g){
        this.structures = s;
        this.waypoints = w;
        this.texture = g;
    }

    public void saveStructures(List<Structure> s){
        this.structures = s;
    }
    public void saveStructures(Structure s){
        this.structures.add(s);
    }

    public Structure loadStructures(){
        for(Structure s : structures){
            return s;
        }
        return null;
    }

    public void saveWaypoints(List<Waypoint> w){
        this.waypoints = w;
    }
    public void saveWaypoints(Waypoint w){
        this.waypoints.add(w);
    }

    public Waypoint loadWaypoints(){
        for (Waypoint w : waypoints) {
            return w;
        }
        return null;
    }

    public String getTexture() {
        return texture;
    }
    public void setTexture(String texture) {
        this.texture = texture;
    }

    public List<Structure> getStructures() {
        return structures;
    }
    public List<Waypoint> getWaypoints() {
        return waypoints;
    }

    public float getSizeX() {
        return sizeX;
    }
    public void setSizeX(float sizeX) {
        this.sizeX = sizeX;
    }

    public float getSizeZ() {
        return sizeZ;
    }
    public void setSizeZ(float sizeZ) {
        this.sizeZ = sizeZ;
    }
}
