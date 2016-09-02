package edu.umich.si.inteco.minuku.dao;


import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.common.util.concurrent.SettableFuture;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Future;

import edu.umich.si.inteco.minuku.config.Constants;
import edu.umich.si.inteco.minuku.config.UserPreferences;
import edu.umich.si.inteco.minuku.logger.Log;
import edu.umich.si.inteco.minuku.model.UserSubmissionStats;
import edu.umich.si.inteco.minukucore.dao.DAO;
import edu.umich.si.inteco.minukucore.dao.DAOException;
import edu.umich.si.inteco.minukucore.user.User;

/**
 * Created by neerajkumar on 8/22/16.
 */
public class UserSubmissionStatsDAO implements DAO<UserSubmissionStats> {

    private String myUserEmail;
    private static String TAG = "UserSubmissionDAO";

    public UserSubmissionStatsDAO() {
        this.myUserEmail = UserPreferences.getInstance().getPreference(Constants.KEY_ENCODED_EMAIL);
    }

    @Override
    public void setDevice(User user, UUID uuid) {

    }

    @Override
    public void add(UserSubmissionStats entity) throws DAOException {
        throw new DAOException();
    }

    @Override
    public void delete(UserSubmissionStats entity) throws DAOException {
        throw new DAOException();
    }

    @Override
    public Future<List<UserSubmissionStats>> getAll() throws DAOException {
        return null;
    }

    @Override
    public Future<List<UserSubmissionStats>> getLast(int N) throws DAOException {
        return null;
    }

    @Override
    public void update(UserSubmissionStats oldEntity, UserSubmissionStats newEntity)
            throws DAOException {
        Log.d(TAG, "Adding note data record.");
        Firebase userStatsRef = new Firebase(Constants.FIREBASE_URL_USER_SUBMISSION_STATS)
                .child(myUserEmail)
                .child(new SimpleDateFormat("MMddyyyy").format(new Date()).toString());
        userStatsRef.setValue(newEntity);
    }

    public Future<UserSubmissionStats> get(){
        final SettableFuture<UserSubmissionStats> future = SettableFuture.create();
        Firebase userStatsRef = new Firebase(Constants.FIREBASE_URL_USER_SUBMISSION_STATS)
                .child(myUserEmail)
                .child(new SimpleDateFormat("MMddyyyy").format(new Date()).toString());

        userStatsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    UserSubmissionStats stats = dataSnapshot.getValue(UserSubmissionStats.class);
                    future.set(stats);
                } else {
                    future.set(new UserSubmissionStats());
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        return future;
    }
}
