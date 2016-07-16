package com.fusebulb.sidebar;


import android.location.Location;
import java.io.File;

/**
 * Created by amiteshmaheshwari on 09/07/16.
 */
public class Tour {
    private int id;
    private String name;
    private Location location;
    private File picture;
    private String summary;
    private int city;
    private int price;
    private String clip_source;
    private int tour_type;

    private static final int ATTRACTION =100;
    private static final int CITY_TOUR =200;

    public Tour() {

    }

//    public Tour(int id, String name, double lat, double lon, String photo_path, String summary, int location_type ) {
//        this.id = id;
//        this.name = name;
//        this.location = new Location("manual");
//        this.location.setLatitude(lat);
//        this.location.setLongitude(lon);
//        this.picture = photo_path;
//        this.summary = summary;
//        this.tour_type = tour_type;
//    }

    public void setId(String id){

        this.id = Integer.parseInt(id);
    }

    public void setPrice(int rate){
        this.price = rate;
    }

    public int getPrice(){
        return this.price;
    }

    public void setClipSource(String clipSource){
        this.clip_source = clipSource;
    }

    public String getClipSource(){
        return this.clip_source;
    }

    public void setId(int id){

        this.id = id;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setCity(int city){
        this.city = city;
    }

    public int getCity(){
        return this.city;
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

    public void setSummary(String summary){
        this.summary = summary;
    }


    public void setTourType(String tourType){
        this.tour_type = Integer.parseInt(tourType);
    }

    public void setTourType(int tourType){
        this.tour_type = tourType;
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

    public String getSummary(){
        return this.summary;
    }

    public int getTourType(){
        return this.tour_type;
    }

}
