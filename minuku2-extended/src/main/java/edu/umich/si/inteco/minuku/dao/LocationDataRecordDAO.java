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
import edu.umich.si.inteco.minukucore.dao.*;
import edu.umich.si.inteco.minukucore.model.question.FreeResponse;
import edu.umich.si.inteco.minukucore.user.User;

/**
 * Created by shriti on 7/15/16.
 * Author: Neeraj Kumar
 */
public class LocationDataRecordDAO implements DAO<LocationDataRecord> {

    private String TAG = "LocationDataRecordDAO";
    private String myUserEmail;
    private UUID uuID;

    public LocationDataRecordDAO() {
        myUserEmail = UserPreferences.getInstance().getPreference(Constants.KEY_ENCODED_EMAIL);
    }

    @Override
    public void setDevice(User user, UUID uuid) {

    }

    @Override
    public void add(LocationDataRecord entity) throws DAOException {
        Log.d(TAG, "Adding location data record.");
        Firebase locationListRef = new Firebase(Constants.FIREBASE_URL_LOCATION)
                .child(myUserEmail)
                .child(new SimpleDateFormat("MMddyyyy").format(new Date()).toString());
        locationListRef.push().setValue((LocationDataRecord) entity);
    }

    @Override
    public void delete(LocationDataRecord entity) throws DAOException {
        // no-op for now.
    }

    @Override
    public Future<List<LocationDataRecord>> getAll() throws DAOException {
        final SettableFuture<List<LocationDataRecord>> settableFuture =
                SettableFuture.create();
        Firebase locationListRef = new Firebase(Constants.FIREBASE_URL_LOCATION)
                .child(myUserEmail)
                .child(new SimpleDateFormat("MMddyyyy").format(new Date()).toString());

        locationListRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, LocationDataRecord> locationListMap =
                        (HashMap<String,LocationDataRecord>) dataSnapshot.getValue();
                List<LocationDataRecord> values = (List) locationListMap.values();
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
    public Future<List<LocationDataRecord>> getLast(int N) throws DAOException {
        final SettableFuture<List<LocationDataRecord>> settableFuture = SettableFuture.create();
        final Date today = new Date();

        final List<LocationDataRecord> lastNRecords = Collections.synchronizedList(
                new ArrayList<LocationDataRecord>());

        getLastNValues(N,
                myUserEmail,
                today,
                lastNRecords,
                settableFuture);

        return settableFuture;
    }

    @Override
    public void update(LocationDataRecord oldEntity, LocationDataRecord newEntity)
            throws DAOException {
        Log.e(TAG, "Method not implemented. Returning null");
    }

    private final void getLastNValues(final int N,
                                      final String userEmail,
                                      final Date someDate,
                                      final List<LocationDataRecord> synchronizedListOfRecords,
                                      final SettableFuture settableFuture) {
        Firebase firebaseRef = new Firebase(Constants.FIREBASE_URL_LOCATION)
                .child(userEmail)
                .child(new SimpleDateFormat("MMddyyyy").format(someDate).toString());

        if(N <= 0) {
            /* TODO(neerajkumar): Get this f***up fixed! */

            // The first element in the list is actually the last in the database.
            // Reverse the list before setting the future with a result.
            Collections.reverse(synchronizedListOfRecords);

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
                    /* TODO(neerajkumar): Get this f***up fixed! */

                    // The first element in the list is actually the last in the database.
                    // Reverse the list before setting the future with a result.
                    Collections.reverse(synchronizedListOfRecords);

                    settableFuture.set(synchronizedListOfRecords);
                    return;
                }

                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    synchronizedListOfRecords.add(snapshot.getValue(LocationDataRecord.class));
                    newN--;
                }
                Date newDate = new Date(someDate.getTime() - 26 * 60 * 60 * 1000); /* -1 Day */
                getLastNValues(newN,
                        userEmail,
                        newDate,
                        synchronizedListOfRecords,
                        settableFuture);
            }



            @Override
            public void onCancelled(FirebaseError firebaseError) {

                /* TODO(neerajkumar): Get this f***up fixed! */

                // The first element in the list is actually the last in the database.
                // Reverse the list before setting the future with a result.
                Collections.reverse(synchronizedListOfRecords);

                // This would mean that the firebase ref does not exist thereby meaning that
                // the number of entries for all dates are over before we could get the last N
                // results
                settableFuture.set(synchronizedListOfRecords);
            }
        });
    }

}
