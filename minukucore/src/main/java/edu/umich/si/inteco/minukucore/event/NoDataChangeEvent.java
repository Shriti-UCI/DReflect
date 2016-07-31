package edu.umich.si.inteco.minukucore.event;

import edu.umich.si.inteco.minukucore.model.DataRecord;

/**
 * Created by neerajkumar on 7/30/16.
 */
public class NoDataChangeEvent {
    //contains the type of data for which the state changed
    // only one data type, comes from stream(generator)
    private Class<? extends DataRecord> eventType;

    public NoDataChangeEvent(Class<? extends DataRecord> anEventType) {
        this.eventType = anEventType;
    }

    public Class<? extends DataRecord> getType() {
        return this.eventType;
    }
}
