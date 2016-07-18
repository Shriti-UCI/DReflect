package edu.umich.si.inteco.minuku.manager;

/**
 * Created by neerajkumar on 7/18/16.
 */
public class DAOManager extends edu.umich.si.inteco.minukucore.manager.AbstractDAOManager {

    private static DAOManager instance;

    private DAOManager() {

    }

    public static DAOManager getInstance() {
        if (instance == null) {
            instance = new DAOManager();
        }
        return instance;
    }
}
