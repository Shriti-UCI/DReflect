package edu.umich.si.inteco.minukucore.manager;

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
     */
    public void onStateChange(StreamSnapshot s);

    /**
     * Register a situation after checking for the existence of all the streams
     * that the situation depends on. Add situation to the situation manager's registry
     * @param s the situation requesting to be registered
     * @return true if registration is successful
     */
    public boolean register(Situation s);

    /**
     * Unregister a situation. Remove situation from registry.
     * @param s the situation requesting to be unregistered
     * @return true if the unregistration is successful
     */
    public boolean unregister(Situation s);
}
