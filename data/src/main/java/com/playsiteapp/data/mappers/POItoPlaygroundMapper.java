package com.playsiteapp.data.mappers;

import com.playsiteapp.domain.models.LatLng;
import com.playsiteapp.domain.models.Playground;

import org.osmdroid.bonuspack.location.POI;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Coco on 07/07/2017.
 */

public class POItoPlaygroundMapper {

    public POItoPlaygroundMapper() {
    }

    public Playground transform(POI osmPOI) {

        Playground playground = new Playground();

        playground.setLocationLatLng(new LatLng(osmPOI.mLocation.getLatitude(), osmPOI.mLocation.getLongitude()));

        return playground;

    }

    public List<Playground> transformList(Collection<POI> userEntityCollection) {
        final List<Playground> userList = new ArrayList<>();
        for (POI poi : userEntityCollection) {
            final Playground playground = transform(poi);
            if (playground != null) {
                userList.add(playground);
            }
        }
        return userList;
    }
}
