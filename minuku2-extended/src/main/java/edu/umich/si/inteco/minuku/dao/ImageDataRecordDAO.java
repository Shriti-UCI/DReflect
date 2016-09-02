package edu.umich.si.inteco.minuku.dao;


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
import java.util.UUID;
import java.util.concurrent.Future;

import edu.umich.si.inteco.minuku.config.Constants;
import edu.umich.si.inteco.minuku.config.UserPreferences;
import edu.umich.si.inteco.minuku.logger.Log;
import edu.umich.si.inteco.minuku.model.ImageDataRecord;
import edu.umich.si.inteco.minukucore.dao.DAO;
import edu.umich.si.inteco.minukucore.dao.DAOException;
import edu.umich.si.inteco.minukucore.user.User;

/**
 * Created by shriti on 7/19/16.
 */
public class ImageDataRecordDAO implements DAO<ImageDataRecord> {

    private String TAG = "ImageDataRecordDAO";
    private String myUserEmail;
    private UUID uuID;

    public ImageDataRecordDAO() {
        myUserEmail = UserPreferences.getInstance().getPreference(Constants.KEY_ENCODED_EMAIL);
    }

    @Override
    public void setDevice(User user, UUID uuid) {

    }

    @Override
    public void add(ImageDataRecord entity) throws DAOException {
        Log.d(TAG, "Adding image data record");
        Firebase imageListRef = new Firebase(Constants.FIREBASE_URL_IMAGES)
                .child(myUserEmail)
                .child(new SimpleDateFormat("MMddyyyy").format(new Date()).toString());
        imageListRef.push().setValue((ImageDataRecord) entity);
    }

    @Override
    public void delete(ImageDataRecord entity) throws DAOException {
        //do nothing for now
    }

    @Override
    public Future<List<ImageDataRecord>> getAll() throws DAOException {
        final SettableFuture<List<ImageDataRecord>> settableFuture =
                SettableFuture.create();
        Firebase imageListRef = new Firebase(Constants.FIREBASE_URL_IMAGES)
                .child(myUserEmail)
                .child(new SimpleDateFormat("MMddyyyy").format(new Date()).toString());

        imageListRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, ImageDataRecord> imageListMap =
                        (HashMap<String,ImageDataRecord>) dataSnapshot.getValue();
                List<ImageDataRecord> values = (List) imageListMap.values();
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
    public Future<List<ImageDataRecord>> getLast(int N) throws DAOException {
        final SettableFuture<List<ImageDataRecord>> settableFuture = SettableFuture.create();
        final Date today = new Date();

        final List<ImageDataRecord> lastNRecords = Collections.synchronizedList(
                new ArrayList<ImageDataRecord>());

        getLastNValues(N,
                myUserEmail,
                today,
                lastNRecords,
                settableFuture);

        return settableFuture;    }

    @Override
    public void update(ImageDataRecord oldEntity, ImageDataRecord newEntity) throws DAOException {
    }

    private final void getLastNValues(final int N,
                                      final String userEmail,
                                      final Date someDate,
                                      final List<ImageDataRecord> synchronizedListOfRecords,
                                      final SettableFuture settableFuture) {
        Firebase firebaseRef = new Firebase(Constants.FIREBASE_URL_IMAGES)
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
                    synchronizedListOfRecords.add(snapshot.getValue(ImageDataRecord.class));
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

                // This would mean that the firebase ref does not exist thereby meaning that
                // the number of entries for all dates are over before we could get the last N
                // results
                settableFuture.set(synchronizedListOfRecords);
            }
        });
    }

}
