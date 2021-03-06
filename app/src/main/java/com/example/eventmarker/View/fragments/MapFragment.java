package com.example.eventmarker.View.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.eventmarker.Model.MarkerViewModel;
import com.example.eventmarker.Model.MapViewModel;
import com.example.eventmarker.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class MapFragment extends Fragment implements OnMapReadyCallback {
    private MapViewModel mapViewModel;
    private LocationManager locationManager;

    public MapFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_maps, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {

        Criteria criteria = new Criteria();
        //In case the permisions are not checked. Do not move the camera anywhere.
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Location location = locationManager.getLastKnownLocation(Objects.requireNonNull(locationManager.getBestProvider(criteria, false)));
            assert location != null;
            LatLng centerLocation = new LatLng(location.getLatitude(), location.getLongitude());
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(centerLocation));

        }
        MarkerViewModel viewModel = ViewModelProviders.of(this).get(MarkerViewModel.class);

        LiveData<QuerySnapshot> liveData = viewModel.getDataSnapshotLiveData();
        //Populate the map with markers
        liveData.observe(this, new Observer<QuerySnapshot>() {
            @Override
            public void onChanged(@androidx.annotation.Nullable QuerySnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    for (DocumentChange dc : dataSnapshot.getDocumentChanges()) {
                        if (dc.getType() == DocumentChange.Type.ADDED) {
                            GeoPoint geoPoint = dc.getDocument().getGeoPoint("latLng");
                            String desc = dc.getDocument().getString("desc");

                            String startTime = dc.getDocument().getString("startDate");
                            if(startTime == null) startTime= "";

                            String endTime = dc.getDocument().getString("endDate");
                            if(endTime == null) endTime= "";

                            assert geoPoint != null;
                            googleMap.addMarker(new MarkerOptions().position(new LatLng(geoPoint.getLatitude(),
                                    geoPoint.getLongitude())).title(desc + " " + startTime + " - " + endTime));
                        }
                    }
                }
            }
        });
        mapViewModel = MapViewModel.getInstance();
        mapViewModel.setMap(googleMap);
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
               openNewMarkerFragment(latLng);
            }
        });
    }

    private void openNewMarkerFragment(LatLng latLng){
        FragmentManager fm = getFragmentManager();
        assert fm != null;
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, NewMarkerFragment.newInstance(latLng.latitude, latLng.longitude));
        fragmentTransaction.commit();
    }
}