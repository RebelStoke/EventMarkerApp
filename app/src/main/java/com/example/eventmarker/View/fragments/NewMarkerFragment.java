package com.example.eventmarker.View.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.eventmarker.Model.FirebaseViewModel;
import com.example.eventmarker.Model.UserViewModel;
import com.example.eventmarker.R;
import com.google.android.gms.maps.model.LatLng;

import java.util.Calendar;

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
    private UserViewModel userManager;

    public NewMarkerFragment() {
        userManager = UserViewModel.getInstance();
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

        final EditText startDate=(EditText) v.findViewById(R.id.editText);
        final EditText endDate=(EditText) v.findViewById(R.id.editText3);

        v.findViewById(R.id.addButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseViewModel viewModel = ViewModelProviders.of(requireActivity()).get(FirebaseViewModel.class);
                TextView nameText = (TextView)v.findViewById(R.id.nameText);
                TextView descriptionText = (TextView)v.findViewById(R.id.descriptionText);
                viewModel.addMarker(new LatLng(lat, lng), nameText.getText().toString(),startDate.getText().toString(),endDate.getText().toString(),descriptionText.getText().toString(), userManager.getUser().getUid());
                openMapFragment();
            }
        });

        //Set up calendar
        final Calendar cldr = Calendar.getInstance();
        final int day = cldr.get(Calendar.DAY_OF_MONTH);
        final int month = cldr.get(Calendar.MONTH);
        final int year = cldr.get(Calendar.YEAR);

        //For both Start date and end date we set up an individual date picker
        startDate.setInputType(InputType.TYPE_NULL);
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // date picker dialog
                DatePickerDialog picker = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                startDate.setText(year +"/" + (monthOfYear + 1) + "/" + dayOfMonth);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        endDate.setInputType(InputType.TYPE_NULL);
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // date picker dialog
                DatePickerDialog picker = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                endDate.setText(year +"/" + (monthOfYear + 1) + "/" + dayOfMonth);
                            }
                        }, year, month, day);
                picker.show();
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
