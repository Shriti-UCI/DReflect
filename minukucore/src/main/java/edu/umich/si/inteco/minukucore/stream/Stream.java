package edu.umich.si.inteco.minukucore.stream;

import java.util.Collection;
import java.util.Queue;

import edu.umich.si.inteco.minukucore.model.DataRecord;

/**
 * Created by shriti on 7/9/16.
 */
public interface Stream<T extends DataRecord> extends Queue {

    public T getCurrentValue();

    public T getPreviousValue();

}
