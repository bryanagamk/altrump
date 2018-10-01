package com.pens.afdolash.altrump.firebaseHelper;

import android.support.annotation.NonNull;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class DatabaseHelper {

    public FirebaseUser getCurrentUser() {
        FirebaseAuth.AuthStateListener authListener;
        FirebaseAuth auth;
        final FirebaseUser[] user = new FirebaseUser[1];

        auth = FirebaseAuth.getInstance();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user[0] = firebaseAuth.getCurrentUser();
            }
        };

        return user[0];
    }
}
