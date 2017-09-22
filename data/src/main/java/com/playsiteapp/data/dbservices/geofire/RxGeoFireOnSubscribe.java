package com.playsiteapp.data.dbservices.geofire;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.playsiteapp.data.dbservices.firebase.BaseFirebaseConstants;
import com.playsiteapp.domain.models.Area;
import com.playsiteapp.domain.models.LatLng;
import com.playsiteapp.domain.util.LocationUtils;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposables;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * Created by Coco on 13/07/2017.
 */

public class RxGeoFireOnSubscribe extends BaseFirebaseConstants implements ObservableOnSubscribe<GeoFireEvent> {

    private static final double RADIUS = 1;
    private GeoFire geoFire;
    private GeoQuery geoQuery;
    private DatabaseReference playgroundLocationRef;

    private Observable<Area> location;

    public RxGeoFireOnSubscribe(Observable<Area> location) {

        this.location = location;

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        playgroundLocationRef = firebaseDatabase.getReferenceFromUrl(firebaseDatabase.getReference() + FIREBASE_CHILD_PLAYGROUND_LOCATION_KEY);
        geoFire = new GeoFire(playgroundLocationRef);
    }


    @Override
    public void subscribe(final @NonNull ObservableEmitter<GeoFireEvent> emitter) throws Exception {

        final GeoQueryEventListener geoQueryEventListener = new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                if (!emitter.isDisposed()) {
                    emitter.onNext(new GeoFireEvent(key, location, GeoFireEvent.GeofireEventType.ENTER));
                }
            }

            @Override
            public void onKeyExited(String key) {
                if (!emitter.isDisposed()) {
                    emitter.onNext(new GeoFireEvent(key, null, GeoFireEvent.GeofireEventType.EXIT));
                }
            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {
                if (!emitter.isDisposed()) {
                    emitter.onNext(new GeoFireEvent(key, location, GeoFireEvent.GeofireEventType.MOVE));
                }
            }

            @Override
            public void onGeoQueryReady() {

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {
                if (!emitter.isDisposed()) {
                    emitter.onError(error.toException());
                }
            }
        };

        location.forEach(new Consumer<Area>() {
            @Override
            public void accept(@NonNull Area area) throws Exception {

                LatLng location = LocationUtils.getCenterfromArea(area);

                if(null == geoQuery) {
                    geoQuery = geoFire.queryAtLocation(new GeoLocation(location.getLatitude(), location.getLongitude()), RADIUS);

                    geoQuery.addGeoQueryEventListener(geoQueryEventListener);
                } else {
                    geoQuery.setCenter(new GeoLocation(location.getLatitude(), location.getLongitude()));
                }
            }
        });

        emitter.setDisposable(Disposables.fromAction(new Action() {
            @Override
            public void run() throws Exception {
            geoQuery.removeGeoQueryEventListener(geoQueryEventListener);
            }
        }));
    }
}
