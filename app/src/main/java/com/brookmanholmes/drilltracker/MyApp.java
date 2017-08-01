package com.brookmanholmes.drilltracker;

import android.app.Application;

import com.brookmanholmes.drilltracker.data.repository.datasource.DrillDataStoreFactory;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

/**
 * Created by Brookman Holmes on 7/16/2017.
 */

public class MyApp extends Application {
    Picasso picasso;

    @Override
    public void onCreate() {
        super.onCreate();
        if (FirebaseAuth.getInstance().getCurrentUser() == null)
            FirebaseAuth.getInstance().signInAnonymously().addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    DrillDataStoreFactory.getInstance();
                }
            });
        picasso = new Picasso.Builder(this).build();
        Picasso.setSingletonInstance(picasso);
    }
}
