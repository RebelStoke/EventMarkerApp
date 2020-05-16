package com.example.eventmarker.Model;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.eventmarker.Repository.FirestoreDAO;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class FirebaseViewModel extends ViewModel {
    private static final CollectionReference EVENT_REF = FirebaseFirestore.getInstance().collection("eventMarkers");

    private final FirestoreDAO liveData = new FirestoreDAO(EVENT_REF);

    @NonNull
    public LiveData<QuerySnapshot> getDataSnapshotLiveData() {
        System.out.println("NEW ASS LIVE DATA " + liveData);
        return liveData;
    }
}
