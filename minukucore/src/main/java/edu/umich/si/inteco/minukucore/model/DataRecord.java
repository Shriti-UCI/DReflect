package edu.umich.si.inteco.minukucore.model;

/**
 * Created by shriti on 7/9/16.
 * A DataRecord is the smallest unit of data.
 * @version 1.0
 */
public interface DataRecord {

    /**
     * Fetch the value of a DataRecord.
     *
     * @return the value of a dataRecord
     */
    public DataRecord getValue();

    /**
     * Fetch the time at which the DataRecord was created.
     *
     * @return the time at which the DataRecord was created
     */
    public long getCreationTime();
}
