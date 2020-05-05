package com.example.eventmarker.GUI.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eventmarker.BE.MarkerPoint;
import com.example.eventmarker.BLL.BLLManager;
import com.example.eventmarker.GUI.adapters.recycleAdapter;
import com.example.eventmarker.R;

import java.util.List;

public class MyMarkersFragment extends Fragment {

    public MyMarkersFragment() {}

    public void setRecyclerView(View v){
        RecyclerView mRecyclerView = v.findViewById(R.id.recyclerView);
        List<MarkerPoint> mpList = BLLManager.getInstance().getUserMarkers();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        RecyclerView.Adapter mAdapter = new recycleAdapter(mpList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_my_markers, container, false);
        setRecyclerView(v);
        return v;
    }
    public void deleteMarker(View v){
        System.out.println("NOAP");
    }
}
