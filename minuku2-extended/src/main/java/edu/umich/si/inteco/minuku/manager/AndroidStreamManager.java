package edu.umich.si.inteco.minuku.manager;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.umich.si.inteco.minukucore.event.StateChangeEvent;
import edu.umich.si.inteco.minukucore.exception.StreamAlreadyExistsException;
import edu.umich.si.inteco.minukucore.exception.StreamNotFoundException;
import edu.umich.si.inteco.minukucore.manager.StreamManager;
import edu.umich.si.inteco.minukucore.model.DataRecord;
import edu.umich.si.inteco.minukucore.stream.Stream;

/**
 * Created by Neeraj Kumar on 7/17/16.
 */
public class AndroidStreamManager extends Service implements StreamManager {

    protected Map<Class, Stream> mStreamMap;
    protected Map<Stream.StreamType, List<Stream<? extends DataRecord>>> mStreamTypeStreamMap;

    @Override
    public List<Stream> getAllStreams() {
        return new LinkedList<>(mStreamMap.values());
    }

    @Override
    public <T extends DataRecord> void register(Stream s, Class<T> clazz)
            throws StreamNotFoundException, StreamAlreadyExistsException {
        if(mStreamMap.containsKey(clazz)) {
            throw new StreamAlreadyExistsException();
        }
        for(Object dependsOnClass:s.dependsOnDataRecord()) {
            if(!mStreamMap.containsKey(dependsOnClass)) {
                throw new StreamNotFoundException();
            }
        }
        mStreamMap.put(clazz, s);
    }

    @Override
    public boolean unregister(Stream s) throws StreamNotFoundException {
        Class classType = s.getCurrentValue().getClass();
        if(!mStreamMap.containsKey(classType)) {
            throw new StreamNotFoundException();
        }
        mStreamMap.remove(s);
        return true;
    }

    @Override
    public <T extends DataRecord> Stream<T> getStreamFor(Class<T> clazz) throws StreamNotFoundException {
        return mStreamMap.get(clazz);
    }

    @Override
    public void handleStateChangeEvent(StateChangeEvent e) {

    }

    @Override
    public List<Stream<? extends DataRecord>> getStreams(Stream.StreamType streamType) {
        return mStreamTypeStreamMap.get(streamType);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
