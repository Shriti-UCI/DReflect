package edu.umich.si.inteco.minuku.streamgenerator;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import edu.umich.si.inteco.minuku.config.Constants;
import edu.umich.si.inteco.minuku.dao.AnnotatedImageDataRecordDAO;
import edu.umich.si.inteco.minuku.dao.ImageDataRecordDAO;
import edu.umich.si.inteco.minuku.event.DecrementLoadingProcessCountEvent;
import edu.umich.si.inteco.minuku.event.IncrementLoadingProcessCountEvent;
import edu.umich.si.inteco.minuku.manager.MinukuDAOManager;
import edu.umich.si.inteco.minuku.manager.MinukuStreamManager;
import edu.umich.si.inteco.minuku.model.AnnotatedImageDataRecord;
import edu.umich.si.inteco.minuku.model.ImageDataRecord;
import edu.umich.si.inteco.minuku.model.LocationDataRecord;
import edu.umich.si.inteco.minuku.stream.AnnotatedImageStream;
import edu.umich.si.inteco.minuku.stream.ImageStream;
import edu.umich.si.inteco.minukucore.dao.DAOException;
import edu.umich.si.inteco.minukucore.event.NoDataChangeEvent;
import edu.umich.si.inteco.minukucore.exception.StreamAlreadyExistsException;
import edu.umich.si.inteco.minukucore.exception.StreamNotFoundException;
import edu.umich.si.inteco.minukucore.stream.Stream;

/**
 * Created by shriti on 7/22/16.
 *
 * AnnotatedImageDataRecordDAO<T extends AnnotatedImageDataRecord> implements
 DAO<T>
 */
public class AnnotatedImageStreamGenerator<T extends AnnotatedImageDataRecord>
        extends AndroidStreamGenerator<T> {

    private AnnotatedImageStream<T> mStream;
    protected String TAG = "AnnotatedImageStreamGenerator";
    private Class<T> mDataRecordType;

    private AnnotatedImageDataRecordDAO mDAO;

    public AnnotatedImageStreamGenerator() {

    }
    /**
     * The AnnotatedImageStreamGenerator class is extended by multiple classes and the type of
     * generic changes for each subclass. The stream generator needs to get the DAO the type of
     * dataRecord which it is created over. As the type is passed in as a generic, the class of the
     * type cannot be determined at runtime. Hence the constructor for AnnotatedImageStreamGenerator
     * needs to take a type of class at runtime.
     * @param applicationContext The context of the application
     * @param dataRecordType The type of data record
     */
    public AnnotatedImageStreamGenerator(Context applicationContext, Class<T> dataRecordType) {
        super(applicationContext);
        this.mStream = new AnnotatedImageStream(Constants.DEFAULT_QUEUE_SIZE);
        this.mDAO = MinukuDAOManager.getInstance().getDaoFor(dataRecordType);
        this.mDataRecordType = dataRecordType;
        this.register();
    }

    @Override
    public void register() {
        Log.d(TAG, "Registering with Stream Manager");
        try {
            MinukuStreamManager.getInstance().register(mStream, mDataRecordType, this);
        } catch (StreamNotFoundException streamNotFoundException) {
            Log.e(TAG, "One of the streams on which ImageDataRecord/ImageStream depends in not found.");
        } catch (StreamAlreadyExistsException streamAlreadyExsistsException) {
            Log.e(TAG, "Another stream which provides ImageDataRecord/ImageStream is already registered.");
        }
    }

    @Override
    public Stream<T> generateNewStream() {
        return mStream;
    }

    @Override
    public boolean updateStream() {
        Log.d(TAG,
                "Update Stream called: The update frequency is - \n" +
                        Constants.FOOD_IMAGE_STREAM_GENERATOR_UPDATE_FREQUENCY_MINUTES);
        MinukuStreamManager.getInstance().handleNoDataChangeEvent(
                new NoDataChangeEvent(mDataRecordType));
        return true;
    }

    @Override
    public long getUpdateFrequency() {
        return -1;
    }

    @Override
    public void sendStateChangeEvent() {

    }

    @Override
    public void onStreamRegistration() {
        Log.d(TAG, "Stream " + TAG + " registered successfully");
        EventBus.getDefault().post(new IncrementLoadingProcessCountEvent());
        AsyncTask.execute(new Runnable() {
        @Override
        public void run() {
            try
            {
                Log.d(TAG, "Stream " + TAG + "initialized from previous state");
                Future<List<T>> listFuture =
                        mDAO.getLast(Constants.DEFAULT_QUEUE_SIZE);
                while(!listFuture.isDone()) {
                    Thread.sleep(1000);
                }
                Log.d(TAG, "Received data from Future for " + TAG);
                mStream.addAll(listFuture.get());
            } catch (DAOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } finally {
                EventBus.getDefault().post(new DecrementLoadingProcessCountEvent());
            }
        }
    });
}

    @Override
    public void offer(T annotatedImageDataRecord) {
        try {
            //add to stream
            Log.d(TAG, "Adding to stream in the offer method");
            mStream.add(annotatedImageDataRecord);
            //add to database
            mDAO.add(annotatedImageDataRecord);
        } catch (DAOException e){
            e.printStackTrace();
        }
    }

}
