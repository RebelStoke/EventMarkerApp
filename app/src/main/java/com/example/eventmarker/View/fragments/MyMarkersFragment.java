package com.example.eventmarker.View.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eventmarker.Entities.MarkerPoint;
import com.example.eventmarker.Model.FirebaseViewModel;
import com.example.eventmarker.Model.UserViewModel;
import com.example.eventmarker.View.adapters.recycleAdapter;
import com.example.eventmarker.R;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MyMarkersFragment extends Fragment {
    private UserViewModel userManager;
    public MyMarkersFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userManager = UserViewModel.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_my_markers, container, false);
        setRecyclerView(v);
        return v;
    }

    public void setRecyclerView(View v){
        final RecyclerView mRecyclerView = v.findViewById(R.id.recyclerView);

        final List<MarkerPoint> mpList = new ArrayList<>();
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        //Model-view-ViewModel
        FirebaseViewModel viewModel = ViewModelProviders.of(this).get(FirebaseViewModel.class);

        LiveData<QuerySnapshot> liveData = viewModel.getDataSnapshotLiveData();

        liveData.observe(this, new Observer<QuerySnapshot>() {
            @Override
            public void onChanged(@Nullable QuerySnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    for (DocumentChange dc : dataSnapshot.getDocumentChanges()) {
                        if (dc.getType() == DocumentChange.Type.ADDED) {
                            GeoPoint geoPoint = dc.getDocument().getGeoPoint("latLng");
                            String userId = dc.getDocument().getString("creator_UID");
                            String name = dc.getDocument().getString("nameOfMarker");
                            String desc = dc.getDocument().getString("desc");
                            String itemID = dc.getDocument().getId();
                            System.out.println(userId);
                            if (userId.equals(userManager.getUser().getUid())) {
                                MarkerPoint mark = new MarkerPoint(geoPoint,name, desc, userId);
                                mark.setMarkerID(itemID);
                                mpList.add(mark);
                            }
                        }
                    }
                    // TODO: this might be a bad place to assign the adapter. Should check a better spot for this
                    RecyclerView.Adapter mAdapter = new recycleAdapter(getActivity(),mpList);
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mRecyclerView.setAdapter(mAdapter);
                }
            }
        });
    }
}
