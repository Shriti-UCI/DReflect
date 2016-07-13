package edu.umich.si.inteco.minukucore.model;

/**
 * Created by shriti on 7/10/16.
 * wrapper over Map<Class<T> clazz, Map<String, T>> snapshotData or any other data structure the
 * developer decides to use
 * Example of a Map:
 *  <LocationDataRecordType, <current value: 1, previous value: 2>
 *  <MoodDataRecordType, <current value: happy, previous value: sad>
 *
 */
public interface StreamSnapshot {

    /**
     * Fetch the current DataRecord value for a given dataRecordType
     * from the StreamSnapshot data structure
     * @param dataRecordType the class of application specific DataRecord
     * @param <T> the application specific DataRecord
     * {@link edu.umich.si.inteco.minukucore.model.DataRecord}
     * @return DataRecord of type T
     */
    public <T extends DataRecord> T getCurrentValue(Class<T> dataRecordType);

    /**
     * Fetch the previous DataRecord value for a given dataRecordType
     * from the StreamSnapshot data structure
     * @param dataRecordType the class of application specific DataRecord
     * @param <T> the application specific DataRecord
     * {@link edu.umich.si.inteco.minukucore.model.DataRecord}
     * @return DataRecord of type T
     */
    public <T extends DataRecord> T getPreviousValue(Class<T> dataRecordType);
}
