/*
 * Copyright (c) 2016.
 *
 * DReflect and Minuku Libraries by Shriti Raj (shritir@umich.edu) and Neeraj Kumar(neerajk@uci.edu) is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License.
 * Based on a work at https://github.com/Shriti-UCI/Minuku-2.
 *
 *
 * You are free to (only if you meet the terms mentioned below) :
 *
 * Share — copy and redistribute the material in any medium or format
 * Adapt — remix, transform, and build upon the material
 *
 * The licensor cannot revoke these freedoms as long as you follow the license terms.
 *
 * Under the following terms:
 *
 * Attribution — You must give appropriate credit, provide a link to the license, and indicate if changes were made. You may do so in any reasonable manner, but not in any way that suggests the licensor endorses you or your use.
 * NonCommercial — You may not use the material for commercial purposes.
 * ShareAlike — If you remix, transform, or build upon the material, you must distribute your contributions under the same license as the original.
 * No additional restrictions — You may not apply legal terms or technological measures that legally restrict others from doing anything the license permits.
 */

package edu.umich.si.inteco.minuku_2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
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
import com.google.android.gms.location.places.Places;
import com.instabug.library.Instabug;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;

import edu.umich.si.inteco.minuku.config.Constants;
import edu.umich.si.inteco.minuku.config.UserPreferences;
import edu.umich.si.inteco.minuku.dao.UserSubmissionStatsDAO;
import edu.umich.si.inteco.minuku.logger.Log;
import edu.umich.si.inteco.minuku.manager.MinukuDAOManager;
import edu.umich.si.inteco.minuku.model.UserSubmissionStats;
import edu.umich.si.inteco.minukucore.dao.DAOException;
import edu.umich.si.inteco.minukucore.event.NotificationClickedEvent;

/**
 * Created by shriti on 7/22/16.
 */
public class BaseActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "BaseActivity";

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

        String endTime = UserPreferences.getInstance().getPreference("endTime");
        Log.d(TAG, "end time " + endTime);
        String startTime = UserPreferences.getInstance().getPreference("startTime");
        Log.d(TAG, "start time " + startTime);

        // Allow google logins
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("1037387516994-tm6655k2k1qa59k0bl8g2pifsf3tdvrf.apps.googleusercontent.com")
                .requestEmail()
                .build();
        // Create new Client API
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
        mGoogleApiClient.connect();

        mFirebaseRef = new Firebase(Constants.FIREBASE_URL);

        if (this instanceof LoginActivity) {
            // do nothing
        } else if (this instanceof CreateAccountActivity) {
            // do nothing
        } else {
            mAuthListener = new Firebase.AuthStateListener() {
                @Override
                public void onAuthStateChanged(AuthData authData) {
                    if (authData == null) {
                        Log.e(TAG, "Kicking user out.");
                        kickUserOut();
                    }
                }
            };
            mFirebaseRef.addAuthStateListener(mAuthListener);
        }

        // Get the provider and email if set. A null value means the user is not yet authenticated.
        mEmail = mSharedPref.getPreference(Constants.ID_SHAREDPREF_EMAIL);
        mProvider = mSharedPref.getPreference(Constants.ID_SHAREDPREF_PROVIDER);
        Log.setDeviceString(mEmail);
        Instabug.setUserEmail(mEmail);

        // The base activity takes care of getting information about the notification that started
        // the activity (if there is one), and posting the NotificationClickEvent on the bus.
        Log.d(TAG, "Checking for bundle passed onto base activity in oncreate method");
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Log.d(TAG, "Trying to get notification id information from bundle");
            String tappedNotificationId = bundle.getString(Constants.TAPPED_NOTIFICATION_ID_KEY);

            if (tappedNotificationId != null
                    && !tappedNotificationId.equals("")
                    && !tappedNotificationId.trim().equals("")) {
                Log.d(TAG, "Got notifiation Id information from bundle: " + tappedNotificationId);
                EventBus.getDefault().post(new NotificationClickedEvent(tappedNotificationId));
            }
        }

        // Need this to register the UserSubmissionStatsDAO.
        MinukuDAOManager daoManager = MinukuDAOManager.getInstance();
        UserSubmissionStatsDAO userSubmissionStatsDAO = new UserSubmissionStatsDAO();
        daoManager.registerDaoFor(UserSubmissionStats.class, userSubmissionStatsDAO);

        requestAllPermissions();

        //moving code from on resume here
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
        Log.d(TAG, "Destroying instance of " + this.getClass().getSimpleName());
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
        /*mSharedPref.writePreference(Constants.CAN_SHOW_NOTIFICATION, Constants.NO);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                Future<UserSubmissionStats> submissionStatsFuture = ((UserSubmissionStatsDAO)
                        MinukuDAOManager.getInstance().getDaoFor(UserSubmissionStats.class)).get();
                while (!submissionStatsFuture.isDone()) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //
                try {
                    gotUserStatsFromDatabase(submissionStatsFuture.get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Log.d(TAG, "Creating mUserSubmissionStats");
                    gotUserStatsFromDatabase(null);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    gotUserStatsFromDatabase(null);
                }
            }
        });*/
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


    public int getRewardRelevantSubmissionCount(UserSubmissionStats userSubmissionStats) {
        return (userSubmissionStats.getGlucoseReadingCount() +
                userSubmissionStats.getInsulinCount() +
                userSubmissionStats.getFoodCount()+
                userSubmissionStats.getMoodCount());
    }


    protected boolean areDatesEqual(long currentTime, long previousTime) {
        Log.d(TAG, "Checking if the both dates are the same");

        Calendar currentDate = Calendar.getInstance();
        Calendar previousDate = Calendar.getInstance();

        currentDate.setTimeInMillis(currentTime);
        previousDate.setTimeInMillis(previousTime);
        Log.d(TAG, "Current:" + currentDate.toString() + " Previous:" + previousDate.toString());

        boolean sameDay = currentDate.get(Calendar.YEAR) == previousDate.get(Calendar.YEAR) &&
                currentDate.get(Calendar.DAY_OF_YEAR) == previousDate.get(Calendar.DAY_OF_YEAR) &&
                currentDate.get(Calendar.MONTH) == previousDate.get(Calendar.MONTH);
        return sameDay;
    }
}
