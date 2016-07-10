package edu.umich.si.inteco.minukucore.model;

/**
 * Created by shriti on 7/10/16.
 */
public interface StreamSnapshot {
    // wrapper over Map<Class<T> clazz, Map<String, T>> snapshotData;
    public <T> T getCurrentValue(Class<T> streamType);
    public <T> T getPreviousValue(Class<T> streamType);
}
