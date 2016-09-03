package edu.umich.si.inteco.minuku_2;

import android.content.Context;

import com.bugfender.sdk.Bugfender;
import com.firebase.client.Firebase;

import edu.umich.si.inteco.minuku.config.UserPreferences;

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

        Bugfender.init(this, "N7pdXEGbmKhK9k8YtpFPyXORtsAwgZa5", BuildConfig.DEBUG);
        Bugfender.enableLogcatLogging();
        Bugfender.enableUIEventLogging(this);

        //register questions registerStudyQuestions();
    }
}
