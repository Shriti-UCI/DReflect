package edu.umich.si.inteco.minukucore.manager;

import java.util.Map;

import edu.umich.si.inteco.minukucore.dao.DAO;
import edu.umich.si.inteco.minukucore.model.DataRecord;

/**
 * Created by neerajkumar on 7/18/16.
 */
public abstract  class AbstractDAOManager implements DAOManager {

    private Map<Class<? extends DataRecord>, DAO<? extends DataRecord>> map;

    @Override
    public <T extends DAO<D>, D extends DataRecord> T getDaoFor(Class<D> dataRecordType) {
        return (T) map.get(dataRecordType);
    }

    @Override
    public <T extends DAO<D>, D extends DataRecord> void registerDaoFor(Class<D> dataRecordType, T dao) {
        map.put(dataRecordType, dao);
    }
}
