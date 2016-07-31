package edu.umich.si.inteco.minukucore.event;

import edu.umich.si.inteco.minukucore.model.DataRecord;

/**
 * Created by neerajkumar on 7/31/16.
 */
public class IsDataExpectedEvent extends MinukuEvent {

    public IsDataExpectedEvent(Class<? extends DataRecord> anEventType) {
        super(anEventType);
    }
}
