package edu.umich.si.inteco.minuku.dao;

import com.firebase.client.Firebase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import edu.umich.si.inteco.minuku.config.Constants;
import edu.umich.si.inteco.minuku.model.LocationDataRecord;
import edu.umich.si.inteco.minukucore.dao.*;
import edu.umich.si.inteco.minukucore.model.DataRecord;
import edu.umich.si.inteco.minukucore.user.User;

/**
 * Created by shriti on 7/15/16.
 */
public class LocationDataRecordDAO implements DAO{

    private String url = Constants.FIREBASE_URL_LOCATION;
    private User myUser;


    @Override
    public void setDevice(User user, UUID uuid) {
        myUser = user;
    }

    @Override
    public void add(DataRecord entity) throws DAOException {
        FirebaseClient.getInstance();
        Firebase locationListRef = new Firebase(Constants.FIREBASE_URL_LOCATION)
                .child(myUser.getEmail())
                .child(new SimpleDateFormat("MMddyyyy").format(new Date()).toString());
        locationListRef.push().setValue((LocationDataRecord) entity);
    }

    @Override
    public void delete(DataRecord entity) throws DAOException {
        // no-op for now.
    }

    @Override
    public List getAll() throws DAOException {
        return null;
    }

    @Override
    public List getLast(int N) throws DAOException {
        return null;
    }

    @Override
    public void update(DataRecord oldEntity, DataRecord newEntity) throws DAOException {

    }
}
