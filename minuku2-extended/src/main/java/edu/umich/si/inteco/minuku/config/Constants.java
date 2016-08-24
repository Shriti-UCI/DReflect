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
    public static final String FIREBASE_URL_NOTIFICATIONS = FIREBASE_URL + "/notifications";
    public static final String FIREBASE_URL_IMAGES = FIREBASE_URL + "/photos";
    public static final String FIREBASE_URL_LOCATION = FIREBASE_URL + "/location";
    public static final String FIREBASE_URL_SEMANTIC_LOCATION = FIREBASE_URL + "/semantic_location";
    public static final String FIREBASE_URL_QUESTIONS = FIREBASE_URL + "/questions";
    public static final String FIREBASE_URL_MCQ = FIREBASE_URL_QUESTIONS + "/mcq";
    public static final String FIREBASE_URL_FREE_RESPONSE = FIREBASE_URL_QUESTIONS + "/freeresponse";
    public static final String FIREBASE_URL_USER_SUBMISSION_STATS = FIREBASE_URL + "/submissionstats";



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


    // Notification related constants
    public static final String CAN_SHOW_NOTIFICATION = "ENABLE_NOTIFICATIONS";

    public static final String MOOD_REMINDER_TITLE = "From Mood Reminder: How are you feeling right now?";
    public static final String MISSED_DATA_GLUCOSE_READING_PROMPT_TITLE = "From Glucose Reading: We want to hear from you!";
    public static final String MISSED_DATA_INSULIN_ADMIN_PROMPT_TITLE = "From Insulin Admin: What are you upto?";
    public static final String MISSED_DATA_FOOD_PROMPT_TITLE = "From Food: What are you upto?";
    public static final String MOOD_ANNOTATION_TITLE = "From Mood Annotation: What happened to your mood?";


    //default queue size
    public static final int DEFAULT_QUEUE_SIZE = 100;

    //specific queue sizes
    public static final int LOCATION_QUEUE_SIZE = 100;
    public static final int IMAGE_QUEUE_SIZE = 100;
    public static final int MOOD_QUEUE_SIZE = 10;

    public static final int MOOD_STREAM_GENERATOR_UPDATE_FREQUENCY_MINUTES = 15;
    public static final int IMAGE_STREAM_GENERATOR_UPDATE_FREQUENCY_MINUTES = 30;
    public static final int FOOD_IMAGE_STREAM_GENERATOR_UPDATE_FREQUENCY_MINUTES = 180;

    public static final int MOOD_NOTIFICATION_EXPIRATION_TIME = 30 * 60 /* 30 minutes*/;
    public static final int MISSED_REPORT_NOTIFICATION_EXPIRATION_TIME = 60 * 60 /* 60 minutes*/;


    public static final String TAPPED_NOTIFICATION_ID_KEY = "TAPPED_NOTIFICATION_ID" ;
    public static final String SELECTED_LOCATIONS = "USERPREF_SELECTED_LOCATIONS";
    public static final String BUNDLE_KEY_FOR_QUESTIONNAIRE_ID = "QUESTIONNAIRE_ID";
}
