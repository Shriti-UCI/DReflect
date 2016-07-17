package edu.umich.si.inteco.minuku.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by shriti on 7/15/16.
 *
 * Imported code from dstudio
 *
 * Created by neera_000 on 1/28/2016.
 *
 * This class can be used to store the globally shared preferences, e.g. the login details of the
 * user, firebase information etc.
 *
 */
public class UserPreferences {

    private static AtomicInteger numActivities = new AtomicInteger(0);
    private String LOG_TAG = getClass().getSimpleName();
    private static UserPreferences mInstance;
    private Context mContext;
    //
    private SharedPreferences mMyPreferences;

    private UserPreferences(){ }

    public static UserPreferences getInstance(){
        if (mInstance == null) mInstance = new UserPreferences();
        return mInstance;
    }

    public void Initialize(Context ctxt){
        mContext = ctxt;
        //
        mMyPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    public void writePreference(String key, String value){
        SharedPreferences.Editor e = mMyPreferences.edit();
        e.putString(key, value);
        e.commit();
    }

    public String getPreference(String key) {
        return mMyPreferences.getString(key, null);
    }

    public void clear() {
        SharedPreferences.Editor e = mMyPreferences.edit();
        e.clear();
        e.commit();
    }

}

