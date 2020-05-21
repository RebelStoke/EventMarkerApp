package com.example.eventmarker.Model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * This class is a singleton.
 * Use this class to maintain User authentication in the application
 */
public class AuthViewModel {
    private static AuthViewModel instance = new AuthViewModel();

    private AuthViewModel(){}

    public static AuthViewModel getInstance(){
        return instance;
    }

    public FirebaseUser getUser(){
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public void logOut(){FirebaseAuth.getInstance().signOut();}
}
