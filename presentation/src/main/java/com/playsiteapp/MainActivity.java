package com.playsiteapp;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.infrastructure.location.LocationService;
import com.patloew.rxlocation.RxLocation;
import com.playsiteapp.contracts.MainContract;
import com.playsiteapp.presenters.MainPresenter;
import com.squareup.leakcanary.LeakCanary;

import io.reactivex.MaybeObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;


public class MainActivity extends AppCompatActivity implements MainContract.MapsView,
        OnMapReadyCallback {

    final static float ZOOM_LEVEL = 15;

    private GoogleMap mainMap;
    private MainContract.Presenter mainPresenter;
    private boolean locationPermAccepted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this.getApplication());

        setContentView(R.layout.activity_main);

        LocationService locationService = new LocationService(this);

        mainPresenter = new MainPresenter(this, locationService);

        PlaceAutocompleteFragment placeAutocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.autocompleteSearch);

        placeAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {

                mainPresenter.locateUserFromSearch(place.getLatLng().latitude, place.getLatLng().longitude);
            }

            @Override
            public void onError(Status status) {

            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mainMap = googleMap;

        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                1);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            mainMap.setMyLocationEnabled(true);
            mainMap.getUiSettings().setZoomControlsEnabled(true);
            mainMap.setMinZoomPreference(ZOOM_LEVEL);

            int padding_in_dp = 50;
            final float scale = getResources().getDisplayMetrics().density;
            int padding_in_px = (int) (padding_in_dp * scale + 0.5f);

            mainMap.setPadding(0, padding_in_px, 0, padding_in_px);


            mainPresenter.autoLocateUser();

        }

        mainPresenter.onMapReady();
    }

    @Override
    public void generateMap() {

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public GoogleMap getMainMap() {
        return mainMap;
    }


    @Override
    public void updateLocationOnMap(double latitude, double longitude) {

        LatLng latLngUser = new LatLng(latitude, longitude);
        mainMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngUser, ZOOM_LEVEL));

    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mainPresenter.unsubscribe();
    }

    @Override
    public Marker addMarker(double latitude, double longitude) {

        return mainMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)));
    }
}
