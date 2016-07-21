package edu.umich.si.inteco.minuku.config;

import edu.umich.si.inteco.minuku.BuildConfig;

/**
 * Created by shriti on 7/17/16.
 */
public class Constants {
    public static final String YES = "YES";
    public static final String NO = "NO";

    // Firebase config
    public static final String FIREBASE_URL = BuildConfig.UNIQUE_FIREBASE_ROOT_URL;
    public static final String FIREBASE_URL_USERS = FIREBASE_URL + "/users";
    public static final String FIREBASE_URL_MOODS = FIREBASE_URL + "/moods";
    public static final String FIREBASE_URL_NOTES = FIREBASE_URL + "/notes";
    public static final String FIREBASE_URL_IMAGES = FIREBASE_URL + "/photos";
    public static final String FIREBASE_URL_LOCATION = FIREBASE_URL + "/location";


    // Provider stuff
    public static final String GOOGLE_AUTH_PROVIDER = "google";
    public static final String PASSWORD_PROVIDER = "password";
    //public static final String PROVIDER_DATA_DISPLAY_NAME = "displayName";

    // Google provider hashkeys
    public static final String GGL_PROVIDER_USERNAME_KEY = "username";
    public static final String GGL_PROVIDER_EMAIL_KEY = "email";

    // Shared pref ids
    public static final String ID_SHAREDPREF_EMAIL = "email";
    public static final String ID_SHAREDPREF_PROVIDER = "provider";
    //public static final String ID_SHAREDPREF_DISPLAYNAME = "displayName";

    public static final String KEY_SIGNUP_EMAIL = "SIGNUP_EMAIL";
    public static final String KEY_ENCODED_EMAIL = "ENCODED_EMAIL";

    public static final String LOG_ERROR = "Error:";


    // Prompt service related constants
    public static final int PROMPT_SERVICE_REPEAT_MILLISECONDS = 1000 * 60; // 1 minute


    // NNotification related constants
    public static final String REMINDER_NOTIFICATION_TITLE = "DStudio Reminder";
    public static final String REMINDER_NOTIFICATION_CONTENT = "Everyone forgets, we get that! That's why god made reminders";
    public static final String CAN_SHOW_NOTIFICATION = "ENABLE_NOTIFICATIONS";

    //default queue size
    public static final int DEFAULT_QUEUE_SIZE = 100;

    //specific queue sizes
    public static final int LOCATION_QUEUE_SIZE = 100;
    public static final int IMAGE_QUEUE_SIZE = 100;
}
