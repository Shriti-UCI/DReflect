package edu.umich.si.inteco.minuku_2;

import android.content.Context;
import android.location.Location;

import com.firebase.client.Firebase;

import java.util.UUID;

import edu.umich.si.inteco.minuku.config.UserPreferences;
import edu.umich.si.inteco.minuku.dao.LocationDataRecordDAO;
import edu.umich.si.inteco.minuku.manager.DAOManager;
import edu.umich.si.inteco.minuku.model.LocationDataRecord;
import edu.umich.si.inteco.minuku.streamgenerator.LocationStreamGenerator;
import edu.umich.si.inteco.minukucore.user.User;

/**
 * Created by neerajkumar on 7/18/16.
 */
public class MinukuApp extends android.app.Application {

    private static MinukuApp instance;
    private static Context mContext;

    public static MinukuApp getInstance() {
        return instance;
    }

    public static Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
        UserPreferences.getInstance().Initialize(getApplicationContext());
    }
}
