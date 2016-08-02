package edu.umich.si.inteco.minuku.streamgenerator;

import android.content.Context;
import android.util.Log;

import edu.umich.si.inteco.minuku.config.Constants;
import edu.umich.si.inteco.minuku.dao.AnnotatedImageDataRecordDAO;
import edu.umich.si.inteco.minuku.dao.ImageDataRecordDAO;
import edu.umich.si.inteco.minuku.manager.MinukuDAOManager;
import edu.umich.si.inteco.minuku.manager.MinukuStreamManager;
import edu.umich.si.inteco.minuku.model.AnnotatedImageDataRecord;
import edu.umich.si.inteco.minuku.model.ImageDataRecord;
import edu.umich.si.inteco.minuku.stream.AnnotatedImageStream;
import edu.umich.si.inteco.minuku.stream.ImageStream;
import edu.umich.si.inteco.minukucore.dao.DAOException;
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
    private String TAG = "AnnotatedImageStreamGenerator";
    private AnnotatedImageDataRecord imageDataRecord;
    private Class<T> mDataRecordType;

    AnnotatedImageDataRecordDAO mDAO;

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
        Log.d(TAG, "Update Stream called: Currently doing nothing on this stream");
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

    }

    @Override
    public void offer(T annotatedImageDataRecord) {
        try {
            //add to stream
            mStream.add(annotatedImageDataRecord);
            //add to database
            mDAO.add(annotatedImageDataRecord);
        } catch (DAOException e){
            e.printStackTrace();
        }
    }

}
