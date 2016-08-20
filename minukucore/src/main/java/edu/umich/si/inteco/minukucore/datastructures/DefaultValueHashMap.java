package edu.umich.si.inteco.minukucore.datastructures;

import java.util.HashMap;

/**
 * Created by neerajkumar on 8/19/16.
 */
public class DefaultValueHashMap<K,V> extends HashMap<K,V> {

    protected V globalDefaultValue;

    public DefaultValueHashMap(V defaultValue) {
        this.globalDefaultValue = defaultValue;
    }

    @Override
    public V get(Object k) {
        return containsKey(k) ? super.get(k) : globalDefaultValue;
    }

    public V getDefault(Object k, V aDefaultValue) {
        return containsKey(k) ? super.get(k) :  aDefaultValue;
    }
}
