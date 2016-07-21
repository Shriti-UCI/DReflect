package edu.umich.si.inteco.minuku.stream;


import java.util.ArrayList;
import java.util.List;

import edu.umich.si.inteco.minuku.model.LocationDataRecord;
import edu.umich.si.inteco.minukucore.model.DataRecord;
import edu.umich.si.inteco.minukucore.stream.AbstractStreamFromDevice;

/**
 * Created by Neeraj Kumar on 7/17/16.
 */
public class LocationStream extends AbstractStreamFromDevice<LocationDataRecord> {

    public LocationStream(int maxSize) {
        super(maxSize);
    }

    @Override
    public List<Class<? extends DataRecord>> dependsOnDataRecord() {
        return new ArrayList<>();
    }
}
