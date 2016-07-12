package edu.umich.si.inteco.minukucore.stream;

import java.util.Collection;
import java.util.Queue;

import edu.umich.si.inteco.minukucore.model.DataRecord;

/**
 * Created by shriti on 7/9/16.
 * A Stream is a collection of DataRecords
 * @see edu.umich.si.inteco.minukucore.model.DataRecord
 * @version 1.0
 */
public interface Stream<T extends DataRecord> extends Queue {

    /**
     * Fetch the current value of the stream
     *
     * @return the value of the newest DataRecord (T) in the stream
     */
    public T getCurrentValue();

    /**
     * Fetch the previous value of the stream - the older current value
     *
     * @return the value of the DataRecord right after the newest DataRecord
     */
    public T getPreviousValue();

}
