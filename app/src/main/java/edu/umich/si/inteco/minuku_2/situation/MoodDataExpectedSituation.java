package edu.umich.si.inteco.minuku_2.situation;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import edu.umich.si.inteco.minuku.config.UserPreferences;
import edu.umich.si.inteco.minuku.manager.MinukuSituationManager;
import edu.umich.si.inteco.minuku.manager.MinukuStreamManager;
import edu.umich.si.inteco.minuku.model.MoodDataRecord;
import edu.umich.si.inteco.minuku_2.event.MoodDataExpectedActionEvent;
import edu.umich.si.inteco.minukucore.event.ActionEvent;
import edu.umich.si.inteco.minukucore.event.IsDataExpectedEvent;
import edu.umich.si.inteco.minukucore.event.MinukuEvent;
import edu.umich.si.inteco.minukucore.event.StateChangeEvent;
import edu.umich.si.inteco.minukucore.exception.DataRecordTypeNotFound;
import edu.umich.si.inteco.minukucore.model.DataRecord;
import edu.umich.si.inteco.minukucore.model.StreamSnapshot;
import edu.umich.si.inteco.minukucore.situation.Situation;

/**
 * Created by neerajkumar on 7/30/16.
 */
public class MoodDataExpectedSituation implements Situation {

    private static final String TAG = "MoodDataExpectedSituat";

    public MoodDataExpectedSituation() {
        try {
            MinukuSituationManager.getInstance().register(this);
            Log.d(TAG, "Registered successfully.");
        } catch (DataRecordTypeNotFound dataRecordTypeNotFound) {
            Log.e(TAG, "Registration failed.", dataRecordTypeNotFound);
        }
    }

    @Override
    public <T extends ActionEvent> T assertSituation(StreamSnapshot snapshot,
                                                     MinukuEvent aMinukuEvent) {
        List<DataRecord> dataRecords = new ArrayList<>();
        if(aMinukuEvent instanceof IsDataExpectedEvent) {
            Log.d(TAG, "MinukuEvent is instance of data expected event. Checking if I should show" +
                    "notification.");
            if(shouldShowNotification()) {
                Log.d(TAG, "Should show notification returned true. Sending ActionEvent.");
                return (T) new MoodDataExpectedActionEvent("TIME_FOR_MOOD_RECORDING", dataRecords);
            }

        }
        return null;
    }

    @Override
    public List<Class<? extends DataRecord>> dependsOnDataRecordType() throws DataRecordTypeNotFound {
        List<Class<? extends  DataRecord>> dependsOn = new ArrayList<>();
        dependsOn.add(MoodDataRecord.class);
        return dependsOn;
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
        if(startTime == null || endTime == null) {
            return new int[0];
        }
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
}
