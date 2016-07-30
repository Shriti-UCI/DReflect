package edu.umich.si.inteco.minuku.streamgenerator;

import android.content.Context;
import android.util.Log;
import edu.umich.si.inteco.minuku.config.Constants;
import edu.umich.si.inteco.minuku.dao.FreeResponseDAO;
import edu.umich.si.inteco.minuku.manager.MinukuDAOManager;
import edu.umich.si.inteco.minuku.manager.MinukuStreamManager;
import edu.umich.si.inteco.minuku.stream.FreeResponseQuestionStream;
import edu.umich.si.inteco.minukucore.dao.DAOException;
import edu.umich.si.inteco.minukucore.exception.StreamAlreadyExistsException;
import edu.umich.si.inteco.minukucore.exception.StreamNotFoundException;
import edu.umich.si.inteco.minukucore.model.question.FreeResponse;
import edu.umich.si.inteco.minukucore.stream.Stream;

/**
 * Created by shriti on 7/28/16.
 */
public class FreeResponseQuestionStreamGenerator extends AndroidStreamGenerator<FreeResponse> {

    private Stream mStream;
    private String TAG = "FreeResponseQuestionStreamGenerator";
    private FreeResponseDAO mDAO;

    public FreeResponseQuestionStreamGenerator(Context aApplicationContext) {
        super(aApplicationContext);
        this.mStream = new FreeResponseQuestionStream(Constants.DEFAULT_QUEUE_SIZE);
        this.mDAO = MinukuDAOManager.getInstance().getDaoFor(FreeResponse.class);
        this.register();
    }

    @Override
    public void register() {
        Log.d(TAG, "Registering with Stream Manager");
        try {
            MinukuStreamManager.getInstance().register(mStream, FreeResponse.class, this);
        } catch (StreamNotFoundException streamNotFoundException) {
            Log.e(TAG, "One of the streams on which FreeResponseQuestion/FreeResponseQuestionStream depends in not found.");
        } catch (StreamAlreadyExistsException streamAlreadyExsistsException) {
            Log.e(TAG, "Another stream which provides FreeResponseQuestion/FreeResponseQuestionStream is already registered.");
        }
    }

    @Override
    public Stream<FreeResponse> generateNewStream() {
        return mStream;
    }

    @Override
    public boolean updateStream() {
        Log.d(TAG, "Update Stream called: Currently doing nothing on this stream");
        return true;    }

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
    public void offer(FreeResponse aMissdedReportQuestion) {
        mStream.add(aMissdedReportQuestion);
        try {
            mDAO.add(aMissdedReportQuestion);
        } catch (DAOException e) {
            e.printStackTrace();
        }
    }
}
