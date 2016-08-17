package edu.umich.si.inteco.minuku.dao;

import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.SettableFuture;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Future;

import edu.umich.si.inteco.minuku.config.Constants;
import edu.umich.si.inteco.minuku.config.UserPreferences;
import edu.umich.si.inteco.minuku.model.AnnotatedImageDataRecord;
import edu.umich.si.inteco.minuku.model.LocationDataRecord;
import edu.umich.si.inteco.minuku.model.MoodDataRecord;
import edu.umich.si.inteco.minukucore.dao.DAO;
import edu.umich.si.inteco.minukucore.dao.DAOException;
import edu.umich.si.inteco.minukucore.user.User;

/**
 * Created by shriti on 7/22/16.
 */
public class AnnotatedImageDataRecordDAO<T extends AnnotatedImageDataRecord> implements
        DAO<T> {

    protected static String TAG = "AnnotatedImageDataRecordDAO";
    protected String myUserEmail;
    protected String mFirebaseUrl;
    protected UUID uuID;
    protected Class<T> mDataRecordType;

    public AnnotatedImageDataRecordDAO(Class aDataRecordType) {
        myUserEmail = UserPreferences.getInstance().getPreference(Constants.KEY_ENCODED_EMAIL);
        this.mDataRecordType = aDataRecordType;
        this.mFirebaseUrl = Constants.FIREBASE_URL_IMAGES;
    }

    public AnnotatedImageDataRecordDAO(Class aDataRecordType,
                                       String aFirebaseUrl) {
        this(aDataRecordType);
        this.mFirebaseUrl = aFirebaseUrl;
    }

    @Override
    public void setDevice(User user, UUID uuid) {

    }

    @Override
    public void add(AnnotatedImageDataRecord entity) throws DAOException {
        Log.d(TAG, "Adding image data record");
        Firebase imageListRef = new Firebase(this.mFirebaseUrl)
                .child(myUserEmail)
                .child(new SimpleDateFormat("MMddyyyy").format(new Date()).toString());
        imageListRef.push().setValue((AnnotatedImageDataRecord) entity);
    }

    @Override
    public void delete(AnnotatedImageDataRecord entity) throws DAOException {
        //do nothing for now
    }

    @Override
    public Future<List<T>> getAll() throws DAOException {
        final SettableFuture<List<T>> settableFuture =
                SettableFuture.create();
        Firebase imageListRef = new Firebase(this.mFirebaseUrl)
                .child(myUserEmail)
                .child(new SimpleDateFormat("MMddyyyy").format(new Date()).toString());

        imageListRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, T> imageListMap =
                        (HashMap<String,T>) dataSnapshot.getValue();
                List<T> values = (List) imageListMap.values();
                settableFuture.set(values);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                settableFuture.set(null);
            }
        });
        return settableFuture;
    }

    @Override
    public Future<List<T>> getLast(int N) throws DAOException {
        final SettableFuture<List<T>> settableFuture = SettableFuture.create();
        final Date today = new Date();

        final List<T> lastNRecords = Collections.synchronizedList(
                new ArrayList<T>());

        getLastNValues(N,
                myUserEmail,
                today,
                lastNRecords,
                settableFuture,
                this.mFirebaseUrl);

        return settableFuture;
    }

    @Override
    public void update(AnnotatedImageDataRecord oldEntity, AnnotatedImageDataRecord newEntity) throws DAOException {
    }

    public final void getLastNValues(final int N,
                                     final String userEmail,
                                     final Date someDate,
                                     final List<T> synchronizedListOfRecords,
                                     final SettableFuture settableFuture,
                                     final String databaseURL) {
        Firebase firebaseRef = new Firebase(databaseURL)
                .child(userEmail)
                .child(new SimpleDateFormat("MMddyyyy").format(someDate).toString());

        Log.d(TAG, "Checking the value of N "+ N);

        if(N <= 0) {
            settableFuture.set(synchronizedListOfRecords);
            return;
        }

        firebaseRef.limitToLast(N).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int newN = N;

                // dataSnapshot.exists returns false when the
                // <root>/<datarecord>/<userEmail>/<date> location does not exist.
                // What it means is that no entries were added for this date, i.e.
                // all the historic information has been exhausted.
                if(!dataSnapshot.exists()) {
                    settableFuture.set(synchronizedListOfRecords);
                    return;
                }

                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    synchronizedListOfRecords.add(snapshot.getValue(mDataRecordType));
                    newN--;
                }
                Date newDate = new Date(someDate.getTime() - 26 * 60 * 60 * 1000); /* -1 Day */
                getLastNValues(newN,
                        userEmail,
                        newDate,
                        synchronizedListOfRecords,
                        settableFuture,
                        databaseURL);
            }


            @Override
            public void onCancelled(FirebaseError firebaseError) {

                // This would mean that the firebase ref does not exist thereby meaning that
                // the number of entries for all dates are over before we could get the last N
                // results
                settableFuture.set(synchronizedListOfRecords);
            }
        });
    }
}
