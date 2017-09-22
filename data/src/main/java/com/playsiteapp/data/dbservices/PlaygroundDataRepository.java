package com.playsiteapp.data.dbservices;

import android.app.Activity;

import com.google.firebase.database.DatabaseException;
import com.patloew.rxlocation.RxLocation;
import com.playsiteapp.data.dbservices.firebase.FirebaseDataSource;
import com.playsiteapp.data.dbservices.geofire.GeoFireEvent;
import com.playsiteapp.data.dbservices.geofire.RxGeoFire;
import com.playsiteapp.data.dbservices.osm.OSMDataSource;
import com.playsiteapp.data.mappers.POItoPOILatLngMapper;
import com.playsiteapp.domain.models.Area;
import com.playsiteapp.domain.models.Business;
import com.playsiteapp.domain.models.LatLng;
import com.playsiteapp.domain.models.PlaySitePOI;
import com.playsiteapp.domain.models.Playground;
import com.playsiteapp.domain.repository.PlaygroundRepository;

import org.osmdroid.bonuspack.location.POI;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by Coco on 07/07/2017.
 */

public class PlaygroundDataRepository implements PlaygroundRepository {


    @Override
    public Observable<List<LatLng>> importPOIsFromExternalSource(Area area) {

        final POItoPOILatLngMapper poItoPOILatLngMapper = new POItoPOILatLngMapper();

        OSMDataSource osmDataSource = new OSMDataSource();

        return osmDataSource.getOSMPOIs(area.getNortheast().getLatitude(), area.getNortheast().getLongitude(),
                area.getSouthwest().getLatitude(), area.getSouthwest().getLongitude())
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        Exceptions.propagate(throwable);
                    }
                })
                .map(new Function<List<POI>, List<LatLng>>() {
            @Override
            public List<LatLng> apply(@NonNull List<POI> pois) throws Exception {

                    return poItoPOILatLngMapper.transformList(pois);

            }
        });
    }

    @Override
    public Observable getPlaySitePOIs(Observable<Area> area) {

        return RxGeoFire.geoFireEvents(area);
    }

    @Override
    public Single<String> saveLocation(LatLng poiLatLng) {

        return RxGeoFire.setGeofireLocation(poiLatLng.getLatitude(), poiLatLng.getLongitude());
    }

    @Override
    public void savePlaySitePOIInfo(PlaySitePOI playSitePOI) {

        FirebaseDataSource.setValue(playSitePOI.getId(), playSitePOI);
    }
}
