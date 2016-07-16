package com.fusebulb.sidebar;


/**
 * Created by amiteshmaheshwari on 09/07/16.
 */
public class Clip {
    private int id;
    private String name;
    private int tour;
    private int order;
    private String source;

    public void setId(String id){
        this.id = Integer.parseInt(id);
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public void setOrder(String order){
        this.order = Integer.parseInt(order);
    }

    public int getOrder(){
        return this.order;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return  this.name;
    }


    public void setTourId(int tour){
        this.tour = tour;
    }

    public int getTourId(){
        return this.tour;
    }

    public void setSource( String source){
        this.source = source;
    }

    public String getSource(){
        return this.source;
    }

}
