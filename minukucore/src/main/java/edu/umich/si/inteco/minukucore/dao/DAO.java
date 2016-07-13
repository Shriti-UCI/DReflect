package edu.umich.si.inteco.minukucore.dao;

import java.util.List;
import java.util.UUID;

import edu.umich.si.inteco.minukucore.model.DataRecord;
import edu.umich.si.inteco.minukucore.user.User;

/**
 * DAO interface expects the DAO class to provide methods for
 * CRUD operations on a DataRecord.
 *
 * Created by Neeraj Kumar on 7/12/2016.
 */
public interface DAO<T extends DataRecord> {

    /**
     * Set the device details and user details for this DAO.
     * Each DataRecord must be associated with a user and device.
     *
     * @param user The {@link edu.umich.si.inteco.minukucore.user.User user}
     *             for whom the DataRecords are generated.
     * @param uuid The UUID of the device which the user is on,
     *             when such a DataRecord was generated.
     */
    public void setDevice(User user, UUID uuid);

    /**
     * Add a new entity to persistent data.
     *
     * @param entity
     * @throws DAOException
     */
    public void add(T entity) throws DAOException;

    /**
     * Delete an entity from the persistent data.
     *
     * @param entity
     * @throws DAOException
     */
    public void delete(T entity) throws DAOException;

    /**
     * Get all entities as a list.
     *
     * @return A list of all entities of this type in the persistent storage.
     * @throws DAOException
     */
    public List<T> getAll() throws DAOException;

    /**
     * Get the last {@param N} entities as a list.
     *
     * @param N the number of records to pull from the persistent storage.
     * @return The last N DataRecords for this DAO.
     * @throws DAOException
     */
    public List<T> getLast(int N) throws DAOException;

    /**
     * Update an old entity with the new entity, without modifying it's location in the
     * list.
     *
     * @param oldEntity
     * @param newEntity
     * @throws DAOException
     */
    public void update(T oldEntity, T newEntity) throws DAOException;

}
