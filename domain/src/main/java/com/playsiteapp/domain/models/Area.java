package com.playsiteapp.domain.models;

/**
 * Created by Coco on 19/07/2017.
 */

public class Area {

    private LatLng northeast;
    private LatLng southwest;

    public Area() {
    }

    public Area(LatLng northeast, LatLng southwest) {
        this.northeast = northeast;
        this.southwest = southwest;
    }

    public LatLng getNortheast() {
        return northeast;
    }

    public void setNortheast(LatLng northeast) {
        this.northeast = northeast;
    }

    public LatLng getSouthwest() {
        return southwest;
    }

    public void setSouthwest(LatLng southwest) {
        this.southwest = southwest;
    }


}
