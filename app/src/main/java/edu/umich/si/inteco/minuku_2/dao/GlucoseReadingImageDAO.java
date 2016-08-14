package edu.umich.si.inteco.minuku_2.dao;

import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.common.util.concurrent.SettableFuture;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import edu.umich.si.inteco.minuku.config.Constants;
import edu.umich.si.inteco.minuku.dao.AnnotatedImageDataRecordDAO;
import edu.umich.si.inteco.minuku.model.AnnotatedImageDataRecord;
import edu.umich.si.inteco.minuku_2.model.GlucoseReadingImage;
import edu.umich.si.inteco.minuku_2.preferences.ApplicationConstants;
import edu.umich.si.inteco.minukucore.dao.DAOException;

/**
 * Created by shriti on 7/31/16.
 */
public class GlucoseReadingImageDAO extends AnnotatedImageDataRecordDAO<GlucoseReadingImage> {

    protected static String TAG = "FoodImageDAO";

    public GlucoseReadingImageDAO() {
        super(GlucoseReadingImage.class,
                ApplicationConstants.FIREBASE_URL_GLUCOSE_READING_IMAGES);
    }

}
