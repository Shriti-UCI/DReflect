package edu.umich.si.inteco.minukucore.stream;

import java.util.Collection;

import edu.umich.si.inteco.minukucore.model.DataRecord;

/**
 * Created by shriti on 7/9/16.
 */
public interface Stream<T extends DataRecord> extends Collection {

    public T getCurrentValue();

    public T getPreviousValue();

    public long getExpiryTime();

    //overrides iterable
}
