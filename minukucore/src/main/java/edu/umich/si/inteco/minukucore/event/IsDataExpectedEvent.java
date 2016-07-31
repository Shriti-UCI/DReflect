package edu.umich.si.inteco.minukucore.event;

import edu.umich.si.inteco.minukucore.model.DataRecord;

/**
 * Created by neerajkumar on 7/31/16.
 */
public class IsDataExpectedEvent {
    //contains the type of data for which the state changed
    // only one data type, comes from stream(generator)
    private Class<? extends DataRecord> eventType;

    public IsDataExpectedEvent(Class<? extends DataRecord> anEventType) {
        this.eventType = anEventType;
    }

    public Class<? extends DataRecord> getType() {
        return this.eventType;
    }
}
