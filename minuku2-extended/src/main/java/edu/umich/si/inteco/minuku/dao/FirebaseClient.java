package edu.umich.si.inteco.minuku.dao;

import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by shriti on 7/15/16.
 * A wrapper over Firebase () to obtain
 * a singleton instance of the Firebase client.
 */
public class FirebaseClient {

    private GoogleApiClient mGoogleApiClient;
    private static FirebaseClient mInstance;

    private FirebaseClient(GoogleApiClient aGoogleApiClient) {
        mGoogleApiClient = aGoogleApiClient;
    }

    private FirebaseClient() {
        mGoogleApiClient = null;
    }

    public static FirebaseClient getInstance(GoogleApiClient aGoogleApiClient) {
        if(mInstance == null) {
            mInstance = new FirebaseClient(aGoogleApiClient);
        }
        return mInstance;
    }

    public static FirebaseClient getInstance() {
        if(mInstance == null) {
            mInstance = new FirebaseClient();
        }
        return mInstance;
    }
}
