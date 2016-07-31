package edu.umich.si.inteco.minuku_2.event;

import java.util.List;

import edu.umich.si.inteco.minukucore.event.ActionEvent;
import edu.umich.si.inteco.minukucore.model.DataRecord;

/**
 * Created by neerajkumar on 7/30/16.
 */
public class MoodDataExpectedActionEvent extends ActionEvent {

    public MoodDataExpectedActionEvent(String typeOfEvent, List<DataRecord> dataRecords) {
        super(typeOfEvent, dataRecords);
    }
}
