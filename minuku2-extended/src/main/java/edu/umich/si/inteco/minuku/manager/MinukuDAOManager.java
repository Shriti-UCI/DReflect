package edu.umich.si.inteco.minuku.manager;

import java.util.HashMap;
import java.util.Map;

import edu.umich.si.inteco.minukucore.dao.DAO;
import edu.umich.si.inteco.minukucore.model.DataRecord;

/**
 * Created by neerajkumar on 7/18/16.
 */
public class MinukuDAOManager extends edu.umich.si.inteco.minukucore.manager.AbstractDAOManager {

    private static MinukuDAOManager instance;
    private Map<? extends DataRecord, DAO<? extends DataRecord>> registeredDAOMap;

    private MinukuDAOManager() {
        this.map = new HashMap<>();
    }

    public static MinukuDAOManager getInstance() {
        if (instance == null) {
            instance = new MinukuDAOManager();
        }
        return instance;
    }

}
