package edu.umich.si.inteco.minukucore.manager;

import edu.umich.si.inteco.minukucore.dao.DAO;
import edu.umich.si.inteco.minukucore.model.DataRecord;

/**
 * The DAOManager keeps a mapping of {@link DataRecord DataRecord} type to
 * {@link DAO DataAccessObject} responsible for CRUD operations on the former.
 *
 * Created by neerajkumar on 7/13/16.
 */
public interface DAOManager {
    /**
     * Given the class of a {@link DataRecord}, returns the DAO associated with it.
     *
     * @param dataRecordType Class of {@link DataRecord}
     * @param <T> A subclass of {@link DAO}
     * @param <D> A subclass of {@link DataRecord}
     * @return
     */
    public <T extends DAO<D>, D extends DataRecord> T getDaoFor(Class<D> dataRecordType);

    /**
     * Register a DAO for a specific DataRecord type.
     *
     * @param dataRecordType Class of {@link DataRecord} with which a specific DAO is associated and
     *                       responsible for.
     * @param dao {@link DAO} object that is responsible for CRUD operations on a DataRecord class.
     * @param <T> A subclass of {@link DAO}
     * @param <D> A subclass of {@link DataRecord}
     */
    public <T extends DAO<D>, D extends DataRecord> void registerDaoFor(Class<D> dataRecordType,
                                                                     T dao);
}
