package com.example.eventmarker.Repository;

import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.eventmarker.Entities.MarkerPoint;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class FirestoreDAO extends LiveData<QuerySnapshot> {
        private static final String LOG_TAG = "FirebaseQueryLiveData";

        private final Query query;
        private final CollectionReference collectionReferenceRef;
        private final MyFirebaseListener listener = new MyFirebaseListener();
        private  ListenerRegistration currentListener;

        public FirestoreDAO(CollectionReference ref) {
                this.query = ref;
                this.collectionReferenceRef= ref;
            }

        @Override
        protected void onActive() {
            Log.d(LOG_TAG, "onActive");
            currentListener =  query.addSnapshotListener(listener);
        }

        @Override
        protected void onInactive() {
            Log.d(LOG_TAG, "onInactive");
            currentListener.remove();
        }

        public void addMarker(MarkerPoint marker){
            collectionReferenceRef.add(marker);
        }

        public void deleteMarker(MarkerPoint marker){
            collectionReferenceRef.document(marker.getMarkerID()).delete();
        }

        private class MyFirebaseListener implements EventListener {
            @Override
            public void onEvent(@androidx.annotation.Nullable Object o, @androidx.annotation.Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(LOG_TAG, "Listen failed.", e);
                    return;
                }
                setValue((QuerySnapshot) o);
            }
        }
}
