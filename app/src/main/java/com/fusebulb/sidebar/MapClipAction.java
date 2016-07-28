package com.fusebulb.sidebar;

import android.location.Location;

/**
 * Created by amiteshmaheshwari on 22/07/16.
 */
public class MapClipAction extends ClipAction {
    private Location location;

    public void setLocation(double lat, double lon){
        this.location = new Location("manual");
        this.location.setLatitude(lat);
        this.location.setLongitude(lon);
    }

    public Location getLocation(){
        return location;
    }

    public void setLocation(String lat_string, String lon_string){
        this.location = new Location("manual");
        this.location.setLatitude(Double.parseDouble(lat_string));
        this.location.setLongitude(Double.parseDouble(lon_string));
    }

}
