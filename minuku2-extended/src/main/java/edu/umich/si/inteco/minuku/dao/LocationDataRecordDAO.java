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
import edu.umich.si.inteco.minuku.model.LocationDataRecord;
import edu.umich.si.inteco.minukucore.dao.*;
import edu.umich.si.inteco.minukucore.user.User;

/**
 * Created by shriti on 7/15/16.
 * Author: Neeraj Kumar
 */
public class LocationDataRecordDAO implements DAO<LocationDataRecord> {

    private String TAG = "LocationDataRecordDAO";
    private User myUser;
    private UUID uuID;


    @Override
    public void setDevice(User user, UUID uuid) {
        myUser = UserPreferences.getInstance().getUser();
        uuID = uuid;
    }

    @Override
    public void add(LocationDataRecord entity) throws DAOException {
        Log.d(TAG, "Adding location data record.");
        Firebase locationListRef = new Firebase(Constants.FIREBASE_URL_LOCATION)
                .child(myUser.getEmail())
                .child(new SimpleDateFormat("MMddyyyy").format(new Date()).toString());
        locationListRef.push().setValue((LocationDataRecord) entity);
    }

    @Override
    public void delete(LocationDataRecord entity) throws DAOException {
        // no-op for now.
    }

    @Override
    public Future<List<LocationDataRecord>> getAll() throws DAOException {
        final SettableFuture<List<LocationDataRecord>> settableFuture =
                SettableFuture.create();
        Firebase locationListRef = new Firebase(Constants.FIREBASE_URL_LOCATION)
                .child(myUser.getEmail())
                .child(new SimpleDateFormat("MMddyyyy").format(new Date()).toString());

        locationListRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, LocationDataRecord> locationListMap =
                        (HashMap<String,LocationDataRecord>) dataSnapshot.getValue();
                List<LocationDataRecord> values = (List) locationListMap.values();
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
    public Future<List<LocationDataRecord>> getLast(int N) throws DAOException {
        Log.e(TAG, "Method not implemented. Returning null");
        return null;
    }

    @Override
    public void update(LocationDataRecord oldEntity, LocationDataRecord newEntity)
            throws DAOException {
        Log.e(TAG, "Method not implemented. Returning null");
    }
}
