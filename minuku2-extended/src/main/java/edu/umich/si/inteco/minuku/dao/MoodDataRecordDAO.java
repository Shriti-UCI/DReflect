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
import edu.umich.si.inteco.minukucore.stream.Stream;
import edu.umich.si.inteco.minukucore.user.User;

/**
 * Created by shriti on 7/21/16.
 */
public class MoodDataRecordDAO implements DAO<MoodDataRecord> {

    String TAG = "MoodDataRecordDAO";
    private User myUser;
    private UUID uuID;

    @Override
    public void setDevice(User user, UUID uuid) {
        myUser = UserPreferences.getInstance().getUser();
        uuID = uuid;
    }

    @Override
    public void add(MoodDataRecord entity) throws DAOException {
        Log.d(TAG, "Adding mood data record");
        Firebase imageListRef = new Firebase(Constants.FIREBASE_URL_MOODS)
                .child(myUser.getEmail())
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
                .child(myUser.getEmail())
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

        final List<MoodDataRecord> lastNRecords = Collections.synchronizedList(
                new ArrayList<MoodDataRecord>());

        MoodDataRecordDAO.getLastNValues(N, myUser.getEmail(), today, lastNRecords, settableFuture);

        return settableFuture;
    }

    @Override
    public void update(MoodDataRecord oldEntity, MoodDataRecord newEntity) throws DAOException {

    }

    private static final void getLastNValues(final int N,
                                             final String userEmail,
                                             final Date someDate,
                                             final List<MoodDataRecord> synchronizedListOfRecords,
                                             final SettableFuture settableFuture) {
        Firebase firebaseRef = new Firebase(Constants.FIREBASE_URL_MOODS)
                .child(userEmail)
                .child(new SimpleDateFormat("MMddyyyy").format(someDate).toString());

        firebaseRef.limitToLast(N).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get all the values in firebase list as map
                Map<String, MoodDataRecord> moodListMap =
                        (HashMap<String,MoodDataRecord>) dataSnapshot.getValue();

                // Add values in the map as a list to the list of results.
                synchronizedListOfRecords.addAll((List)moodListMap.values());

                // if after adding records for "Today" (moving day for each loop),
                // the number of records in list exceed N, set the future.
                if(synchronizedListOfRecords.size() > N) {
                    settableFuture.set(Lists.reverse(synchronizedListOfRecords));
                } else {
                    int newN = N - moodListMap.values().size();
                    Date newDate = new Date(someDate.getTime() - 26 * 60 * 60 * 1000); /* -1 Day */
                    MoodDataRecordDAO.getLastNValues(newN,
                            userEmail,
                            newDate,
                            synchronizedListOfRecords,
                            settableFuture);
                }
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
