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
    public static final String FIREBASE_URL_DIABETESLOG = FIREBASE_URL + "/diabetes_log";
    public static final String FIREBASE_URL_EOD_QUESTION_ANSWER = FIREBASE_URL + "/EOD_question_answer";



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

    public static final String MOOD_REMINDER_TITLE = "How are you feeling right now?";
    public static final String MOOD_REMINDER_MESSAGE = "Tap here to report your mood.";

    public static final String MOOD_ANNOTATION_TITLE = "Tell us more about your mood";
    public static final String MOOD_ANNOTATION_MESSAGE = "Tap here answer a quick question.";

    public static final String MISSED_ACTIVITY_DATA_PROMPT_TITLE = "We want to hear from you!";
    public static final String MISSED_ACTIVITY_DATA_PROMPT_MESSAGE = "Tap here to answer some questions.";





    //default queue size
    public static final int DEFAULT_QUEUE_SIZE = 20;

    //specific queue sizes
    public static final int LOCATION_QUEUE_SIZE = 50;
    public static final int IMAGE_QUEUE_SIZE = 20;
    public static final int MOOD_QUEUE_SIZE = 20;

    public static final int MOOD_STREAM_GENERATOR_UPDATE_FREQUENCY_MINUTES = 15;
    public static final int IMAGE_STREAM_GENERATOR_UPDATE_FREQUENCY_MINUTES = 30;
    public static final int FOOD_IMAGE_STREAM_GENERATOR_UPDATE_FREQUENCY_MINUTES = 180;

    public static final int MOOD_NOTIFICATION_EXPIRATION_TIME = 30 * 60 /* 30 minutes*/;
    public static final int MISSED_REPORT_NOTIFICATION_EXPIRATION_TIME = 60 * 60 /* 60 minutes*/;


    public static final String TAPPED_NOTIFICATION_ID_KEY = "TAPPED_NOTIFICATION_ID" ;
    public static final String SELECTED_LOCATIONS = "USERPREF_SELECTED_LOCATIONS";
    public static final String BUNDLE_KEY_FOR_QUESTIONNAIRE_ID = "QUESTIONNAIRE_ID";
    public static final String BUNDLE_KEY_FOR_NOTIFICATION_SOURCE = "NOTIFICATION_SOURCE";
    public static final String APP_NAME = "DReflect";
    public static final String RUNNING_APP_DECLARATION = APP_NAME + " is running in the background";
    public static final long INTERNAL_LOCATION_UPDATE_FREQUENCY = 10 * 60 * 1000;
    public static final float LOCATION_MINUMUM_DISPLACEMENT_UPDATE_THRESHOLD = 150 ;

    public static final String FOOD_NOTIFICATION_SOURCE = "FOOD";
    public static final String GLUCOSE_READING_NOTIFICATION_SOURCE = "GLUCOSE_READING";
    public static final String INSULIN_SHOT_NOTIFICATION_SOURCE = "INSULIN_SHOT";


}
