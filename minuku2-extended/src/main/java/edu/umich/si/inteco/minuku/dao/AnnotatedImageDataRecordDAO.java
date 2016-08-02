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
import edu.umich.si.inteco.minuku.model.AnnotatedImageDataRecord;
import edu.umich.si.inteco.minukucore.dao.DAO;
import edu.umich.si.inteco.minukucore.dao.DAOException;
import edu.umich.si.inteco.minukucore.user.User;

/**
 * Created by shriti on 7/22/16.
 */
public class AnnotatedImageDataRecordDAO<T extends AnnotatedImageDataRecord> implements
        DAO<T> {

    private String TAG = "AnnotatedImageDataRecordDAO";
    private User myUser;
    private UUID uuID;

    @Override
    public void setDevice(User user, UUID uuid) {
        myUser = UserPreferences.getInstance().getUser();
        uuID = uuid;
    }

    @Override
    public void add(AnnotatedImageDataRecord entity) throws DAOException {
        Log.d(TAG, "Adding image data record");
        Firebase imageListRef = new Firebase(Constants.FIREBASE_URL_IMAGES)
                .child(myUser.getEmail())
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
        Firebase imageListRef = new Firebase(Constants.FIREBASE_URL_IMAGES)
                .child(myUser.getEmail())
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
        return null;
    }

    @Override
    public void update(AnnotatedImageDataRecord oldEntity, AnnotatedImageDataRecord newEntity) throws DAOException {
    }
}
