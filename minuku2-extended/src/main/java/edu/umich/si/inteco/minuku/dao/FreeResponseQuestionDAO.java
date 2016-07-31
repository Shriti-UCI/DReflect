package edu.umich.si.inteco.minuku.dao;

import android.util.Log;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.common.util.concurrent.SettableFuture;
import edu.umich.si.inteco.minuku.config.Constants;
import edu.umich.si.inteco.minuku.config.UserPreferences;
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
    private User myUser;
    private UUID uuID;

    @Override
    public void setDevice(User user, UUID uuid) {
        this.myUser = UserPreferences.getInstance().getUser();
        this.uuID = uuid;
    }

    @Override
    public void add(FreeResponse entity) throws DAOException {
        Log.d(TAG, "Adding reponse");
        Firebase FreeResponseListRef = new Firebase(Constants.FIREBASE_URL_QUESTIONS)
                .child(myUser.getEmail())
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
                .child(myUser.getEmail())
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
        return null;
    }

    @Override
    public void update(FreeResponse oldEntity, FreeResponse newEntity) throws DAOException {

    }

}
