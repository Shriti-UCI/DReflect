package edu.umich.si.inteco.minukucore.stream;

import edu.umich.si.inteco.minukucore.model.DataRecord;

/**
 * Created by shriti on 7/19/16.
 */
public abstract class AbstractStreamFromUser<T extends DataRecord> extends AbstractStream<T> {

    public AbstractStreamFromUser(int maxSize) {
        super(maxSize);
    }

    @Override
    public StreamType getType() {
        return StreamType.FROM_USER;
    }
}
