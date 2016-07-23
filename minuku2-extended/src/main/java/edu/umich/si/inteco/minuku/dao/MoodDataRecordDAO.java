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
import edu.umich.si.inteco.minuku.model.ImageDataRecord;
import edu.umich.si.inteco.minuku.model.MoodDataRecord;
import edu.umich.si.inteco.minukucore.dao.DAO;
import edu.umich.si.inteco.minukucore.dao.DAOException;
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
    public Future<List<MoodDataRecord>> getLast(int N) throws DAOException {
        return null;
    }

    @Override
    public void update(MoodDataRecord oldEntity, MoodDataRecord newEntity) throws DAOException {

    }
}
