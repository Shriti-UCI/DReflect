package edu.umich.si.inteco.minukucore.situation;

import java.util.List;

import edu.umich.si.inteco.minukucore.event.ActionEvent;
import edu.umich.si.inteco.minukucore.model.DataRecord;
import edu.umich.si.inteco.minukucore.model.StreamSnapshot;
import edu.umich.si.inteco.minukucore.stream.Stream;

/**
 * Created by shriti on 7/9/16.
 * A situation depends on certain type of data records
 * It generates an action events defined by the developer
 * The developer would code the rules for the situation in the
 * assertSituation method.
 */
public interface Situation {

    /**
     * Put the rules to be tested for the situation and return an action event
     * @param snapshot a data structure containing current and previous values of the stream when
     *                 the state change happened.
     *                 {@link edu.umich.si.inteco.minukucore.model.StreamSnapshot}
     * @param <T>      the application specific DataRecord
     *                 {@link edu.umich.si.inteco.minukucore.model.DataRecord}
     * @return         an {@link edu.umich.si.inteco.minukucore.event.ActionEvent}
     *                 some {@link edu.umich.si.inteco.minukucore.action.Action} will be
     *                 subscribed to this action event
     *
     */
    public <T extends ActionEvent> T assertSituation(StreamSnapshot snapshot);

    /**
     * Fetch a list of DataRecords types that the situation depends on
     * @return the list of DataRecord classes
     */
    public <T extends DataRecord> List<Class<T>> dependsOnDataRecord();
}
