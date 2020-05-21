package com.example.eventmarker.Model;

import com.google.android.gms.maps.GoogleMap;

/**
 * This class is singleton.
 * The class is responsible for storing google map for fragment reference.
 */
public class MapViewModel {

    private static MapViewModel instance = new MapViewModel();
    private GoogleMap googleMap;

    private MapViewModel(){}

    public static MapViewModel getInstance(){
        return instance;
    }

    public void setMap(GoogleMap googleMap){
        this.googleMap = googleMap;
    }

    public GoogleMap getMap(){
        return googleMap;
    }
}
