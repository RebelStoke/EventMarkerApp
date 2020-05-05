package com.example.eventmarker.BLL;

import com.example.eventmarker.BE.MarkerPoint;
import com.example.eventmarker.DAL.MarkerRepository;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BLLManager {
    private static BLLManager instance = new BLLManager();
    private HashMap<MarkerPoint, Marker> markersHashMap;
    private FirebaseUser user;
    private GoogleMap mMap;
    private MarkerRepository repo;

    private BLLManager(){
        repo = new MarkerRepository();
    }

    public static BLLManager getInstance(){
        return instance;
    }

    public void setMap(GoogleMap mMap){
        this.mMap = mMap;
    }

    public void setUser(FirebaseUser user){
        this.user = user;
    }

    public FirebaseUser getUser(){
        return user;
    }

    public void dbListener(){
        markersHashMap = new HashMap<>();
        repo.markerListener();
    }
    
    public List<MarkerPoint> getUserMarkers(){
        List<MarkerPoint> usersMarkers = new ArrayList<>();
        Iterator it = markersHashMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            MarkerPoint mp = (MarkerPoint) pair.getKey();
            if(mp.creator_UID.equals(user.getUid()))
                usersMarkers.add(mp);
        }
        return usersMarkers;
    }

    public void addMarker(LatLng latLng, String desc){
        GeoPoint geoPoint = new GeoPoint(latLng.latitude, latLng.longitude);
        repo.addMarker(new MarkerPoint(geoPoint, desc, user.getUid()));
    }


    public void deleteMarker(MarkerPoint mark){
        repo.deleteMarker(mark);
    }
    
    
    public void readMarker(QueryDocumentSnapshot document) {
        GeoPoint geoPoint = document.getGeoPoint("latLng");
        String desc = document.getString("desc");
        String creator_uid = document.getString("creator_UID");
        String id = document.getId();

        assert geoPoint != null;
        assert mMap != null;
        Marker m = mMap.addMarker(new MarkerOptions().position(new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude())).title(desc));
        MarkerPoint point = new MarkerPoint(id,geoPoint, desc, creator_uid);
        markersHashMap.put(point, m);
    }

    public void removeMarker(QueryDocumentSnapshot document) {
        GeoPoint geoPoint = document.getGeoPoint("latLng");
        String desc = document.getString("desc");
        String creator_uid = document.getString("creator_uid");
        String id = document.getId();

        MarkerPoint markerToDelete = new MarkerPoint(id,geoPoint, desc, creator_uid);

        for (Map.Entry<MarkerPoint, Marker> entry : markersHashMap.entrySet()) {
            if (entry.getKey().latLng.equals(markerToDelete.latLng)) {
                entry.getValue().remove();
                markersHashMap.remove(entry.getKey());
                break;
            }
        }
    }

}
