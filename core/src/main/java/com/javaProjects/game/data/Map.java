package com.javaProjects.game.data;

import java.util.ArrayList;
import java.util.List;

public class Map {

    public List<Structure> structures;
    public List<Waypoint> waypoints;
    public List<GroundTexture> textures;

    public Map(){
        this.structures = new ArrayList<>();
        this.waypoints = new ArrayList<>();
        this.textures = new ArrayList<>();
    }
    public Map(List<Structure> s, List<Waypoint> w, List<GroundTexture> g){
        this.structures = s;
        this.waypoints = w;
        this.textures = g;
    }

    public void saveStructures(List<Structure> s){
        this.structures = s;
    }
    public void saveStructures(Structure s){
        this.structures.add(s);
    }

    public Structure loadStructures(){
        return new Structure();
    }

    public void saveWaypoints(List<Waypoint> w){
        this.waypoints = w;
    }
    public void saveWaypoints(Waypoint w){
        this.waypoints.add(w);
    }

    public Waypoint loadWaypoints(){
        return new Waypoint();
    }

    public void saveTextures(List<GroundTexture> g){
        this.textures = g;
    }
    public void saveTextures(GroundTexture g){
        this.textures.add(g);
    } 

    public GroundTexture loadTextures(){
        return new GroundTexture();
    }
}
