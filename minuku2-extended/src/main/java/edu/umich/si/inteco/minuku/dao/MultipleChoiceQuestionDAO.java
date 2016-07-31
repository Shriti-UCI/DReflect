package edu.umich.si.inteco.minuku.dao;

import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.common.util.concurrent.SettableFuture;

import java.text.SimpleDateFormat;
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
    private User myUser;
    private UUID uuID;

    @Override
    public void setDevice(User user, UUID uuid) {
        this.myUser = UserPreferences.getInstance().getUser();
        this.uuID = uuid;
    }

    @Override
    public void add(MultipleChoice entity) throws DAOException {
        Log.d(TAG, "Adding reponse");
        Firebase multipleChoiceListRef = new Firebase(Constants.FIREBASE_URL_QUESTIONS)
                .child(myUser.getEmail())
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
                .child(myUser.getEmail())
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
        return null;
    }

    @Override
    public void update(MultipleChoice oldEntity, MultipleChoice newEntity) throws DAOException {

    }

}
