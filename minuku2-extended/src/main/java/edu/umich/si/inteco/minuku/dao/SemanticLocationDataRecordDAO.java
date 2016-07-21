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
import edu.umich.si.inteco.minuku.model.LocationDataRecord;
import edu.umich.si.inteco.minuku.model.SemanticLocationDataRecord;
import edu.umich.si.inteco.minukucore.dao.DAO;
import edu.umich.si.inteco.minukucore.dao.DAOException;
import edu.umich.si.inteco.minukucore.user.User;

/**
 * Created by neerajkumar on 7/21/16.
 */
public class SemanticLocationDataRecordDAO implements DAO<SemanticLocationDataRecord> {

    private String TAG = "SemanticLocationDataRecordDAO";
    private User myUser;
    private UUID uuID;


    @Override
    public void setDevice(User user, UUID uuid) {
        myUser = user;
        uuID = uuid;
    }

    @Override
    public void add(SemanticLocationDataRecord entity) throws DAOException {
        Log.d(TAG, "Adding location data record.");
        Firebase locationListRef = new Firebase(Constants.FIREBASE_URL_SEMANTIC_LOCATION)
                .child(myUser.getEmail())
                .child(new SimpleDateFormat("MMddyyyy").format(new Date()).toString());
        locationListRef.push().setValue((SemanticLocationDataRecord) entity);
    }

    @Override
    public void delete(SemanticLocationDataRecord entity) throws DAOException {
        Log.e(TAG, "Method not implemented.");
    }

    @Override
    public Future<List<SemanticLocationDataRecord>> getAll() throws DAOException {
        final SettableFuture<List<SemanticLocationDataRecord>> settableFuture =
                SettableFuture.create();
        Firebase locationListRef = new Firebase(Constants.FIREBASE_URL_SEMANTIC_LOCATION)
                .child(myUser.getEmail())
                .child(new SimpleDateFormat("MMddyyyy").format(new Date()).toString());

        locationListRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, SemanticLocationDataRecord> locationListMap =
                        (HashMap<String,SemanticLocationDataRecord>) dataSnapshot.getValue();
                List<SemanticLocationDataRecord> values = (List) locationListMap.values();
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
    public Future<List<SemanticLocationDataRecord>> getLast(int N) throws DAOException {
        Log.e(TAG, "Method not implemented. Returning null");
        return null;
    }

    @Override
    public void update(SemanticLocationDataRecord oldEntity, SemanticLocationDataRecord newEntity)
            throws DAOException {
        Log.e(TAG, "Method not implemented. Returning null");
    }
}
