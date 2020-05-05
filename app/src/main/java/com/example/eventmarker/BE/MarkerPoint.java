package com.example.eventmarker.BE;

import com.google.firebase.firestore.GeoPoint;

import java.util.Date;

public class MarkerPoint {
    public String markerID;
    public GeoPoint latLng;
    public String desc;
    public String creator_UID;
    public Date startDate;
    public Date endDate;

    public MarkerPoint(GeoPoint latLng, String desc, String creator_UID) {
        this.latLng = latLng;
        this.desc = desc;
        this.creator_UID = creator_UID;
    }

    public MarkerPoint(String markerID, GeoPoint latLng, String desc, String creator_UID) {
        this.creator_UID = creator_UID;
        this.latLng = latLng;
        this.desc = desc;
        this.markerID = markerID;
    }

}
