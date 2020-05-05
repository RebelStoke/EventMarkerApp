package com.example.eventmarker.GUI.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.eventmarker.BLL.BLLManager;
import com.example.eventmarker.R;
import com.google.android.gms.maps.model.LatLng;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewMarkerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewMarkerFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private double lat;
    private double lng;


    public NewMarkerFragment() {
    }

    // TODO: Rename and change types and number of parameters
    public static NewMarkerFragment newInstance(double lat, double lng) {
        NewMarkerFragment fragment = new NewMarkerFragment();
        Bundle args = new Bundle();
        args.putDouble(ARG_PARAM1, lat);
        args.putDouble(ARG_PARAM2, lng);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            lat = getArguments().getDouble(ARG_PARAM1);
            lng = getArguments().getDouble(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_new_marker, container, false);

        v.findViewById(R.id.addButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView t = (TextView)v.findViewById(R.id.descriptionText);
                BLLManager.getInstance().addMarker(new LatLng(lat, lng), t.getText().toString());
                openMapFragment();
            }
        });

        return v;
    }

    private void openMapFragment(){
        FragmentManager fm = getFragmentManager();
        assert fm != null;
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, new MapFragment());
        fragmentTransaction.commit();
    }
}
