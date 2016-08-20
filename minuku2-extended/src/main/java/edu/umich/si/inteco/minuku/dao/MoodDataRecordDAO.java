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
import edu.umich.si.inteco.minuku.manager.MinukuStreamManager;
import edu.umich.si.inteco.minuku.model.ImageDataRecord;
import edu.umich.si.inteco.minuku.model.MoodDataRecord;
import edu.umich.si.inteco.minukucore.dao.DAO;
import edu.umich.si.inteco.minukucore.dao.DAOException;
import edu.umich.si.inteco.minukucore.model.question.FreeResponse;
import edu.umich.si.inteco.minukucore.stream.Stream;
import edu.umich.si.inteco.minukucore.user.User;

/**
 * Created by shriti on 7/21/16.
 */
public class MoodDataRecordDAO implements DAO<MoodDataRecord> {

    String TAG = "MoodDataRecordDAO";
    private String myUserEmail;
    private UUID uuID;

    public MoodDataRecordDAO() {
        myUserEmail = UserPreferences.getInstance().getPreference(Constants.KEY_ENCODED_EMAIL);
    }

    @Override
    public void setDevice(User user, UUID uuid) {

    }

    @Override
    public void add(MoodDataRecord entity) throws DAOException {
        Log.d(TAG, "Adding mood data record");
        Firebase imageListRef = new Firebase(Constants.FIREBASE_URL_MOODS)
                .child(myUserEmail)
                .child(new SimpleDateFormat("MMddyyyy").format(new Date()).toString());
        imageListRef.push().setValue((MoodDataRecord) entity);
    }

    @Override
    public void delete(MoodDataRecord entity) throws DAOException {
        //do nothing for now
    }

    @Override
    public Future<List<MoodDataRecord>> getAll() throws DAOException {
        final SettableFuture<List<MoodDataRecord>> settableFuture =
                SettableFuture.create();
        Firebase moodListRef = new Firebase(Constants.FIREBASE_URL_MOODS)
                .child(myUserEmail)
                .child(new SimpleDateFormat("MMddyyyy").format(new Date()).toString());

        moodListRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, MoodDataRecord> moodListMap =
                        (HashMap<String,MoodDataRecord>) dataSnapshot.getValue();
                List<MoodDataRecord> values = (List) moodListMap.values();
                settableFuture.set(values);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                settableFuture.set(null);
            }
        });
        return settableFuture;    }

    @Override
    public Future<List<MoodDataRecord>> getLast(final int N) throws DAOException {
        final SettableFuture<List<MoodDataRecord>> settableFuture = SettableFuture.create();
        final Date today = new Date();

        final List<MoodDataRecord> lastNRecords = new ArrayList<MoodDataRecord>();

        getLastNValues(N,
                myUserEmail,
                today,
                lastNRecords,
                settableFuture);

        return settableFuture;
    }

    @Override
    public void update(MoodDataRecord oldEntity, MoodDataRecord newEntity) throws DAOException {

    }

    private final void getLastNValues(final int N,
                                             final String userEmail,
                                             final Date someDate,
                                             final List<MoodDataRecord> synchronizedListOfRecords,
                                             final SettableFuture settableFuture) {
        Firebase firebaseRef = new Firebase(Constants.FIREBASE_URL_MOODS)
                .child(userEmail)
                .child(new SimpleDateFormat("MMddyyyy").format(someDate).toString());

        Log.d(TAG, "Checking the value of N "+ N);

        if(N <= 0) {
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
                    // The first element in the list is actually the last in the database.
                    // Reverse the list before setting the future with a result.
                    Collections.reverse(synchronizedListOfRecords);


                    settableFuture.set(synchronizedListOfRecords);
                    return;
                }

                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    synchronizedListOfRecords.add(snapshot.getValue(MoodDataRecord.class));
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
