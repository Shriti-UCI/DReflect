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
 */
public class AnnotatedImageStreamGenerator extends AndroidStreamGenerator<AnnotatedImageDataRecord> {

    private AnnotatedImageStream mStream;
    private String TAG = "AnnotatedImageStreamGenerator";
    private AnnotatedImageDataRecord imageDataRecord;

    AnnotatedImageDataRecordDAO mDAO;

    public AnnotatedImageStreamGenerator(Context applicationContext) {
        super(applicationContext);
        this.mStream = new AnnotatedImageStream(Constants.DEFAULT_QUEUE_SIZE);
        this.mDAO = MinukuDAOManager.getInstance().getDaoFor(AnnotatedImageDataRecord.class);
        this.register();
    }

    @Override
    public void register() {
        Log.d(TAG, "Registering with Stream Manager");
        try {
            MinukuStreamManager.getInstance().register(mStream, AnnotatedImageDataRecord.class, this);
        } catch (StreamNotFoundException streamNotFoundException) {
            Log.e(TAG, "One of the streams on which ImageDataRecord/ImageStream depends in not found.");
        } catch (StreamAlreadyExistsException streamAlreadyExsistsException) {
            Log.e(TAG, "Another stream which provides ImageDataRecord/ImageStream is already registered.");
        }
    }

    @Override
    public Stream<AnnotatedImageDataRecord> generateNewStream() {
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
    public void offer(AnnotatedImageDataRecord annotatedImageDataRecord) {
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
