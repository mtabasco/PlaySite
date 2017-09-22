package com.playsiteapp.domain.models;

/**
 * Created by Coco on 21/07/2017.
 */

public class PlaySitePOI {

    private String id;
    private String name;
    private LatLng locationLatLng;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public LatLng getLocationLatLng() {
        return locationLatLng;
    }

    public void setLocationLatLng(LatLng locationLatLng) {
        this.locationLatLng = locationLatLng;
    }

    public PlaySitePOI() {
    }

    public PlaySitePOI(String id, LatLng locationLatLng) {

        this.id = id;
        this.locationLatLng = locationLatLng;
    }
}
