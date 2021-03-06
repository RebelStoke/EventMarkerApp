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
import com.example.eventmarker.Model.MarkerViewModel;
import com.example.eventmarker.Model.AuthViewModel;
import com.example.eventmarker.View.adapters.recycleAdapter;
import com.example.eventmarker.R;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MyMarkersFragment extends Fragment {
    private AuthViewModel userManager;
    public MyMarkersFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userManager = AuthViewModel.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_my_markers, container, false);
        setRecyclerView(v);
        return v;
    }

    /**
     *  This method is responable for setting up markers inside the map by observing the
     *  live data from database. In order to observe it, it uses a query snapshot
     *  gotten from MarkerViewModel
     * @param v - view which is used in getting the recycler view
     */
    private void setRecyclerView(View v){
        final RecyclerView mRecyclerView = v.findViewById(R.id.recyclerView);

        final List<MarkerPoint> mpList = new ArrayList<>(); // This will contain all of the
        // markers  on the map

        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());


        MarkerViewModel viewModel = ViewModelProviders.of(this)
                .get(MarkerViewModel.class);//get current ViewModel from class

        LiveData<QuerySnapshot> liveData = viewModel.getDataSnapshotLiveData(); // gets the query
        // snapshot containing live data from ViewModel

        liveData.observe(this, new Observer<QuerySnapshot>() { //Gets initial data and then
            // observes any changes that might happen.(Such as create, update , delete)

            @Override
            public void onChanged(@Nullable QuerySnapshot dataSnapshot) {
                if (dataSnapshot != null) { //If any data exists
                    for (DocumentChange dc : dataSnapshot.getDocumentChanges()) {
                        if (dc.getType() == DocumentChange.Type.ADDED) { // What elements to add.
                            //Data changes automatically so no need for Remove as changes will be
                            //implemented instantly


                            //Get all necessary data from Snapshot
                            GeoPoint geoPoint = dc.getDocument().getGeoPoint("latLng");
                            String userId = dc.getDocument().getString("creator_UID");
                            String name = dc.getDocument().getString("nameOfMarker");
                            String desc = dc.getDocument().getString("desc");
                            String itemID = dc.getDocument().getId();

                            if (userId.equals(userManager.getUser().getUid())) {
                                MarkerPoint mark = new MarkerPoint(geoPoint,name, desc, userId);
                                mark.setMarkerID(itemID); //Assigned firebase ID. This is used for removal later on
                                mpList.add(mark); //Assign all user markers to recyclerView
                            }
                        }
                    }
                    RecyclerView.Adapter mAdapter = new recycleAdapter(getActivity(),mpList);
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mRecyclerView.setAdapter(mAdapter);
                }
            }
        });
    }
}
