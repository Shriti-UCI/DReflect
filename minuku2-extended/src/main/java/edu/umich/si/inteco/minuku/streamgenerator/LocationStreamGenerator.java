package edu.umich.si.inteco.minuku.streamgenerator;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.concurrent.atomic.AtomicLong;

import edu.umich.si.inteco.minuku.config.Constants;
import edu.umich.si.inteco.minuku.dao.LocationDataRecordDAO;
import edu.umich.si.inteco.minuku.manager.AndroidStreamManager;
import edu.umich.si.inteco.minuku.manager.DAOManager;
import edu.umich.si.inteco.minuku.model.LocationDataRecord;
import edu.umich.si.inteco.minuku.stream.LocationStream;
import edu.umich.si.inteco.minukucore.dao.DAOException;
import edu.umich.si.inteco.minukucore.exception.StreamAlreadyExistsException;
import edu.umich.si.inteco.minukucore.exception.StreamNotFoundException;
import edu.umich.si.inteco.minukucore.manager.StreamManager;
import edu.umich.si.inteco.minukucore.stream.Stream;

/**
 * Created by neerajkumar on 7/18/16.
 */
public class LocationStreamGenerator implements AndroidStreamGenerator<LocationDataRecord>,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private LocationStream mStream;
    private String TAG = "LocationStreamGenerator";

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    private AtomicLong latitude;
    private AtomicLong longitude;

    LocationDataRecordDAO mDAO;

    public LocationStreamGenerator() {
        this.mStream = new LocationStream(Constants.LOCATION_QUEUE_SIZE);
        this.mDAO = DAOManager.getInstance().getDaoFor(LocationDataRecord.class);
    }


    @Override
    public void onStreamRegistration(Context applicationContext) {
        // do nothing.
        if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(applicationContext)
                == ConnectionResult.SUCCESS) {
            mGoogleApiClient = new GoogleApiClient.Builder(applicationContext)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
            if (!mGoogleApiClient.isConnected() || !mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        } else {
            Log.e(TAG, "Error occurred while attempting to access Google play.");
        }

    }

    @Override
    public void register() {
        try {
            AndroidStreamManager.getInstance().register(mStream, LocationDataRecord.class, this);
        } catch (StreamNotFoundException streamNotFoundException) {
            Log.d(TAG, "Another stream which provides LocationDataRecord is already registered.");
        } catch (StreamAlreadyExistsException streamAlreadyExistsException) {
            Log.d(TAG, "Another stream which provides LocationDataRecord is already registered.");
        }
    }

    @Override
    public Stream<LocationDataRecord> generateNewStream() {
        return mStream;
    }

    @Override
    public boolean updateStream() {
        LocationDataRecord locationDataRecord = new LocationDataRecord();
        locationDataRecord.setLatitude(latitude.get());
        locationDataRecord.setLongitude(longitude.get());
        mStream.add(locationDataRecord);
        try {
            mDAO.add(locationDataRecord);
        } catch (DAOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public long getUpdateFrequency() {
        return 5; // 5 minutes
    }

    @Override
    public void sendStateChangeEvent() {

    }

    @Override
    public void onStreamRegistration() {

    }


    /**
     * Location Listerner events start here.
     */

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            Log.e(TAG, "GPS: "
                    + location.getLatitude() + ", "
                    + location.getLongitude() + ", "
                    + "accuracy: " + location.getAccuracy());

            // If the location is accurate to 30 meters, it's good enough for us.
            // Post an update event and exit.
            if (location.getAccuracy() < 30.0f) {
                this.latitude.set((long) location.getLatitude());
                this.longitude.set((long) location.getLongitude());
            }
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected");

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.FusedLocationApi
                .requestLocationUpdates(mGoogleApiClient, mLocationRequest,
                        (com.google.android.gms.location.LocationListener) this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG, "Connection to Google play services failed.");
        stopCheckingForLocationUpdates();
    }

    private void stopCheckingForLocationUpdates() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
            try {
                AndroidStreamManager.getInstance().unregister(mStream, this);
                Log.e(TAG, "Unregistering location stream generator from stream manager");
            } catch (StreamNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
