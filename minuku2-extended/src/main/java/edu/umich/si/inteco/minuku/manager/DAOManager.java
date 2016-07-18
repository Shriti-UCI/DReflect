package edu.umich.si.inteco.minuku.manager;

import android.provider.ContactsContract;

import java.util.HashMap;
import java.util.Map;

import edu.umich.si.inteco.minukucore.dao.DAO;
import edu.umich.si.inteco.minukucore.model.DataRecord;

/**
 * Created by neerajkumar on 7/18/16.
 */
public class DAOManager extends edu.umich.si.inteco.minukucore.manager.AbstractDAOManager {

    private static DAOManager instance;
    private Map<? extends DataRecord, DAO<? extends DataRecord>> registeredDAOMap;

    private DAOManager() {
        this.map = new HashMap<>();
    }

    public static DAOManager getInstance() {
        if (instance == null) {
            instance = new DAOManager();
        }
        return instance;
    }

}
