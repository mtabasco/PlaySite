package com.infrastructure.location;

import android.content.Context;
import android.location.Address;
import android.location.Location;

import com.patloew.rxlocation.RxLocation;
import com.playsiteapp.domain.models.LatLng;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.operators.maybe.MaybeCallbackObserver;
import io.reactivex.observers.DisposableMaybeObserver;

/**
 * Created by Coco on 26/07/2017.
 */

public class LocationService {

    RxLocation rxLocation;

    public LocationService(Context context) {

        rxLocation = new RxLocation(context);
    }

    public Maybe lastLocation() {

        return rxLocation.location().lastLocation();
    }

}
