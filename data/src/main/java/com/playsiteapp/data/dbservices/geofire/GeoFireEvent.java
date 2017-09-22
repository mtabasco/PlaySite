package com.playsiteapp.data.dbservices.geofire;

import com.firebase.geofire.GeoLocation;

/**
 * Created by Coco on 13/07/2017.
 */

public class GeoFireEvent {

    private String key;
    private GeoLocation location;
    private GeofireEventType eventType;

    public GeoFireEvent(String key, GeoLocation location, GeofireEventType eventType) {
        this.key = key;
        this.location = location;
        this.eventType = eventType;
    }

    public String getKey() {
        return key;
    }

    public GeoLocation getLocation() {
        return location;
    }

    public GeofireEventType getEventType() {
        return eventType;
    }



    public enum GeofireEventType {
        ENTER, EXIT, MOVE
    }

}
