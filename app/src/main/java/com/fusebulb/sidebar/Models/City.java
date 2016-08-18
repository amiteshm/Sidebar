package com.fusebulb.sidebar.Models;

import android.location.Location;

import java.util.ArrayList;
import java.io.File;

/**
 * Created by amiteshmaheshwari on 07/07/16.
 */
public class City {
    private int id;
    private String name;
    private Location location;
    private File picture;
    private int tour_size;
    private String tours_source;


    public City() {

    }

//    public City(int id, String name, double lat, double lon, String photo_path) {
//        this.id = id;
//        this.name = name;
//        this.location = new Location("manual");
//        this.location.setLatitude(lat);
//        this.location.setLongitude(lon);
//        this.picture = photo_path;
//    }

    public void setTourSize(int size){
        tour_size = size;
    }

    public int getTourSize(){
        return this.tour_size;
    }

    public void setCityTours(String file_path){
        tours_source = file_path;
    }

    public String getCityTours(){
        return tours_source;
    }

    public void setId(String id){

        this.id = Integer.parseInt(id);
    }

    public void setId(int id){

        this.id = id;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setLocation(double lat, double lon){
        this.location = new Location("manual");
        this.location.setLatitude(lat);
        this.location.setLongitude(lon);
    }

    public void setLocation(String lat_string, String lon_string){
        this.location = new Location("manual");
        this.location.setLatitude(Double.parseDouble(lat_string));
        this.location.setLongitude(Double.parseDouble(lon_string));
    }

    public void setPicture(File photo_file) {
        this.picture = photo_file;
    }

    public Integer getId(){
        return this.id;
    }

    public Location getLocation(){
        return this.location;
    }

    public File getPicture(){
        return this.picture;
    }

    public String getName(){
        return this.name;
    }
    //FeaturedPhoto
}
