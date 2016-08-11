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
import edu.umich.si.inteco.minuku_2.model.FoodImage;
import edu.umich.si.inteco.minuku_2.model.GlucoseReadingImage;
import edu.umich.si.inteco.minuku_2.preferences.ApplicationConstants;
import edu.umich.si.inteco.minukucore.dao.DAOException;
import edu.umich.si.inteco.minukucore.model.question.FreeResponse;

/**
 * Created by shriti on 7/31/16.
 */
public class FoodImageDAO extends AnnotatedImageDataRecordDAO<FoodImage> {

    private String TAG = "FoodImageDAO";
    public FoodImageDAO() {
        super(FoodImage.class);
    }

    @Override
    public void add(FoodImage entity) throws DAOException {
        Log.d(TAG, "Adding food image data record");
        Firebase imageListRef = new Firebase(ApplicationConstants.FIREBASE_URL_FOOD_IMAGES)
                .child(myUserEmail)
                .child(new SimpleDateFormat("MMddyyyy").format(new Date()).toString());
        imageListRef.push().setValue((FoodImage) entity);
    }

    @Override
    public Future<List<FoodImage>> getLast(int N) throws DAOException {
        final SettableFuture<List<FoodImage>> settableFuture = SettableFuture.create();
        final Date today = new Date();

        final List<FoodImage> lastNRecords = Collections.synchronizedList(
                new ArrayList<FoodImage>());

        String databaseURL = ApplicationConstants.FIREBASE_URL_FOOD_IMAGES;

        getLastNValues(N,
                myUserEmail,
                today,
                lastNRecords,
                settableFuture,
                databaseURL);

        return settableFuture;
    }

    @Override
    public Future<List<FoodImage>> getAll() throws DAOException {
        final SettableFuture<List<FoodImage>> settableFuture =
                SettableFuture.create();
        Firebase imageListRef = new Firebase(ApplicationConstants.FIREBASE_URL_FOOD_IMAGES)
                .child(myUserEmail)
                .child(new SimpleDateFormat("MMddyyyy").format(new Date()).toString());

        imageListRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, FoodImage> imageListMap =
                        (HashMap<String,FoodImage>) dataSnapshot.getValue();
                List<FoodImage> values = (List) imageListMap.values();
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
