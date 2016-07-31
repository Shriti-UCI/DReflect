package edu.umich.si.inteco.minukucore.event;

import edu.umich.si.inteco.minukucore.model.DataRecord;

/**
 * Created by neerajkumar on 7/30/16.
 */
public class NoDataChangeEvent extends MinukuEvent {

    public NoDataChangeEvent(Class<? extends DataRecord> anEventType) {
        super(anEventType);
    }
}
