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
import edu.umich.si.inteco.minuku.model.ImageDataRecord;
import edu.umich.si.inteco.minuku.model.LocationDataRecord;
import edu.umich.si.inteco.minukucore.dao.DAO;
import edu.umich.si.inteco.minukucore.dao.DAOException;
import edu.umich.si.inteco.minukucore.user.User;

/**
 * Created by shriti on 7/19/16.
 */
public class ImageDataRecordDAO implements DAO<ImageDataRecord> {

    private String TAG = "ImageDataRecordDAO";
    private User myUser;
    private UUID uuID;

    @Override
    public void setDevice(User user, UUID uuid) {
        myUser = user;
        uuID = uuid;
    }

    @Override
    public void add(ImageDataRecord entity) throws DAOException {
        Log.d(TAG, "Adding image data record");
        Firebase imageListRef = new Firebase(Constants.FIREBASE_URL_IMAGES)
                .child(myUser.getEmail())
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
                .child(myUser.getEmail())
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
        return null;
    }

    @Override
    public void update(ImageDataRecord oldEntity, ImageDataRecord newEntity) throws DAOException {
    }
}
