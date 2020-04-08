package com.brookmanholmes.drilltracker.presentation.base;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.brookmanholmes.drilltracker.R;
import com.brookmanholmes.drilltracker.presentation.signin.SignInActivity;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Brookman Holmes on 7/7/2017.
 */

public abstract class BaseActivity extends AppCompatActivity {
    private static final String TAG = BaseActivity.class.getName();
    /**
     * Adds a {@link Fragment} to this activity's layout.
     *
     * @param containerViewId The container view to where add the fragment.
     * @param fragment The fragment to be added.
     */
    private void addFragment(@IdRes int containerViewId, Fragment fragment) {
        final FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(containerViewId, fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(this, SignInActivity.class));
            finish();
        } else {
            if (savedInstanceState == null)
                addFragment(R.id.fragmentContainer, getFragment());
        }
    }

    protected abstract Fragment getFragment();
}
