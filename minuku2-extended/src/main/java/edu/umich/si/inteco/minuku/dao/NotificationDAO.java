package edu.umich.si.inteco.minuku.dao;

import android.util.Log;

import com.firebase.client.Firebase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Future;

import edu.umich.si.inteco.minuku.config.Constants;
import edu.umich.si.inteco.minuku.config.UserPreferences;
import edu.umich.si.inteco.minukucore.dao.DAO;
import edu.umich.si.inteco.minukucore.dao.DAOException;
import edu.umich.si.inteco.minukucore.event.ShowNotificationEvent;
import edu.umich.si.inteco.minukucore.user.User;

/**
 * Created by neerajkumar on 8/21/16.
 */
public class NotificationDAO implements DAO<ShowNotificationEvent> {

    public static String TAG = "NotificationDAO";
    private String myUserEmail;

    public NotificationDAO() {
        myUserEmail = UserPreferences.getInstance().getPreference(Constants.KEY_ENCODED_EMAIL);
    }

    @Override
    public void setDevice(User user, UUID uuid) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void add(ShowNotificationEvent entity) throws DAOException {
        Log.d(TAG, "Adding note data record.");
        Firebase locationListRef = new Firebase(Constants.FIREBASE_URL_NOTIFICATIONS)
                .child(myUserEmail)
                .child(new SimpleDateFormat("MMddyyyy").format(new Date()).toString());
        locationListRef.push().setValue(entity);
    }

    @Override
    public void delete(ShowNotificationEvent entity) throws DAOException {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public Future<List<ShowNotificationEvent>> getAll() throws DAOException {
        throw new DAOException();

    }

    @Override
    public Future<List<ShowNotificationEvent>> getLast(int N) throws DAOException {
        throw new DAOException();
    }

    @Override
    public void update(ShowNotificationEvent oldEntity, ShowNotificationEvent newEntity) throws DAOException {
        throw new RuntimeException("Not implemented");
    }
}
