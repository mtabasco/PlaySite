package com.playsiteapp.domain.util;

import com.playsiteapp.domain.models.Area;
import com.playsiteapp.domain.models.LatLng;

/**
 * Created by Coco on 19/07/2017.
 */

public class LocationUtils {

    public static LatLng getCenterfromArea(Area area) {

        double latitude = (area.getNortheast().getLatitude()/2) + (area.getSouthwest().getLatitude()/2);
        double longitude = (area.getNortheast().getLongitude()/2) + (area.getSouthwest().getLongitude()/2);

        return new LatLng(latitude, longitude);
    }
}
