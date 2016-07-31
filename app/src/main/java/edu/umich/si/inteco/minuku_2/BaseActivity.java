package edu.umich.si.inteco.minuku_2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;

import edu.umich.si.inteco.minuku.config.Constants;
import edu.umich.si.inteco.minuku.config.UserPreferences;

/**
 * Created by shriti on 7/22/16.
 */
public class BaseActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener {

    private static final String LOG_TAG = "BaseActivity";

    protected GoogleApiClient mGoogleApiClient;
    protected Firebase.AuthStateListener mAuthListener;
    protected Firebase mFirebaseRef;
    protected String mProvider;
    protected String mEmail;
    protected UserPreferences mSharedPref;

    // Permission related variables
    protected static final int FINE_LOCATION = 100;
    protected static final int CAMERA = 101;

    protected View mLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSharedPref = UserPreferences.getInstance();

        // Allow google logins
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Create new Client API
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();

        mFirebaseRef = new Firebase(Constants.FIREBASE_URL);

        if(this instanceof LoginActivity) {
            // do nothing
        } else if(this instanceof CreateAccountActivity) {
            // do nothing
        } else {
            mAuthListener = new Firebase.AuthStateListener() {
                @Override
                public void onAuthStateChanged(AuthData authData) {
                    if (authData == null) {
                        Log.e(LOG_TAG, "Kicking user out.");
                        kickUserOut();
                    }
                }
            };
            mFirebaseRef.addAuthStateListener(mAuthListener);
        }

        // Get the provider and email if set. A null value means the user is not yet authenticated.
        mEmail = mSharedPref.getPreference(Constants.ID_SHAREDPREF_EMAIL);
        mProvider = mSharedPref.getPreference(Constants.ID_SHAREDPREF_PROVIDER);

        requestAllPermissions();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // The Auth listener is created only when the user is not a part of the login or
        // create account activity, do the cleanup only in such cases.
        if (!((this instanceof LoginActivity) || (this instanceof CreateAccountActivity))) {
            mFirebaseRef.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_base, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            super.onBackPressed();
            return true;
        }
        if (id == R.id.action_logout) {
            logout();
            return true;
        }
        //new thing, create notification
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }


    @Override
    protected void onResume() {
        super.onResume();
        mSharedPref.writePreference(Constants.CAN_SHOW_NOTIFICATION, Constants.NO);
        // mSharedPref.writePreference(Constants.ID_SHAREDPREF_CANGOOFFLINE, Constants.NO);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSharedPref.writePreference(Constants.CAN_SHOW_NOTIFICATION, Constants.YES);
    }

    /**
     * This is called from the child activities that are not associated
     * with login or account creation flows.
     */
    protected void logout() {
        Toast.makeText(this, "Attemping to logout.", Toast.LENGTH_LONG);
        // mProvider is set only after the user logs in successfully.
        if (mProvider != null) {
            mFirebaseRef.unauth();
            if (mProvider.equals(Constants.GOOGLE_AUTH_PROVIDER)) {
                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {
                                // We do not intend to do anything after logout.
                                // Ignore.
                            }
                        });
            }
        }
    }

    private void kickUserOut() {
        mSharedPref.clear();
        // Shared prefs store data about email, clear that and kick users out by moving them
        // to login screen.
        Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    protected void showToast(String aText) {
        Toast.makeText(this, aText, Toast.LENGTH_SHORT).show();
    }

    public void requestAllPermissions() {
        mLayout = findViewById(R.id.main_layout);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                Log.i("MainActivity",
                        "Displaying location permission rationale to provide additional context.");

                Snackbar.make(mLayout, R.string.permission_location_rationale,
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.ok, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ActivityCompat.requestPermissions(BaseActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        FINE_LOCATION);
                            }
                        })
                        .show();
            } else {
                // Location permission has not been granted yet. Request it directly.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        FINE_LOCATION);
            }

            /*if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.CAMERA)) {

                    Log.i("MainActivity",
                            "Displaying camera permission rationale to provide additional context.");

                    Snackbar.make(mLayout, R.string.squarecamera__request_write_storage_permission_text,
                            Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.ok, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ActivityCompat.requestPermissions(BaseActivity.this,
                                            new String[]{Manifest.permission.CAMERA},
                                            CAMERA);
                                }
                            })
                            .show();
                } else {

                    // Location permission has not been granted yet. Request it directly.
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.CAMERA},
                            CAMERA);
                }
            }*/
        }
    }
}