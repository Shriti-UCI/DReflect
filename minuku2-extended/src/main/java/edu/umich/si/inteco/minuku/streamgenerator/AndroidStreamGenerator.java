package edu.umich.si.inteco.minuku.streamgenerator;

import android.content.Context;

import edu.umich.si.inteco.minukucore.model.DataRecord;
import edu.umich.si.inteco.minukucore.streamgenerator.StreamGenerator;

/**
 * Created by Neeraj Kumar on 7/17/16.
 *
 * This is an Android OS specific interface which extends the StreamGenerator interface. Application
 * Context would be an important requirement for any StreamGenerator which generates
 * a {@link edu.umich.si.inteco.minukucore.stream.Stream} of type
 * {@link edu.umich.si.inteco.minukucore.stream.Stream.StreamType#FROM_DEVICE}
 */
public interface AndroidStreamGenerator<T extends DataRecord> extends StreamGenerator<T> {

    /**
     * onStreamRegistration will be called when this StreamGenerator is registered successfully
     * with the {@link edu.umich.si.inteco.minukucore.manager.StreamManager}. The {@link Context}
     * is a required conduit to get access to raw data from the phone, e.g. Location, Accelerometer
     * etc. This method passes the Context to the StreamGenerator for whatever purpose the
     * StreamGenerator might require it for.
     *
     * @param applicationContext The application's context.
     */
    public void onStreamRegistration(Context applicationContext);
}
