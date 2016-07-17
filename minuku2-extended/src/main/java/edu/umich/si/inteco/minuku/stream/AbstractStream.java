package edu.umich.si.inteco.minuku.stream;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import edu.umich.si.inteco.minukucore.exception.DataRecordTypeNotFound;
import edu.umich.si.inteco.minukucore.model.DataRecord;
import edu.umich.si.inteco.minukucore.stream.Stream;

/**
 * Created by Neeraj Kumar on 7/17/16.
 */
public class AbstractStream<T extends DataRecord> extends LinkedList<T> implements Stream<T> {

    @Override
    public T getCurrentValue() {
        return this.getLast();
    }

    @Override
    public T getPreviousValue() {
        return this.get(this.size() - 1);
    }

    @Override
    public StreamType getType() {
        return StreamType.FROM_DEVICE;
    }

    @Override
    public <T extends DataRecord> List<Class<T>> dependsOnDataRecord() throws DataRecordTypeNotFound {
        return new ArrayList<>();
    }

}
