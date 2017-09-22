package com.playsiteapp.contracts;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.playsiteapp.common.BasePresenter;
import com.playsiteapp.common.BaseView;

/**
 * Created by Coco on 02/07/2017.
 */

public interface MainContract {

    interface MapsView extends BaseView<Presenter>{

        Marker addMarker(double latitude, double longitude);

        void generateMap();

        GoogleMap getMainMap();

        void updateLocationOnMap(double latitude, double longitude);
    }

    interface Presenter extends BasePresenter {

        void onMapReady();

        void locateUserFromSearch(double latitude, double longitude);

        void autoLocateUser();

    }
}