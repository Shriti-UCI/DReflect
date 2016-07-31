package edu.umich.si.inteco.minukucore.event;

import edu.umich.si.inteco.minukucore.model.DataRecord;

/**
 * Created by neerajkumar on 7/12/16.
 */
public class StateChangeEvent extends MinukuEvent {

    public StateChangeEvent(Class<? extends DataRecord> anEventType) {
        super(anEventType);
    }

}


