package com.playsiteapp.data.dbservices.geofire;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.LocationCallback;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.playsiteapp.data.dbservices.firebase.BaseFirebaseConstants;
import com.playsiteapp.domain.models.Area;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.annotations.NonNull;

/**
 * Created by Coco on 13/07/2017.
 */

public class RxGeoFire extends BaseFirebaseConstants {


    public static Observable<GeoFireEvent> geoFireEvents(Observable<Area> area) {

        return Observable.create(new RxGeoFireOnSubscribe(area));
    }

    public static Single<String> setGeofireLocation(final double longitude, final double latitude) {

        return Single.create(new SingleOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull final SingleEmitter<String> emitter) throws Exception {

                DatabaseReference playgroundLocationRef = FirebaseDatabase.getInstance().getReferenceFromUrl(FirebaseDatabase.getInstance().getReference() + FIREBASE_CHILD_PLAYGROUND_LOCATION_KEY);

                String key = String.valueOf(latitude).replace(".", "").concat(String.valueOf(longitude).replace(".", ""));
                final GeoFire geoFire = new GeoFire(playgroundLocationRef);

                geoFire.getLocation(key, new LocationCallback() {
                    @Override
                    public void onLocationResult(String key, GeoLocation location) {

                        if (null == location) { // Key not found

                            geoFire.setLocation(key, new GeoLocation(longitude, latitude), new GeoFire.CompletionListener() {
                                @Override
                                public void onComplete(String key, DatabaseError error) {
                                    if (error != null) {
                                        emitter.onError(error.toException());
                                    } else {
                                        emitter.onSuccess(key);
                                    }
                                }
                            });

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        emitter.onError(databaseError.toException());
                    }
                });
            }
        });
    }
}


