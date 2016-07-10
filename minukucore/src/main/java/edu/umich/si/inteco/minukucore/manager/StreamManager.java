package edu.umich.si.inteco.minukucore.manager;

import android.content.Context;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import edu.umich.si.inteco.minukucore.exception.StreamAlreadyExistsException;
import edu.umich.si.inteco.minukucore.exception.StreamNotFoundException;
import edu.umich.si.inteco.minukucore.model.DataRecord;
import edu.umich.si.inteco.minukucore.stream.Stream;

/**
 * Created by shriti on 7/9/16.
 */
public interface StreamManager {

    public List<Stream> getAllStreams();

    public <T> void register(Stream s, Class<T> clazz) throws StreamNotFoundException, StreamAlreadyExistsException;

    public boolean unregister(Stream s);

    public <T> T getStreamFor(Class<T> clazz);

    //subscribe this to the state change event in the abstract class
    public void onStateChange();

}

/**
 * getStreamFor(LocationDataStream.class);
 */


