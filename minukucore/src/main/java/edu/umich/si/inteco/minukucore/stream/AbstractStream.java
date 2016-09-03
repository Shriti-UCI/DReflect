package edu.umich.si.inteco.minukucore.stream;

import android.util.Log;

import java.util.Collection;
import java.util.LinkedList;

import edu.umich.si.inteco.minukucore.model.DataRecord;

/**
 * Created by Neeraj Kumar on 7/17/16.
 *
 * AbstractStream which also acts as an evicting queue with a maxSize.
 * The evicting queue implementation has a maxSize. Once the queue reaches this maxSize, any new
 * elements added to the queue lead to elements being removed(evicted) from the front of the queue.
 */
public abstract class AbstractStream<T extends DataRecord>
        extends LinkedList<T>
        implements Stream<T> {

    protected int maxSize;

    public AbstractStream(int maxSize) {
        this.maxSize = maxSize;
    }

    @Override
    public boolean add(T object) {
        if (this.size() == maxSize) {
            this.removeFirst();
        }
        return super.add(object);
    }

    @Override
    public void add(int location, T object) {
        if (this.size() == maxSize) {
            this.removeFirst();
        }
        super.add(location, object);
    }

    @Override
    public boolean addAll(Collection<? extends T> objects) {
        final int neededSize = size() + objects.size();
        final int overflowSize = neededSize - maxSize;
        if (overflowSize > 0) {
            removeRange(0, overflowSize);
        }
        return super.addAll(objects);
    }

    @Override
    public boolean addAll(int location, Collection<? extends T> objects) {
        Log.e("AbstractStream", "Cannot add to multiple entries at location.");
        throw new UnsupportedOperationException(
                "Cannot add to multiple entries at location.");
    }

    @Override
    public void addFirst(T object) {
        Log.e("AbstractStream", "Cannot add to the starting of the queue in abstract stream.");
        throw new UnsupportedOperationException(
                "Cannot add to the starting of the queue in abstract stream.");
    }

    @Override
    public void addLast(T object) {
        this.add(object);
    }

    @Override
    public T getCurrentValue() {
        return (this.size() >= 2 ? this.getLast() : null);
    }

    @Override
    public T getPreviousValue() {
        return this.size() > 1 ? this.get(this.size() - 2): null;
    }

}
