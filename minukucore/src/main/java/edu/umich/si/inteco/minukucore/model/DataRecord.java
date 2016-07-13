package edu.umich.si.inteco.minukucore.model;

/**
 * Created by shriti on 7/9/16.
 * A DataRecord is the smallest unit of data.
 * @version 1.0
 */
public interface DataRecord {

    /**
     * Fetch the time at which the DataRecord was created.
     *
     * @return the time at which the DataRecord was created
     */
    public long getCreationTime();
}
