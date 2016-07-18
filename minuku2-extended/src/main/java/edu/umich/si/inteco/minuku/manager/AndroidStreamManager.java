package edu.umich.si.inteco.minuku.manager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.umich.si.inteco.minuku.config.Constants;
import edu.umich.si.inteco.minukucore.event.StateChangeEvent;
import edu.umich.si.inteco.minukucore.exception.StreamAlreadyExistsException;
import edu.umich.si.inteco.minukucore.exception.StreamNotFoundException;
import edu.umich.si.inteco.minukucore.manager.StreamManager;
import edu.umich.si.inteco.minukucore.model.DataRecord;
import edu.umich.si.inteco.minukucore.stream.Stream;
import edu.umich.si.inteco.minukucore.streamgenerator.StreamGenerator;

/**
 * Created by Neeraj Kumar on 7/17/16.
 *
 * The AndroidStreamManager class implements {@link StreamManager} and runs as a service within
 * the application context. It maintains a list of all the Streams and StreamGenerators registered
 * within the application and is responsible for trigerring the
 * {@link StreamGenerator#updateStream() updateStream} method of the StreamManager class after
 * every {@link StreamGenerator#getUpdateFrequency() updateFrequency}.
 *
 * The AndroidStreamManager is a {@link Service#START_STICKY sticky} service.
 */
public class AndroidStreamManager extends Service implements StreamManager {

    private final String TAG = "AndroidStreamManager";

    protected Map<Class, Stream> mStreamMap;
    protected Map<Stream.StreamType, List<Stream<? extends DataRecord>>> mStreamTypeStreamMap;
    protected List<StreamGenerator> mRegisteredStreamGenerators;

    private static int counter = 0;

    private static AndroidStreamManager instance = null;

    private AndroidStreamManager() {

    }

    public static AndroidStreamManager getInstance() {
        if(instance == null) {
            instance = new AndroidStreamManager();
        }
        return instance;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Start command received.");
        for(StreamGenerator streamGenerator: mRegisteredStreamGenerators) {
            if(counter % streamGenerator.getUpdateFrequency() == 0) {
                streamGenerator.updateStream();
            }
        }

        AlarmManager alarm = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarm.set(
                alarm.RTC_WAKEUP,
                System.currentTimeMillis() + Constants.PROMPT_SERVICE_REPEAT_MILLISECONDS,
                PendingIntent.getService(this, 0, new Intent(this, AndroidStreamManager.class), 0)
        );

        return START_STICKY_COMPATIBILITY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "Destroying service.");
    }


    @Override
    public List<Stream> getAllStreams() {
        return new LinkedList<>(mStreamMap.values());
    }

    @Override
    public <T extends DataRecord> void register(Stream s, Class<T> clazz,
                                                StreamGenerator aStreamGenerator)
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
        mRegisteredStreamGenerators.add(aStreamGenerator);
    }

    @Override
    public boolean unregister(Stream s, StreamGenerator sg) throws StreamNotFoundException {
        Class classType = s.getCurrentValue().getClass();
        if(!mStreamMap.containsKey(classType)) {
            throw new StreamNotFoundException();
        }
        mStreamMap.remove(s);
        return mRegisteredStreamGenerators.remove(sg);
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
