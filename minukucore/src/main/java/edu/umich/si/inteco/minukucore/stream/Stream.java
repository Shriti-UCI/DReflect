package edu.umich.si.inteco.minukucore.stream;

import java.util.Collection;
import java.util.List;
import java.util.Queue;

import edu.umich.si.inteco.minukucore.exception.DataRecordTypeNotFound;
import edu.umich.si.inteco.minukucore.model.DataRecord;

/**
 * Created by shriti on 7/9/16.
 * A Stream is a collection of DataRecords
 * @see edu.umich.si.inteco.minukucore.model.DataRecord
 * @version 1.0
 */
public interface Stream<T extends DataRecord> extends Queue<T> {

    /**
     * Defining stream types
     */
    public enum StreamType{
        FROM_DEVICE, FROM_USER, FROM_QUESTION
    }

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

    /**
     * Fetch a list of DataRecord types that this stream
     * uses as inputs to create a new stream
     *
     * @return the list of DataRecord types
     * @throws {@link edu.umich.si.inteco.minukucore.exception.DataRecordTypeNotFound}
     */
    public<T extends DataRecord> List<Class<T>> dependsOnDataRecord() throws DataRecordTypeNotFound;

    /**
     * Get the type of stream - from_device, from_user, from_question
     * @return the type of stream
     *         {@link edu.umich.si.inteco.minukucore.stream.Stream.StreamType}
     */
    public StreamType getType();

}
