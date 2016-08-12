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
public abstract class AndroidStreamGenerator<T extends DataRecord>
        implements StreamGenerator<T> {

    protected Context mApplicationContext;

    public AndroidStreamGenerator(Context aApplicationContext) {
        this.mApplicationContext = aApplicationContext;
    }

    public AndroidStreamGenerator() {

    }
}
