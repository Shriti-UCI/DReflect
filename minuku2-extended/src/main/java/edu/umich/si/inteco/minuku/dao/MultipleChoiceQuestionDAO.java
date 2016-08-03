package edu.umich.si.inteco.minuku.dao;

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
import java.util.UUID;
import java.util.concurrent.Future;

import edu.umich.si.inteco.minuku.config.Constants;
import edu.umich.si.inteco.minuku.config.UserPreferences;
import edu.umich.si.inteco.minukucore.dao.DAO;
import edu.umich.si.inteco.minukucore.dao.DAOException;
import edu.umich.si.inteco.minukucore.model.question.FreeResponse;
import edu.umich.si.inteco.minukucore.model.question.MultipleChoice;
import edu.umich.si.inteco.minukucore.user.User;

/**
 * Created by shriti on 7/31/16.
 */
public class MultipleChoiceQuestionDAO implements DAO<MultipleChoice> {

    private String TAG = "MultipleChoiceQuestionDAO";
    private String myUserEmail;
    private UUID uuID;

    public MultipleChoiceQuestionDAO() {
        myUserEmail = UserPreferences.getInstance().getPreference(Constants.KEY_ENCODED_EMAIL);

    }

    @Override
    public void setDevice(User user, UUID uuid) {
    }

    @Override
    public void add(MultipleChoice entity) throws DAOException {
        Log.d(TAG, "Adding reponse");
        Firebase multipleChoiceListRef = new Firebase(Constants.FIREBASE_URL_QUESTIONS)
                .child(myUserEmail)
                .child(new SimpleDateFormat("MMddyyyy").format(new Date()).toString());
        multipleChoiceListRef.push().setValue((MultipleChoice) entity);
    }

    @Override
    public void delete(MultipleChoice entity) throws DAOException {

    }

    @Override
    public Future<List<MultipleChoice>> getAll() throws DAOException {
        final SettableFuture<List<MultipleChoice>> settableFuture =
                SettableFuture.create();
        Firebase multipleChoiceListRef = new Firebase(Constants.FIREBASE_URL_QUESTIONS)
                .child(myUserEmail)
                .child(new SimpleDateFormat("MMddyyyy").format(new Date()).toString());

        multipleChoiceListRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, MultipleChoice> multipleChoiceListMap =
                        (HashMap<String,MultipleChoice>) dataSnapshot.getValue();
                List<MultipleChoice> values = (List) multipleChoiceListMap.values();
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
    public Future<List<MultipleChoice>> getLast(int N) throws DAOException {
        /*final SettableFuture<List<MultipleChoice>> settableFuture = SettableFuture.create();
        final Date today = new Date();

        final List<MultipleChoice> lastNRecords = Collections.synchronizedList(
                new ArrayList<MultipleChoice>());

        getLastNValues(N,
                myUserEmail,
                today,
                lastNRecords,
                settableFuture);

        return settableFuture;*/
        return null;
    }

    @Override
    public void update(MultipleChoice oldEntity, MultipleChoice newEntity) throws DAOException {

    }

    private final void getLastNValues(final int N,
                                      final String userEmail,
                                      final Date someDate,
                                      final List<MultipleChoice> synchronizedListOfRecords,
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
                    synchronizedListOfRecords.add(snapshot.getValue(MultipleChoice.class));
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
