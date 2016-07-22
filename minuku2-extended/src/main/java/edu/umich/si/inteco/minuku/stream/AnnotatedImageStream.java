package edu.umich.si.inteco.minuku.stream;

import java.util.ArrayList;
import java.util.List;

import edu.umich.si.inteco.minuku.model.AnnotatedImageDataRecord;
import edu.umich.si.inteco.minukucore.model.DataRecord;
import edu.umich.si.inteco.minukucore.stream.AbstractStreamFromUser;

/**
 * Created by shriti on 7/22/16.
 */
public class AnnotatedImageStream extends AbstractStreamFromUser<AnnotatedImageDataRecord> {

    public AnnotatedImageStream(int maxSize) {
        super(maxSize);
    }

    @Override
    public List<Class <? extends DataRecord>> dependsOnDataRecordType() {
        return new ArrayList<>();
    }
}
