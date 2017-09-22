package com.playsiteapp.data.mappers;

import com.playsiteapp.domain.models.LatLng;

import org.osmdroid.bonuspack.location.POI;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Coco on 07/07/2017.
 */

public class POItoPOILatLngMapper {

    public POItoPOILatLngMapper() {
    }

    public LatLng transform(POI osmPOI) {

        return new LatLng(osmPOI.mLocation.getLatitude(), osmPOI.mLocation.getLongitude());
    }

    public List<LatLng> transformList(Collection<POI> osmPOIsCollection) {

        final List<LatLng> poisList = new ArrayList<>();
        for (POI poi : osmPOIsCollection) {
            final LatLng poiLatLng = transform(poi);
            if (poiLatLng != null) {
                poisList.add(poiLatLng);
            }
        }
        return poisList;
    }
}
