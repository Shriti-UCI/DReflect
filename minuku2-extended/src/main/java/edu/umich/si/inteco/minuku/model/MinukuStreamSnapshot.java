package edu.umich.si.inteco.minuku.model;

import java.util.List;
import java.util.Map;

import edu.umich.si.inteco.minukucore.model.DataRecord;
import edu.umich.si.inteco.minukucore.model.StreamSnapshot;

/**
 * Created by neerajkumar on 7/27/16.
 */
public class MinukuStreamSnapshot implements StreamSnapshot {

    private Map<Class<? extends DataRecord>, List<? extends DataRecord>> mSnapshotData;

    public MinukuStreamSnapshot(Map<Class<? extends DataRecord>,
            List<? extends DataRecord>> snapshotData) {
        mSnapshotData = snapshotData;
    }

    @Override
    public <T extends DataRecord> T getCurrentValue(Class<T> dataRecordType) {
        return (T) mSnapshotData.get(dataRecordType).get(0);
    }

    @Override
    public <T extends DataRecord> T getPreviousValue(Class<T> dataRecordType) {
        return (T) mSnapshotData.get(dataRecordType).get(1);
    }
}
