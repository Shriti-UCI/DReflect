package edu.umich.si.inteco.minuku.streamgenerator;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.CalendarContract;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;

import edu.umich.si.inteco.minuku.R;
import edu.umich.si.inteco.minuku.config.Constants;
import edu.umich.si.inteco.minuku.config.UserPreferences;
import edu.umich.si.inteco.minuku.dao.MoodDataRecordDAO;
import edu.umich.si.inteco.minuku.manager.MinukuDAOManager;
import edu.umich.si.inteco.minuku.manager.MinukuStreamManager;
import edu.umich.si.inteco.minuku.model.ImageDataRecord;
import edu.umich.si.inteco.minuku.model.MoodDataRecord;
import edu.umich.si.inteco.minuku.stream.ImageStream;
import edu.umich.si.inteco.minukucore.dao.DAOException;
import edu.umich.si.inteco.minukucore.event.IsDataExpectedEvent;
import edu.umich.si.inteco.minukucore.event.ShowNotificationEvent;
import edu.umich.si.inteco.minukucore.event.StateChangeEvent;
import edu.umich.si.inteco.minukucore.exception.StreamAlreadyExistsException;
import edu.umich.si.inteco.minukucore.exception.StreamNotFoundException;
import edu.umich.si.inteco.minukucore.manager.StreamManager;
import edu.umich.si.inteco.minukucore.stream.Stream;

/**
 * Created by shriti on 7/21/16.
 */
public class MoodStreamGenerator extends AndroidStreamGenerator<MoodDataRecord> {

    private Stream mStream;
    private String TAG = "MoodStreamGenerator";
    private MoodDataRecordDAO mMoodDataDAO;

    public MoodStreamGenerator(Context applicationContext) {
        super(applicationContext);
        this.mStream = new ImageStream(Constants.DEFAULT_QUEUE_SIZE);
        this.mMoodDataDAO = MinukuDAOManager.getInstance().getDaoFor(MoodDataRecord.class);
        this.register();
    }

    @Override
    public void register() {
        Log.d(TAG, "Registering with Stream Manager");
        try {
            MinukuStreamManager.getInstance().register(mStream, MoodDataRecord.class, this);
        } catch (StreamNotFoundException streamNotFoundException) {
            Log.e(TAG, "One of the streams on which" +
                    " MoodDataRecord/MoodStream depends in not found.");
        } catch (StreamAlreadyExistsException streamAlreadyExsistsException) {
            Log.e(TAG, "Another stream which provides" +
                    " MoodDataRecord/MoodStream is already registered.");
        }
    }

    @Override
    public Stream<MoodDataRecord> generateNewStream() {
        return mStream;
    }

    @Override
    public boolean updateStream() {
        Log.d(TAG,
                "Update Stream called: The preference values are - \n" +
                        UserPreferences.getInstance().getPreference("startTime") + "\n" +
                        UserPreferences.getInstance().getPreference("endTime"));
        MinukuStreamManager.getInstance().handleIsDataExpectedEvent(
                new IsDataExpectedEvent(MoodDataRecord.class));
        return true;
    }

    @Override
    public long getUpdateFrequency() {
        return Constants.MOOD_STREAM_GENERATOR_UPDATE_FREQUENCY_MINUTES;
    }

    @Override
    public void sendStateChangeEvent() {
        EventBus.getDefault().post(new StateChangeEvent(MoodDataRecord.class));
    }

    @Override
    public void onStreamRegistration() {
        Log.d(TAG, "Stream " + TAG + " registered successfully");
    }

    @Override
    public void offer(MoodDataRecord aMoodDataRecord) {
        mStream.add(aMoodDataRecord);
        try {
            mMoodDataDAO.add(aMoodDataRecord);
        } catch (DAOException e) {
            e.printStackTrace();
        }
    }

}
