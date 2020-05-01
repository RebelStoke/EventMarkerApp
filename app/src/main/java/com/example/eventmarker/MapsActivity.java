package com.example.eventmarker;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.eventmarker.BE.MarkerPoint;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "MyActivity";
    private GoogleMap mMap;
    private FirebaseFirestore database;
    private CollectionReference markersCollection;
    private List<MarkerPoint> markersList = new ArrayList<>();
    private HashMap<MarkerPoint, Marker> markersHashMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        database = FirebaseFirestore.getInstance();
        markersCollection = database.collection("eventMarkers");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //getMarkers();
        markerListener();
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                addNewMarker(latLng);
            }
        });
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    public void addNewMarker(LatLng latLng) {
        GeoPoint geoPoint = new GeoPoint(latLng.latitude, latLng.longitude);
        MarkerPoint marker = new MarkerPoint(geoPoint, "Marker");

        markersCollection.add(marker).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    public void getMarkers() {
        markersCollection.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                readMarker(document);
                            }
                            System.out.println(markersHashMap.size());
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public void readMarker(QueryDocumentSnapshot document) {
        System.out.println(document.getData());
        GeoPoint geoPoint = document.getGeoPoint("latLng");
        String desc = document.getString("desc");

        assert geoPoint != null;
        Marker m = mMap.addMarker(new MarkerOptions().position(new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude())).title(desc));
        MarkerPoint point = new MarkerPoint(geoPoint, desc);
        markersHashMap.put(point, m);
    }

    public void removeMarker(QueryDocumentSnapshot document) {
        markersHashMap.size();
        GeoPoint geoPoint = document.getGeoPoint("latLng");
        String desc = document.getString("desc");

        MarkerPoint markerToDelete = new MarkerPoint(geoPoint, desc);

        for (Map.Entry<MarkerPoint, Marker> entry : markersHashMap.entrySet()) {
            if (entry.getKey().latLng.equals(markerToDelete.latLng)) {
                entry.getValue().remove();
                markersHashMap.remove(entry.getKey());
                break;
            }
        }
    }

    public void markerListener() {
        markersCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                            Log.d(TAG, "New point: " + dc.getDocument().getData());
                            readMarker(dc.getDocument());
                            break;
                        case REMOVED:
                            Log.d(TAG, "Removed point: " + dc.getDocument().getData());
                            removeMarker(dc.getDocument());
                            break;
                    }
                }

            }
        });
    }

}
