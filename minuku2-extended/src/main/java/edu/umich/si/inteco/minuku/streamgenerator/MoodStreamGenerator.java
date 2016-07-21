package edu.umich.si.inteco.minuku.streamgenerator;

import android.content.Context;
import android.util.Log;

import edu.umich.si.inteco.minuku.config.Constants;
import edu.umich.si.inteco.minuku.dao.MoodDataRecordDAO;
import edu.umich.si.inteco.minuku.manager.MinukuDAOManager;
import edu.umich.si.inteco.minuku.manager.MinukuStreamManager;
import edu.umich.si.inteco.minuku.model.ImageDataRecord;
import edu.umich.si.inteco.minuku.model.MoodDataRecord;
import edu.umich.si.inteco.minuku.stream.ImageStream;
import edu.umich.si.inteco.minukucore.dao.DAOException;
import edu.umich.si.inteco.minukucore.exception.StreamAlreadyExistsException;
import edu.umich.si.inteco.minukucore.exception.StreamNotFoundException;
import edu.umich.si.inteco.minukucore.stream.Stream;

/**
 * Created by shriti on 7/21/16.
 */
public class MoodStreamGenerator extends AndroidStreamGenerator<MoodDataRecord> {

    private Stream mStream;
    private String TAG = "MoodStreamGenerator";
    private MoodDataRecordDAO mDAO;

    public MoodStreamGenerator(Context applicationContext) {
        super(applicationContext);
        this.mStream = new ImageStream(Constants.DEFAULT_QUEUE_SIZE);
        this.mDAO = MinukuDAOManager.getInstance().getDaoFor(MoodDataRecord.class);
        this.register();
    }

    @Override
    public void register() {
        Log.d(TAG, "Registering with Stream Manager");
        try {
            MinukuStreamManager.getInstance().register(mStream, MoodDataRecord.class, this);
        } catch (StreamNotFoundException streamNotFoundException) {
            Log.e(TAG, "One of the streams on which MoodDataRecord/MoodStream depends in not found.");
        } catch (StreamAlreadyExistsException streamAlreadyExsistsException) {
            Log.e(TAG, "Another stream which provides MoodDataRecord/MoodStream is already registered.");
        }
    }

    @Override
    public Stream<MoodDataRecord> generateNewStream() {
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
    public void offer(MoodDataRecord aMoodDataRecord) {
        //add to stream
        mStream.add(aMoodDataRecord);
        //add to database
        try {
            mDAO.add(aMoodDataRecord);
        } catch (DAOException e) {
            e.printStackTrace();
        }

    }
}
