package edu.umich.si.inteco.minuku.logger;

import com.bugfender.sdk.Bugfender;

/**
 * Created by shriti on 9/1/16.
 */
public class Log {

    public Log(){

    }

    public static void d(String TAG, String message) {
        Bugfender.d(TAG, message);
        android.util.Log.d(TAG, message);
    }

    public static void e(String TAG, String message) {
        Bugfender.e(TAG, message);
        android.util.Log.e(TAG, message);
    }

    public static void w(String TAG, String message) {
        Bugfender.w(TAG, message);
        android.util.Log.w(TAG, message);
    }

    public static void i(String TAG, String message) {
        Bugfender.d(TAG, message);
        android.util.Log.i(TAG, message);
    }

    public static String getDeviceIdentifier() {

        return Bugfender.getDeviceIdentifier();
    }

    public static void setDeviceString(String deviceString) {
        Bugfender.setDeviceString("user.email", deviceString);
    }
}
