package com.fusebulb.sidebar;


import android.location.Location;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by amiteshmaheshwari on 09/07/16.
 */
public class Clip {
    private int id;
    private String name;
    private int tour;
    private int order;
    private String pictureSource;
    private String clipSource;
    private Location location;
    private File clipFile;
    private String actionFileSource;
    private File actionFile;
    private ArrayList<ClipAction> clipActions;

    public Clip() {
        clipActions = new ArrayList<ClipAction>();
    }

    public void setClipActionFile(File file) {
        this.actionFile = file;
    }

    public File getClipActionFile(){
        return this.actionFile;
    }

    public String getActionFileSource(){
        return this.actionFileSource;
    }

    public void setActionFileSource(String source){
        this.actionFileSource = source;
    }

    public void setClipFile(File file) {
        this.clipFile = file;
    }

    public File getClipFile() {
        return this.clipFile;
    }

    public void setId(String id) {
        this.id = Integer.parseInt(id);
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setOrder(String order) {
        this.order = Integer.parseInt(order);
    }

    public int getOrder() {
        return this.order;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(String lat_string, String lon_string) {
        this.location = new Location("manual");
        this.location.setLatitude(Double.parseDouble(lat_string));
        this.location.setLongitude(Double.parseDouble(lon_string));
    }


    public void setTourId(int tour) {
        this.tour = tour;
    }

    public int getTourId() {
        return this.tour;
    }

    public void setClipSource(String source) {
        this.clipSource = source;
    }

    public String getClipSource() {
        return this.clipSource;
    }

    public void setClipActions(ArrayList<ClipAction> clipActionsList) {
        this.clipActions = clipActionsList;
    }

    public ArrayList<ClipAction> getClipActions() {
        return this.clipActions;
    }

    public String getPictureSource() {
        return this.pictureSource;
    }

    public void setPictureSource(String picture_source) {
        this.pictureSource = picture_source;
    }

}
