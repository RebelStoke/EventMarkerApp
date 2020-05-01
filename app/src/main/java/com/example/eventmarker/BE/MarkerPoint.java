package com.example.eventmarker.BE;

import com.google.firebase.firestore.GeoPoint;

public class MarkerPoint {
    public GeoPoint latLng;
    private String desc;

    public MarkerPoint() {
    }

    public MarkerPoint(GeoPoint latLng, String desc) {
        this.latLng = latLng;
        this.desc = desc;
    }

}
