package com.example.eventmarker.Entities;

import com.google.firebase.firestore.GeoPoint;

/**
 *  Marker class which contains all the information from firebase.
 *  markerID - ID of item in firebase. If the item is just added. It will not contain any ID
 *  nameOfMarker - name of marker
 *  creator_UID - the user that created marker
 */
public class MarkerPoint {
    private String nameOfMarker;
    private String markerID;
    private GeoPoint latLng;
    private String desc;
    private String creator_UID;
    private String startDate;
    private String endDate;

    public MarkerPoint(GeoPoint latLng, String nameOfMarker, String desc, String creator_UID) {
        this.latLng = latLng;
        this.nameOfMarker= nameOfMarker;
        this.desc = desc;
        this.creator_UID = creator_UID;
    }

    public String getMarkerID() {
        return markerID;
    }

    public String getNameOfMarker() {
        return nameOfMarker;
    }

    public void setNameOfMarker(String nameOfMarker) {
        this.nameOfMarker = nameOfMarker;
    }

    public void setMarkerID(String markerID) {
        this.markerID = markerID;
    }

    public GeoPoint getLatLng() {
        return latLng;
    }

    public void setLatLng(GeoPoint latLng) {
        this.latLng = latLng;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCreator_UID() {
        return creator_UID;
    }

    public void setCreator_UID(String creator_UID) {
        this.creator_UID = creator_UID;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
