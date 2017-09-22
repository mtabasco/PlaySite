package com.playsiteapp.domain.repository;

import com.playsiteapp.domain.models.Area;
import com.playsiteapp.domain.models.LatLng;
import com.playsiteapp.domain.models.PlaySitePOI;
import com.playsiteapp.domain.models.Playground;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;


/**
 * Created by Coco on 07/07/2017.
 */

public interface PlaygroundRepository {

    Observable<List<LatLng>> importPOIsFromExternalSource(Area area);


    Observable getPlaySitePOIs(Observable<Area> area);

    Single<String> saveLocation(LatLng poiLatLng);

    void savePlaySitePOIInfo(PlaySitePOI playSitePOI);
}
