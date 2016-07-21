package edu.umich.si.inteco.minukucore.stream;

import edu.umich.si.inteco.minukucore.model.DataRecord;

/**
 * Created by shriti on 7/19/16.
 */
public abstract class AbstractStreamFromDevice<T extends DataRecord> extends AbstractStream<T> {

    public AbstractStreamFromDevice(int maxSize) {
        super(maxSize);
    }


    @Override
    public StreamType getType() {
        return StreamType.FROM_DEVICE;
    }

}
