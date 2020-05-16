package com.example.eventmarker.Repository;

import android.util.Log;

import androidx.lifecycle.LiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class FirestoreDAO extends LiveData<QuerySnapshot> {
        private static final String LOG_TAG = "FirebaseQueryLiveData";

        private final CollectionReference query;
        private final MyValueEventListener listener = new MyValueEventListener();


        public FirestoreDAO(CollectionReference ref) {
                this.query = ref;
            }

        @Override
        protected void onActive() {
            Log.d(LOG_TAG, "onActive");
            query.addSnapshotListener(listener);
        }

        @Override
        protected void onInactive() {
            Log.d(LOG_TAG, "onInactive");
        }

      /*  private class MyValueEventListener implements ValueEventListener {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println(dataSnapshot);
                setValue(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(LOG_TAG, "Can't listen to query " + query, databaseError.toException());
            }
        }*/
    private class MyValueEventListener implements EventListener {
        @Override
        public void onEvent(@androidx.annotation.Nullable Object o, @androidx.annotation.Nullable FirebaseFirestoreException e) {
            QuerySnapshot value= (QuerySnapshot) o;
            if (e != null) {
                Log.w(LOG_TAG, "Listen failed.", e);
                return;
            }
            setValue(value);
           /* for (DocumentChange dc : value.getDocumentChanges()) {

               switch (dc.getType()) {
                    case ADDED:
                        Log.d(LOG_TAG, "New point: " + dc.getDocument().getData());
                        setValue(dc.getDocument());
                        //BLLManager.getInstance().readMarker(dc.getDocument());
                        break;
                    case REMOVED:
                        Log.d(LOG_TAG, "Removed point: " + dc.getDocument().getData());
                       // BLLManager.getInstance().removeMarker(dc.getDocument());
                        break;
                }
            }*/
        }
    }
}
