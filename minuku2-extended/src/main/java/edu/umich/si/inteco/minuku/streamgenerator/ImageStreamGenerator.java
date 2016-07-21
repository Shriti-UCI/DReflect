package edu.umich.si.inteco.minuku.streamgenerator;

import android.content.Context;
import android.util.Log;

import edu.umich.si.inteco.minuku.config.Constants;
import edu.umich.si.inteco.minuku.dao.ImageDataRecordDAO;
import edu.umich.si.inteco.minuku.manager.MinukuDAOManager;
import edu.umich.si.inteco.minuku.manager.MinukuStreamManager;
import edu.umich.si.inteco.minuku.model.ImageDataRecord;
import edu.umich.si.inteco.minuku.stream.ImageStream;
import edu.umich.si.inteco.minukucore.dao.DAOException;
import edu.umich.si.inteco.minukucore.exception.StreamAlreadyExistsException;
import edu.umich.si.inteco.minukucore.exception.StreamNotFoundException;
import edu.umich.si.inteco.minukucore.stream.Stream;

/**
 * Created by shriti on 7/19/16.
 */
public class ImageStreamGenerator extends AndroidStreamGenerator<ImageDataRecord> {

    private ImageStream mStream;
    private String TAG = "ImageStreamGenerator";
    private ImageDataRecord imageDataRecord;

    ImageDataRecordDAO mDAO;

    public ImageStreamGenerator(Context applicationContext) {
        super(applicationContext);
        this.mStream = new ImageStream(Constants.DEFAULT_QUEUE_SIZE);
        this.mDAO = MinukuDAOManager.getInstance().getDaoFor(ImageDataRecord.class);
        this.register();
    }

    @Override
    public void register() {
        Log.d(TAG, "Registering with Stream Manager");
        try {
            MinukuStreamManager.getInstance().register(mStream, ImageDataRecord.class, this);
        } catch (StreamNotFoundException streamNotFoundException) {
            Log.e(TAG, "One of the streams on which ImageDataRecord/ImageStream depends in not found.");
        } catch (StreamAlreadyExistsException streamAlreadyExsistsException) {
            Log.e(TAG, "Another stream which provides ImageDataRecord/ImageStream is already registered.");
        }
    }

    @Override
    public Stream<ImageDataRecord> generateNewStream() {
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
    public void offer(ImageDataRecord anImageDataRecord) {
        try {
            //add to stream
            mStream.add(anImageDataRecord);
            //add to database
            mDAO.add(anImageDataRecord);
        } catch (DAOException e){
            e.printStackTrace();
        }
    }
}
