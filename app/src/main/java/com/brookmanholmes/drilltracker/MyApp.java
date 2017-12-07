package com.brookmanholmes.drilltracker;

import android.app.Application;
import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.solovyev.android.checkout.Billing;

import javax.annotation.Nonnull;

/**
 * Created by Brookman Holmes on 7/16/2017.
 */

public class MyApp extends Application implements FirebaseAuth.AuthStateListener {
    private static MyApp instance;
    private final Billing billing = new Billing(this, new Billing.DefaultConfiguration() {
        @Nonnull
        @Override
        public String getPublicKey() {
            return TempPublicKey.KEY;
        }
    });
    private Picasso picasso;

    public MyApp() {
        instance = this;
    }

    public static MyApp get() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Billing.setLogger(Billing.newLogger());
        picasso = new Picasso.Builder(this).build();
        Picasso.setSingletonInstance(picasso);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    public Billing getBilling() {
        return billing;
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

    }
}
