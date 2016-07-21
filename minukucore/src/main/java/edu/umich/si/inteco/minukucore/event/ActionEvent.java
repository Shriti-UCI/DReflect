package edu.umich.si.inteco.minukucore.event;

import java.util.List;

import edu.umich.si.inteco.minukucore.action.Action;
import edu.umich.si.inteco.minukucore.model.DataRecord;

/**
 * Created by neerajkumar on 7/12/16.
 *
 * Author: Shriti Raj
 */
public class ActionEvent {
    public String eventType;
    public List<DataRecord> dataRecordsForEvent;

    public ActionEvent(String typeOfEvent, List<DataRecord> dataRecords ){
        this.eventType = typeOfEvent;
        this.dataRecordsForEvent = dataRecords;
    }
}
