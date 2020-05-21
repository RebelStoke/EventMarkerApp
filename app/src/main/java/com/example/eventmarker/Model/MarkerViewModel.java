package com.example.eventmarker.Model;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.eventmarker.Entities.MarkerPoint;
import com.example.eventmarker.Repository.MarkerDAO;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * https://developer.android.com/topic/libraries/architecture/viewmodel
 * This class uses View model to manage and store UI related data. In this case it
 * it mostly stores data gotten from eventMarkers collection.
 */
public class MarkerViewModel extends ViewModel {
    private static final CollectionReference EVENT_REF = FirebaseFirestore.getInstance().collection("eventMarkers");

    private final MarkerDAO liveData = new MarkerDAO(EVENT_REF);

    public void addMarker(LatLng latLng,String name,String startDate,String endDate, String desc, String userID){
        GeoPoint geoPoint = new GeoPoint(latLng.latitude, latLng.longitude);

        MarkerPoint newMarker = new MarkerPoint(geoPoint,name, desc, userID);
        newMarker.setStartDate(startDate);
        newMarker.setEndDate(endDate);

        liveData.addMarker(newMarker);
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
