package com.brookmanholmes.drilltracker.presentation.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.brookmanholmes.drilltracker.R;
import com.brookmanholmes.drilltracker.presentation.drills.DrillsListActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity implements OnSuccessListener<AuthResult>, OnFailureListener {
    private static final String TAG = SignInActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        FirebaseAuth.getInstance().signInAnonymously()
                .addOnSuccessListener(this)
                .addOnFailureListener(this);
    }

    protected void showToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        showToastMessage(e.getMessage());
    }

    @Override
    public void onSuccess(AuthResult authResult) {
        Log.i(TAG, "onSuccess: " + authResult.getUser().getUid());
        startDrillsListActivity();
    }

    private void startDrillsListActivity() {
        startActivity(new Intent(this, DrillsListActivity.class));
    }
}
