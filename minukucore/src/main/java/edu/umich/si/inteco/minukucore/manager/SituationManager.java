package edu.umich.si.inteco.minukucore.manager;

import edu.umich.si.inteco.minukucore.event.StateChangeEvent;
import edu.umich.si.inteco.minukucore.exception.DataRecordTypeNotFound;
import edu.umich.si.inteco.minukucore.model.StreamSnapshot;
import edu.umich.si.inteco.minukucore.situation.Situation;

/**
 * Created by shriti on 7/9/16.
 * A registry of all the {@link edu.umich.si.inteco.minukucore.situation.Situation} situations
 * contains the logic to call relevant registered situations when a state change event is received
 *
 */
public interface SituationManager {

    /**
     * Called by {@link edu.umich.si.inteco.minukucore.manager.StreamManager}
     * Sends request to appropriate situations
     * Note: does not subscribe to any event on the event bus
     * @param s {@link edu.umich.si.inteco.minukucore.model.StreamSnapshot}
     *          a snapshot of the stream with current and previous values
     *          at the time of state change.
     * @param event state change event transferred by StreamManager
     *              contains the type of data for which the state changed
     *              the situations associated with this data type will be called
     */
    public void onStateChange(StreamSnapshot s, StateChangeEvent event);

    /**
     * Register a situation after checking for the existence of all the streams
     * that the situation depends on. Add situation to the situation manager's registry.
     * Whenever a {@link Situation} calls the register method, this Manager would call the
     * {@link Situation#dependsOnDataRecordType()} method of the Situation trying to register
     *      before it can successfully add that Situation to the registry.
     * @param s the situation requesting to be registered
     * @return true if registration is successful
     * @throws DataRecordTypeNotFound exception when the any of the
     *      {@link edu.umich.si.inteco.minukucore.model.DataRecord}s that the Situation depends on
     *      are not registered with the StreamManager.
     */
    public <T extends Situation> boolean register(T s) throws DataRecordTypeNotFound;

    /**
     * Unregister a situation. Remove situation from registry.
     * @param s the situation requesting to be unregistered
     * @return true if the unregistration is successful
     */
    public boolean unregister(Situation s);
}
