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
import edu.umich.si.inteco.minuku_2.model.GlucoseReadingImage;
import edu.umich.si.inteco.minuku_2.model.InsulinAdminImage;
import edu.umich.si.inteco.minuku_2.preferences.ApplicationConstants;
import edu.umich.si.inteco.minukucore.dao.DAOException;

/**
 * Created by shriti on 7/31/16.
 */
public class InsulinAdminImageDAO extends AnnotatedImageDataRecordDAO<InsulinAdminImage> {

    private String TAG = "InsulinAdminImageDAO";
    public InsulinAdminImageDAO() {
        super(InsulinAdminImage.class);
    }

    @Override
    public void add(InsulinAdminImage entity) throws DAOException {
        Log.d(TAG, "Adding insulin admin image data record");
        Firebase imageListRef = new Firebase(ApplicationConstants.FIREBASE_URL_INSULIN_ADMIN_IMAGES)
                .child(myUserEmail)
                .child(new SimpleDateFormat("MMddyyyy").format(new Date()).toString());
        imageListRef.push().setValue((InsulinAdminImage) entity);
    }

    @Override
    public Future<List<InsulinAdminImage>> getLast(int N) throws DAOException {
        final SettableFuture<List<InsulinAdminImage>> settableFuture = SettableFuture.create();
        final Date today = new Date();

        final List<InsulinAdminImage> lastNRecords = Collections.synchronizedList(
                new ArrayList<InsulinAdminImage>());

        String databaseURL = ApplicationConstants.FIREBASE_URL_INSULIN_ADMIN_IMAGES;

        getLastNValues(N,
                myUserEmail,
                today,
                lastNRecords,
                settableFuture,
                databaseURL);

        return settableFuture;
    }

    @Override
    public Future<List<InsulinAdminImage>> getAll() throws DAOException {
        final SettableFuture<List<InsulinAdminImage>> settableFuture =
                SettableFuture.create();
        Firebase imageListRef = new Firebase(ApplicationConstants.FIREBASE_URL_INSULIN_ADMIN_IMAGES)
                .child(myUserEmail)
                .child(new SimpleDateFormat("MMddyyyy").format(new Date()).toString());

        imageListRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, InsulinAdminImage> imageListMap =
                        (HashMap<String,InsulinAdminImage>) dataSnapshot.getValue();
                List<InsulinAdminImage> values = (List) imageListMap.values();
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
