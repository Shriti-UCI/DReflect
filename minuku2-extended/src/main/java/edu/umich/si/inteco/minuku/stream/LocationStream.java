package edu.umich.si.inteco.minuku.stream;


import edu.umich.si.inteco.minuku.model.LocationDataRecord;
import edu.umich.si.inteco.minukucore.stream.AbstractStream;

/**
 * Created by Neeraj Kumar on 7/17/16.
 */
public class LocationStream extends AbstractStream<LocationDataRecord> {

    public LocationStream(int maxSize) {
        super(maxSize);
    }
}
