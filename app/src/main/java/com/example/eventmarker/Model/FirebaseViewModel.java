package com.example.eventmarker.Model;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.eventmarker.Entities.MarkerPoint;
import com.example.eventmarker.Repository.FirestoreDAO;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;

public class FirebaseViewModel extends ViewModel {
    private static final CollectionReference EVENT_REF = FirebaseFirestore.getInstance().collection("eventMarkers");

    private final FirestoreDAO liveData = new FirestoreDAO(EVENT_REF);

    public void addMarker(LatLng latLng, String desc, String userID){
        GeoPoint geoPoint = new GeoPoint(latLng.latitude, latLng.longitude);
        liveData.addMarker(new MarkerPoint(geoPoint, desc, userID));
    }

    public void deleteMarker(MarkerPoint mark)
    {
        liveData.deleteMarker(mark);
    }

    @NonNull
    public LiveData<QuerySnapshot> getDataSnapshotLiveData() {
        return liveData;
    }
}
