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

    private String TAG = "GlucoseReadingImageDAO";
    public GlucoseReadingImageDAO() {
        super(GlucoseReadingImage.class);
    }

    @Override
    public void add(GlucoseReadingImage entity) throws DAOException {
        Log.d(TAG, "Adding glucose reading image data record");
        Firebase imageListRef = new Firebase(ApplicationConstants.FIREBASE_URL_GLUCOSE_READING_IMAGES)
                .child(myUserEmail)
                .child(new SimpleDateFormat("MMddyyyy").format(new Date()).toString());
        imageListRef.push().setValue((GlucoseReadingImage) entity);
    }

    @Override
    public Future<List<GlucoseReadingImage>> getLast(int N) throws DAOException {
        final SettableFuture<List<GlucoseReadingImage>> settableFuture = SettableFuture.create();
        final Date today = new Date();

        final List<GlucoseReadingImage> lastNRecords = Collections.synchronizedList(
                new ArrayList<GlucoseReadingImage>());

        String databaseURL = ApplicationConstants.FIREBASE_URL_GLUCOSE_READING_IMAGES;

        getLastNValues(N,
                myUserEmail,
                today,
                lastNRecords,
                settableFuture,
                databaseURL);

        return settableFuture;
    }

    @Override
    public Future<List<GlucoseReadingImage>> getAll() throws DAOException {
        final SettableFuture<List<GlucoseReadingImage>> settableFuture =
                SettableFuture.create();
        Firebase imageListRef = new Firebase(ApplicationConstants.FIREBASE_URL_GLUCOSE_READING_IMAGES)
                .child(myUserEmail)
                .child(new SimpleDateFormat("MMddyyyy").format(new Date()).toString());

        imageListRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, GlucoseReadingImage> imageListMap =
                        (HashMap<String,GlucoseReadingImage>) dataSnapshot.getValue();
                List<GlucoseReadingImage> values = (List) imageListMap.values();
                settableFuture.set(values);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                settableFuture.set(null);
            }
        });
        return settableFuture;
    }
}
