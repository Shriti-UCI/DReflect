package edu.umich.si.inteco.minuku_2.event;

import java.security.AccessControlContext;
import java.util.List;

import edu.umich.si.inteco.minukucore.event.ActionEvent;
import edu.umich.si.inteco.minukucore.model.DataRecord;

/**
 * Created by shriti on 8/1/16.
 */
public class MissedInsulinAdminEvent extends ActionEvent {

    public MissedInsulinAdminEvent(String typeOfEvent, List<DataRecord> dataRecords) {
        super(typeOfEvent, dataRecords);
    }
}
