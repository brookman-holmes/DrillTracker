package com.brookmanholmes.drilltracker;

import android.app.Application;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.solovyev.android.checkout.Billing;

import javax.annotation.Nonnull;

import timber.log.Timber;

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

    public MyApp() {
        instance = this;
    }

    public static MyApp get() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG)
            Timber.plant(new Timber.DebugTree());

        Billing.setLogger(Billing.newLogger());
        Picasso picasso = new Picasso.Builder(this).build();
        Picasso.setSingletonInstance(picasso);
        //picasso.setLoggingEnabled(BuildConfig.DEBUG);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    public Billing getBilling() {
        return billing;
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

    }
}
