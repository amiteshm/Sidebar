package com.fusebulb.sidebar;

import java.util.ArrayList;

/**
 * Created by amiteshmaheshwari on 22/07/16.
 */
public class ClipAction {

    public static final String MapClipAction = "map";
    public static final String MediaClipAction = "media";
    public static final String ClickClipAction = "click";

    private int id;
    private String title;
    private int startTime;
    private int endTime;
    private String actionType;

    public void setActionType(String type){
        this.actionType = type;
    }

    public String getActionType(){
        return this.actionType;
    }

    public void setId(String mId){
        this.id = Integer.parseInt(mId);
    }

    public int getId(){
        return this.id;
    }

    public void setTitle(String mName){
        this.title = mName;
    }

    public String getTitle(){
        return this.title;
    }

    public void setStartTime(String start_time){
        this.startTime = processTimeString(start_time);
    }

    public void setEndTime(String end_time){
        this.endTime = processTimeString(end_time);
    }

    public int getStartTime(){
        return this.startTime;
    }

    public int getEndTime(){
        return this.endTime;
    }

    private int processTimeString(String timeString){
        String[] timeStrings = timeString.split(":");
        //Time in milliseconds
        return (Integer.parseInt(timeStrings[0])*60+Integer.parseInt(timeStrings[1])) * 1000;
    }
}
