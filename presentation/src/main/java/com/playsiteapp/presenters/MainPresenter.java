package com.playsiteapp.presenters;

import android.location.Location;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.infrastructure.location.LocationService;
import com.playsiteapp.contracts.MainContract;
import com.playsiteapp.data.dbservices.PlaygroundDataRepository;
import com.playsiteapp.data.dbservices.geofire.GeoFireEvent;
import com.playsiteapp.data.dbservices.geofire.RxGeoFire;
import com.playsiteapp.domain.interactors.GetPlaygroundMarkers;
import com.playsiteapp.domain.models.Area;
import com.playsiteapp.domain.models.LatLng;
import com.playsiteapp.domain.repository.PlaygroundRepository;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.MaybeObserver;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by Coco on 02/07/2017.
 */

public class MainPresenter implements MainContract.Presenter {


    private static final float ZOOM_LEVEL = 15;

    private MainContract.MapsView mapsView;

    private GetPlaygroundMarkers getPlaygroundMarkers; // Use case for getting markers

    private Map<String, Marker> markers;

    private LocationService locationService;

    CompositeDisposable disposables;

    public MainPresenter(MainContract.MapsView view, LocationService locationService) {

        mapsView = view;
        mapsView.generateMap();

        this.locationService = locationService;

        PlaygroundRepository playgroundRepository = new PlaygroundDataRepository();
        getPlaygroundMarkers = new GetPlaygroundMarkers(playgroundRepository);

        markers = new HashMap<String, Marker>();

        disposables = new CompositeDisposable();

    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

        for (Marker marker : markers.values()) {
            marker.remove();
        }
        markers.clear();

        disposables.dispose();

    }

    @Override
    public void onMapReady() {

        disposables.add(
        RxGeoFire.geoFireEvents(observeCameraListener(mapsView.getMainMap()))
                .subscribe(new Consumer<GeoFireEvent>() {

                    @Override
                    public void accept(@NonNull GeoFireEvent geoFireEvent) throws Exception {

                        // Event ENTER
                        if (geoFireEvent.getEventType().equals(GeoFireEvent.GeofireEventType.ENTER)) {

                            if (!markers.containsKey(geoFireEvent.getKey())) {
                                // Añadir marker geofire

                                Marker marker = mapsView.addMarker(geoFireEvent.getLocation().latitude, geoFireEvent.getLocation().longitude);

                                // añadir al map de markers geofire
                                markers.put(geoFireEvent.getKey(), marker);

                            }
                        } // Event EXIT
                        else if (geoFireEvent.getEventType().equals(GeoFireEvent.GeofireEventType.EXIT)) {

                            Marker marker = markers.get(geoFireEvent.getKey());
                            if (marker != null) {
                                marker.remove();
                                markers.remove(geoFireEvent.getKey());
                            }
                        }
                    }
                }));


    }

    @Override
    public void locateUserFromSearch(double latitude, double longitude) {

        // TODO
        mapsView.updateLocationOnMap(latitude, longitude);
    }

    @Override
    public void autoLocateUser() {

        locationService.lastLocation()
                .subscribe(new MaybeObserver<Location>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull Location location) {
                        mapsView.updateLocationOnMap(location.getLatitude(), location.getLongitude());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    public Observable observeCameraListener(final GoogleMap googleMap) {

        return Observable.create(new ObservableOnSubscribe() {
            @Override
            public void subscribe(@NonNull final ObservableEmitter emmiter) throws Exception {

                googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                    @Override
                    public void onCameraIdle() {

                    LatLng northeast = new LatLng(googleMap.getProjection().getVisibleRegion().latLngBounds.northeast.latitude,
                                                googleMap.getProjection().getVisibleRegion().latLngBounds.northeast.longitude);
                    LatLng southwest = new LatLng(googleMap.getProjection().getVisibleRegion().latLngBounds.southwest.latitude,
                                googleMap.getProjection().getVisibleRegion().latLngBounds.southwest.longitude);


                    Area area = new Area(northeast, southwest);

                    getPlaygroundMarkers.importPlaygrounds(area);

                    emmiter.onNext(area);
                    }
                });
            }
        });
    }

}
