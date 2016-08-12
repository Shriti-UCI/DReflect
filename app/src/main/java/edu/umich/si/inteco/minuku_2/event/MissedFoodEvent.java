package edu.umich.si.inteco.minuku_2.event;

import java.util.List;

import edu.umich.si.inteco.minukucore.event.ActionEvent;
import edu.umich.si.inteco.minukucore.model.DataRecord;

/**
 * Created by shriti on 8/12/16.
 */
public class MissedFoodEvent extends ActionEvent {

    public MissedFoodEvent(String typeOfEvent, List<DataRecord> dataRecords) {
        super(typeOfEvent, dataRecords);
    }
}
