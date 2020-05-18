package com.example.eventmarker.Model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserViewModel {
    private static UserViewModel instance = new UserViewModel();

    private UserViewModel(){
    }

    public static UserViewModel getInstance(){
        return instance;
    }
    // Change this to AuthViewModel
    public FirebaseUser getUser(){
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public void logOut(){FirebaseAuth.getInstance().signOut();}
}
