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
        if(shouldShowNotification()) {
            Log.d(TAG, "Will show mood notification now. ");
            createNotificationForMood();
        }
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

    /**
     * As per the UserPreferences for wake up and sleep time, select three times during the day
     * at which the user will be shown a prompt to register their mood.
     * @return Array of integers, where each entry in the array is the number of seconds from
     * midnight at which the mood prompt should be generated.
     */
    private int[] getTimesForNotification() {
        String startTime = UserPreferences.getInstance().getPreference("startTime");
        String endTime = UserPreferences.getInstance().getPreference("endTime");
        int[] timesForNotification = new int[3];

        int startTimeInSeconds = convertHHMMtoSeconds(startTime);
        int endTimeInSeconds = convertHHMMtoSeconds(endTime);

        int partitionWindow = (endTimeInSeconds - startTimeInSeconds) / 3;
        timesForNotification[0] = startTimeInSeconds += partitionWindow;
        timesForNotification[1] = startTimeInSeconds += partitionWindow;
        timesForNotification[2] = startTimeInSeconds += partitionWindow;

        return timesForNotification;
    }

    /**
     * Given a String in the format HH:MM, returns the number of seconds from midnight.
     * @param aTime
     * @return
     */
    private int convertHHMMtoSeconds(String aTime) {
        String[] aTimeParts = aTime.split(":");
        return Integer.valueOf(aTimeParts[0]) * 3600 +
                Integer.valueOf(aTimeParts[1]) * 60;
    }

    /**
     * Gets all the times at which a notification needs to be shown, and check if the current time
     * is within a window of 15 minutes from that time. Returns true if that is the case, false
     * otherwise.
     * @return true if current time is within a 15 minutes window of a time at which a notification
     * needs to be shown. False otherwise.
     */
    private boolean shouldShowNotification() {
        Calendar c = Calendar.getInstance();
        long now = c.getTimeInMillis();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        long passed = now - c.getTimeInMillis();
        long secondsPassed = passed / 1000;

        for(int i:getTimesForNotification()) {
            Log.d(TAG, "Seconds passed: " + secondsPassed + "; Time: " + i);
            if(secondsPassed - i > 0 && secondsPassed - i < 300) {
                return true;
            }
        }
        return false;
    }

    private void createNotificationForMood() {
        MinukuStreamManager.getInstance().handleStateChangeEvent(
                new StateChangeEvent(MoodDataRecord.class));
    }
}
