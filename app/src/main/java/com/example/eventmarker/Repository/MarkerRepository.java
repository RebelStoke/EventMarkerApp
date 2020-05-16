package com.example.eventmarker.Repository;

import android.util.Log;

import com.example.eventmarker.Entities.MarkerPoint;
import com.example.eventmarker.Model.BLLManager;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class MarkerRepository {

   private FirebaseFirestore database = FirebaseFirestore.getInstance();
   private static final String TAG = "MyActivity";

   public MarkerRepository(){}

    public void addMarker(MarkerPoint marker){
        database.collection("eventMarkers").add(marker);
    }

    public void deleteMarker(MarkerPoint marker){
        database.collection("eventMarkers").document(marker.getMarkerID()).delete();
    }

    public void markerListener() {
        database.collection("eventMarkers").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                for (DocumentChange dc : value.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                         //   Log.d(TAG, "New point: " + dc.getDocument().getData());
                            BLLManager.getInstance().readMarker(dc.getDocument());
                            break;
                        case REMOVED:
                        //    Log.d(TAG, "Removed point: " + dc.getDocument().getData());
                            BLLManager.getInstance().removeMarker(dc.getDocument());
                            break;
                    }
                }

            }
        });
    }


}
