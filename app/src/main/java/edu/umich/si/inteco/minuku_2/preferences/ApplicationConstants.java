package edu.umich.si.inteco.minuku_2.preferences;

import edu.umich.si.inteco.minuku.config.Constants;
import edu.umich.si.inteco.minuku_2.BuildConfig;

/**
 * Created by shriti on 8/2/16.
 * Will contain all the constants specific to application
 * ToDo Clean the Constants in extended library and put all the app specific constants here.
 *
 */
public class ApplicationConstants {

    /** public static final String FIREBASE_URL_GLUCOSE_READING_IMAGES = Constants.FIREBASE_URL_IMAGES + "/glucose_readings";
    public static final String FIREBASE_URL_INSULIN_ADMIN_IMAGES = Constants.FIREBASE_URL_IMAGES + "/insulin_admin";
    public static final String FIREBASE_URL_FOOD_IMAGES = Constants.FIREBASE_URL_IMAGES + "/food";
     **/

    public static final String IMAGE_TYPE_GLUCOSE_READIMG = "GLUCOSE_READINGS";
    public static final String IMAGE_TYPE_INSULIN_SHOT = "INSULIN_SHOT";
    public static final String IMAGE_TYPE_FOOD = "FOOD";
    public static final String IMAGE_TYPE_OTHERS = "OTHER_IMAGES";
    public static final String IMAGE_TYPE_GALLERY_UPLOAD = "GALLERY_UPLOAD";


    public static final String NOTIFICATION_CATEGORY_MISSED_ACTIVITY = "MISSED_IMAGE_REPORT_NOTIF";
    public static final String NOTIFICATION_CATEGORY_MOOD_REPORT = "MOOD_REPORT_NOTIF";
    public static final String NOTIFICATION_CATEGORY_MOOD_ANNOTATION = "MOOD_ANNOTATION_NOTIF";

    public static final int MIN_REPORTS_TO_GET_REWARD = 2;

    public static final String EMAIL_FROM = "dstudio.umich@gmail.com";
    public static final String EMAIL_FROM_PASSWORD = BuildConfig.DSTUDIO_MAIL_PASSWORD;
    public static final String EMAIL_TO = "shritir@uci.edu";

}


