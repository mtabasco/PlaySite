package com.playsiteapp.domain.interactors;

import com.playsiteapp.domain.models.Area;
import com.playsiteapp.domain.models.LatLng;
import com.playsiteapp.domain.models.Playground;
import com.playsiteapp.domain.repository.PlaygroundRepository;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Created by Coco on 07/07/2017.
 */

public class GetPlaygroundMarkers {

    private PlaygroundRepository playgroundRepository;

    private final static Logger LOGGER = Logger.getLogger(GetPlaygroundMarkers.class.getName());

    public GetPlaygroundMarkers(PlaygroundRepository playgroundRepository) {
        this.playgroundRepository = playgroundRepository;
    }

    public void importPlaygrounds(Area area) {
/*
        playgroundRepository.importOSMMarkers(area)
                .subscribeOn(Schedulers.io())
                .flatMapIterable(new Function<List<LatLng>, Iterable<Playground>>() {
                    @Override
                    public Iterable<Playground> apply(@NonNull List<LatLng> latLngs) throws Exception {

                        final List<Playground> playgrounds = new ArrayList<Playground>();

                        for (final LatLng latLng : latLngs) {

                            playgroundRepository.saveLocation(latLng)
                                    .subscribe(new SingleObserver<String>() {
                                        @Override
                                        public void onSubscribe(@NonNull Disposable d) {

                                        }

                                        @Override
                                        public void onSuccess(@NonNull String key) {

                                            Playground playground = new Playground(key, latLng);
                                            playgrounds.add(playground);
                                        }

                                        @Override
                                        public void onError(@NonNull Throwable e) {

                                        }
                                    });
                        }
                        return playgrounds;
                    }
                })
                .subscribe(new Consumer<Playground>() {
                    @Override
                    public void accept(@NonNull Playground playground) throws Exception {

                        playgroundRepository.savePlaySitePOIInfo(playground);
                    }
                });
*/

        playgroundRepository.importPOIsFromExternalSource(area)
                .subscribeOn(Schedulers.io())
                .flatMapIterable(new Function<List<LatLng>, Iterable<LatLng>>() {
                    @Override
                    public Iterable<LatLng> apply(@NonNull List<LatLng> latLngs) throws Exception {

                        List<LatLng> playgroundLocations = new ArrayList<LatLng>();

                        for (final LatLng latLng : latLngs) {

                            playgroundLocations.add(latLng);
                        }

                        return playgroundLocations;
                    }
                })
                .retry(3) // Retries retrieving POIs from external source 3 times (now from OSM)
                .subscribe(new Observer<LatLng>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull final LatLng latLng) {

                        playgroundRepository.saveLocation(latLng) // Saves location on GeoFire
                                .subscribe(new SingleObserver<String>() {
                                    @Override
                                    public void onSubscribe(@NonNull Disposable d) {

                                    }

                                    @Override
                                    public void onSuccess(@NonNull String key) {

                                        Playground playground = new Playground(key, latLng);

                                        playgroundRepository.savePlaySitePOIInfo(playground);
                                    }

                                    @Override
                                    public void onError(@NonNull Throwable e) {

                                        LOGGER.log(Level.SEVERE, e.getMessage(), e);
                                    }
                                });
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                        LOGGER.log(Level.WARNING, e.getLocalizedMessage(), e.getCause());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }


    public Observable getPOIsInArea(Observable<Area> area) {

        return playgroundRepository.getPlaySitePOIs(area);
    }

}
