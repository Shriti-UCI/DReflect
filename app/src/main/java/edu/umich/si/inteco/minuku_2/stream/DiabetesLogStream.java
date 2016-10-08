package edu.umich.si.inteco.minuku_2.stream;

import java.util.ArrayList;
import java.util.List;

import edu.umich.si.inteco.minuku_2.model.DiabetesLogDataRecord;
import edu.umich.si.inteco.minukucore.model.DataRecord;
import edu.umich.si.inteco.minukucore.stream.AbstractStreamFromUser;

/**
 * Created by shriti on 10/8/16.
 */

public class DiabetesLogStream extends AbstractStreamFromUser<DiabetesLogDataRecord> {

    public DiabetesLogStream(int maxSize) {
        super(maxSize);
    }

    @Override
    public List<Class<? extends DataRecord>> dependsOnDataRecordType() {
        return new ArrayList<>();
    }
}
