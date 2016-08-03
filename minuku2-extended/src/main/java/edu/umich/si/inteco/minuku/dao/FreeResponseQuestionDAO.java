package edu.umich.si.inteco.minuku.dao;

import android.util.Log;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.SettableFuture;
import edu.umich.si.inteco.minuku.config.Constants;
import edu.umich.si.inteco.minuku.config.UserPreferences;
import edu.umich.si.inteco.minuku.model.AnnotatedImageDataRecord;
import edu.umich.si.inteco.minuku.model.LocationDataRecord;
import edu.umich.si.inteco.minukucore.dao.DAO;
import edu.umich.si.inteco.minukucore.dao.DAOException;
import edu.umich.si.inteco.minukucore.model.question.FreeResponse;
import edu.umich.si.inteco.minukucore.user.User;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Future;

/**
 * Created by shriti on 7/28/16.
 */
public class FreeResponseQuestionDAO implements DAO<FreeResponse> {

    private String TAG = "FreeResponseQuestionDAO";
    private String myUserEmail;
    private UUID uuID;


    public FreeResponseQuestionDAO() {
        myUserEmail = UserPreferences.getInstance().getPreference(Constants.KEY_ENCODED_EMAIL);
    }

    @Override
    public void setDevice(User user, UUID uuid) {
    }

    @Override
    public void add(FreeResponse entity) throws DAOException {
        Log.d(TAG, "Adding reponse");
        Firebase FreeResponseListRef = new Firebase(Constants.FIREBASE_URL_QUESTIONS)
                .child(myUserEmail)
                .child(new SimpleDateFormat("MMddyyyy").format(new Date()).toString());
        FreeResponseListRef.push().setValue((FreeResponse) entity);
    }

    @Override
    public void delete(FreeResponse entity) throws DAOException {

    }

    @Override
    public Future<List<FreeResponse>> getAll() throws DAOException {
        final SettableFuture<List<FreeResponse>> settableFuture =
                SettableFuture.create();
        Firebase freeResponseListRef = new Firebase(Constants.FIREBASE_URL_QUESTIONS)
                .child(myUserEmail)
                .child(new SimpleDateFormat("MMddyyyy").format(new Date()).toString());

        freeResponseListRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, FreeResponse> freeResponseListMap =
                        (HashMap<String,FreeResponse>) dataSnapshot.getValue();
                List<FreeResponse> values = (List) freeResponseListMap.values();
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
    public Future<List<FreeResponse>> getLast(int N) throws DAOException {

        /*final SettableFuture<List<FreeResponse>> settableFuture = SettableFuture.create();
        final Date today = new Date();

        final List<FreeResponse> lastNRecords = Collections.synchronizedList(
                new ArrayList<FreeResponse>());

        getLastNValues(N,
                myUserEmail,
                today,
                lastNRecords,
                settableFuture);

        return settableFuture;*/
        return null;
    }

    @Override
    public void update(FreeResponse oldEntity, FreeResponse newEntity) throws DAOException {

    }

    private final void getLastNValues(final int N,
                                      final String userEmail,
                                      final Date someDate,
                                      final List<FreeResponse> synchronizedListOfRecords,
                                      final SettableFuture settableFuture) {
        Firebase firebaseRef = new Firebase(Constants.FIREBASE_URL_QUESTIONS)
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
                    synchronizedListOfRecords.add(snapshot.getValue(FreeResponse.class));
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
