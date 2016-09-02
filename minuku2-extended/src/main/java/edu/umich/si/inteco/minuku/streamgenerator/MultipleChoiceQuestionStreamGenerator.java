package edu.umich.si.inteco.minuku.streamgenerator;

import android.content.Context;

import edu.umich.si.inteco.minuku.config.Constants;
import edu.umich.si.inteco.minuku.dao.MultipleChoiceQuestionDAO;
import edu.umich.si.inteco.minuku.logger.Log;
import edu.umich.si.inteco.minuku.manager.MinukuDAOManager;
import edu.umich.si.inteco.minuku.manager.MinukuStreamManager;
import edu.umich.si.inteco.minuku.stream.MultipleChoiceQuestionStream;
import edu.umich.si.inteco.minukucore.dao.DAOException;
import edu.umich.si.inteco.minukucore.exception.StreamAlreadyExistsException;
import edu.umich.si.inteco.minukucore.exception.StreamNotFoundException;
import edu.umich.si.inteco.minukucore.model.question.MultipleChoice;
import edu.umich.si.inteco.minukucore.stream.Stream;

/**
 * Created by shriti on 7/31/16.
 */
public class MultipleChoiceQuestionStreamGenerator extends AndroidStreamGenerator<MultipleChoice> {

    private Stream mStream;
    private String TAG = "MultipleChoiceQuestionStreamGenerator";
    private MultipleChoiceQuestionDAO mDAO;

    public MultipleChoiceQuestionStreamGenerator(Context aApplicationContext) {
        super(aApplicationContext);
        this.mStream = new MultipleChoiceQuestionStream(Constants.DEFAULT_QUEUE_SIZE);
        this.mDAO = MinukuDAOManager.getInstance().getDaoFor(MultipleChoice.class);
        this.register();
    }

    @Override
    public void register() {
        Log.d(TAG, "Registering with Stream Manager");
        try {
            MinukuStreamManager.getInstance().register(mStream, MultipleChoice.class, this);
        } catch (StreamNotFoundException streamNotFoundException) {
            Log.e(TAG, "One of the streams on which FreeResponseQuestion/FreeResponseQuestionStream depends in not found.");
        } catch (StreamAlreadyExistsException streamAlreadyExsistsException) {
            Log.e(TAG, "Another stream which provides FreeResponseQuestion/FreeResponseQuestionStream is already registered.");
        }
    }

    @Override
    public Stream<MultipleChoice> generateNewStream() {
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
        /*Log.d(TAG, "Stream " + TAG + " registered successfully");
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try
                {
                    Log.d(TAG, "Stream " + TAG + "initialized from previous state");
                    Future<List<MultipleChoice>> listFuture =
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
                }
            }
        });*/
    }

    @Override
    public void offer(MultipleChoice aQuestion) {
        mStream.add(aQuestion);
        try {
            mDAO.add(aQuestion);
        } catch (DAOException e) {
            e.printStackTrace();
        }
    }
}
