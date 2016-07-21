package edu.umich.si.inteco.minuku.stream;

import java.util.ArrayList;
import java.util.List;

import edu.umich.si.inteco.minuku.model.ImageDataRecord;
import edu.umich.si.inteco.minukucore.model.DataRecord;
import edu.umich.si.inteco.minukucore.stream.AbstractStreamFromUser;

/**
 * Created by shriti on 7/19/16.
 */
public class ImageStream extends AbstractStreamFromUser<ImageDataRecord>{

    public ImageStream(int maxSize) {
        super(maxSize);
    }

    @Override
    public <T extends DataRecord> List<Class<T>> dependsOnDataRecord() {
        return new ArrayList<>();
    }
}
