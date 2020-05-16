package com.example.eventmarker.View;

import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eventmarker.Model.BLLManager;
import com.example.eventmarker.Model.FirebaseViewModel;
import com.example.eventmarker.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private BLLManager manager;
    private static final int RC_SIGN_IN = 123;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        manager = BLLManager.getInstance();

        // Obtain a new or prior instance of HotStockViewModel from the
        // ViewModelProviders utility class.
        FirebaseViewModel viewModel = ViewModelProviders.of(this).get(FirebaseViewModel.class);

        LiveData<QuerySnapshot> liveData = viewModel.getDataSnapshotLiveData();

        liveData.observe(this, new Observer<QuerySnapshot>() {
            @Override
            public void onChanged(@Nullable QuerySnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    for (DocumentChange dc : dataSnapshot.getDocumentChanges()) {

                        switch (dc.getType()) {
                            case ADDED:
                                GeoPoint geoPoint = dc.getDocument().getGeoPoint("latLng");
                                String desc = dc.getDocument().getString("desc");
                                System.out.println(geoPoint);
                                System.out.println(desc);
                                //BLLManager.getInstance().readMarker(dc.getDocument());
                                break;
                            case REMOVED:
                                // BLLManager.getInstance().removeMarker(dc.getDocument());
                                break;
                        }
                    }
                }
            }
        });

        checkIfUserIsAlreadyLoggedIn();
        setUpToolbar();
        setUpNavBar();
        setUpUserInfoInMenu();
    }


    private void setUpToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Use this for GPS
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
    private void setUpNavBar(){
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.myMarkersFragments, R.id.mapFragment)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }
    private void checkIfUserIsAlreadyLoggedIn() {
        manager.setUser(FirebaseAuth.getInstance().getCurrentUser());
        if (manager.getUser() == null) {
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(Arrays.asList(
                                    new AuthUI.IdpConfig.GoogleBuilder().build(),
                                    new AuthUI.IdpConfig.EmailBuilder().build(),
                                    new AuthUI.IdpConfig.PhoneBuilder().build()))
                            .build(),
                    RC_SIGN_IN);
        }
    }

    private void setUpUserInfoInMenu(){
        NavigationView navigationView = findViewById(R.id.nav_view);
        View hView = navigationView.getHeaderView(0);
        TextView nav_email = hView.findViewById(R.id.textEmail);
        TextView nav_name = hView.findViewById(R.id.textName);
        ImageView nav_picture = hView.findViewById(R.id.profileImage);
        nav_email.setText(manager.getUser().getEmail());
        nav_name.setText(manager.getUser().getDisplayName());
        nav_picture.setImageURI(manager.getUser().getPhotoUrl());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
