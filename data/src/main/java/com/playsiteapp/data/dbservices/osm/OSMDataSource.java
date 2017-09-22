package com.playsiteapp.data.dbservices.osm;

import com.playsiteapp.data.exception.OSMPOIRequestException;

import org.osmdroid.bonuspack.location.OverpassAPIProvider;
import org.osmdroid.bonuspack.location.POI;
import org.osmdroid.util.BoundingBox;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.internal.operators.observable.ObservableFromCallable;

/**
 * Created by Coco on 07/07/2017.
 */

public class OSMDataSource {

    public OSMDataSource() {
    }

    public Observable<List<POI>> getOSMPOIs(final Double northeastLatitude, final Double northeastLongitude,
                                            final Double southwestLatitude, final Double southwestLongitude) {

        return new ObservableFromCallable(new Callable() {
            @Override
            public Object call() throws Exception {

                BoundingBox boundingbox = new BoundingBox(northeastLatitude, northeastLongitude,
                        southwestLatitude, southwestLongitude);

                OverpassAPIProvider overpassProvider = new OverpassAPIProvider();

                String oUrl = overpassProvider.urlForPOISearch("leisure=playground", boundingbox, 50, 60);
                ArrayList<POI> pois = overpassProvider.getPOIsFromUrl(oUrl);

                // OSM returns null when timeout

                if(null == pois) {
                    throw new OSMPOIRequestException();
                } else {
                    return pois;
                }
            }
        });
    }
}
