package edu.umich.si.inteco.minukucore.event;

import edu.umich.si.inteco.minukucore.model.DataRecord;

/**
 * An event in Minuku must contain the {@link Class<DataRecord>} to which this event is related to.
 * All events going around to the {@link edu.umich.si.inteco.minukucore.manager.StreamManager} or
 * {@link edu.umich.si.inteco.minukucore.manager.SituationManager} must extend this class.
 *
 * Created by neerajkumar on 7/31/16.
 */
public class MinukuEvent {
    private Class<? extends DataRecord> eventType;

    /**
     * Constructor.
     * @param anEventType The {@link edu.umich.si.inteco.minukucore.model.DataRecord} type which this event
     * is associated with.
     */
    public MinukuEvent(Class<? extends DataRecord> anEventType) {
        this.eventType = anEventType;
    }

    public Class<? extends DataRecord> getType() {
        return this.eventType;
    }
}
