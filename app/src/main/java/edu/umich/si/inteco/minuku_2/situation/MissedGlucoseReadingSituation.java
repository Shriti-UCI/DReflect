package edu.umich.si.inteco.minuku_2.situation;

import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import edu.umich.si.inteco.minuku.config.Constants;
import edu.umich.si.inteco.minuku.config.UserPreferences;
import edu.umich.si.inteco.minuku.manager.MinukuSituationManager;
import edu.umich.si.inteco.minuku_2.event.MissedGlucoseReadingEvent;
import edu.umich.si.inteco.minuku_2.model.GlucoseReadingImage;
import edu.umich.si.inteco.minukucore.event.ActionEvent;
import edu.umich.si.inteco.minukucore.event.MinukuEvent;
import edu.umich.si.inteco.minukucore.event.NoDataChangeEvent;
import edu.umich.si.inteco.minukucore.exception.DataRecordTypeNotFound;
import edu.umich.si.inteco.minukucore.model.DataRecord;
import edu.umich.si.inteco.minukucore.model.StreamSnapshot;
import edu.umich.si.inteco.minukucore.situation.Situation;

/**
 * Created by shriti on 7/31/16.
 */
public class MissedGlucoseReadingSituation implements Situation {

    String TAG = "MissedGlucoseReadingSituation";

    public MissedGlucoseReadingSituation() {
        try {
            MinukuSituationManager.getInstance().register(this);
            Log.d(TAG, "Registered successfully.");
        } catch (DataRecordTypeNotFound dataRecordTypeNotFound) {
            Log.e(TAG, "Registration failed.", dataRecordTypeNotFound);
        }
    }

    @Override
    public <T extends ActionEvent> T assertSituation(StreamSnapshot snapshot, MinukuEvent aMinukuEvent) {
        List<DataRecord> dataRecords = new ArrayList<>();
        dataRecords.add(snapshot.getCurrentValue(GlucoseReadingImage.class));
        if(aMinukuEvent instanceof NoDataChangeEvent) {
            Log.d(TAG, "MinukuEvent is instance of no data change event. Checking if I should check"+
                    "time passed from last image report to check for missing reports");
            if(checkLastImageReport(snapshot)) {
                Log.d(TAG, "Should show questionnaire if returned true. Sending ActionEvent.");
                return (T) new MissedGlucoseReadingEvent("MISSED_DATA_GLUCOSE_READING", dataRecords);
            }

        }
        return null;
    }

    @Override
    public List<Class<? extends DataRecord>> dependsOnDataRecordType() throws DataRecordTypeNotFound {
        List<Class<? extends  DataRecord>> dependsOn = new ArrayList<>();
        dependsOn.add(GlucoseReadingImage.class);
        return dependsOn;
    }

    private List<Integer> getTimesForCheckingLastReports() {
        Set<String> glucoseReadingTimes = UserPreferences.getInstance().getPreferenceSet("gmTimes");
        //String endTime = UserPreferences.getInstance().getPreference("endTime");

        List<Integer> timesForNotification = new LinkedList<>();

        if(glucoseReadingTimes == null) {
            return timesForNotification;
        }
        Set<Integer> glucoseReadingTimesInSeconds = new HashSet<Integer>();

        for (String time:glucoseReadingTimes) {
            glucoseReadingTimesInSeconds.add(convertHHMMtoSeconds(time));
        }
        //int endTimeInSeconds = convertHHMMtoSeconds(endTime);

        int partitionWindow = 3600;
        for (Integer time: glucoseReadingTimesInSeconds) {
            int temp = time+partitionWindow;
            Log.d(TAG, "time for glucose reading check: " + time);
            Log.d(TAG, "time for notification: " + temp);
            timesForNotification.add( temp);
        }

        return timesForNotification;
    }

    /**
     * Given a String in the format HH:MM, returns the number of seconds from midnight.
     * @param aTime
     * @return
     */
    private int convertHHMMtoSeconds(String aTime) {
        return Integer.valueOf(aTime) * 3600;
    }

    /**
     * times at which we need to check last reported image to make sure
     * that the
     * @return
     */
    private boolean checkLastImageReport(StreamSnapshot snapshot) {
        Calendar c = Calendar.getInstance();
        long now = c.getTimeInMillis();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        long passed = now - c.getTimeInMillis();
        long secondsPassed = passed / 1000;
        Log.d(TAG, "Time in seconds now is: " + secondsPassed);
        long time = 0;
        //long time = secondsPassed;
        for(int i:getTimesForCheckingLastReports()) {
            Log.d(TAG, "Seconds passed: " + secondsPassed + "; Time: " + i);
            Log.d(TAG, "upper bound for time: " + secondsPassed);
            Log.d(TAG, "Lower bound for time: " +
                    (secondsPassed - Constants.IMAGE_STREAM_GENERATOR_UPDATE_FREQUENCY_MINUTES*60));

            if (i <= secondsPassed && secondsPassed< i + Constants.IMAGE_STREAM_GENERATOR_UPDATE_FREQUENCY_MINUTES*60) {
                Log.d(TAG, "Setting the value of time");
                //time = secondsPassed;
                //time should be equal to i //TODO: check logic
                time = i; //this is the relevant notification time for the current time.
                break;
            }
        }
        if(time==0) {
            Log.d(TAG, "Situation Returning false because there is no relevant time to be checked");
            return false;
        }

        Log.d(TAG, "Set value of time is: " + time);

        if(snapshot.getCurrentValue(GlucoseReadingImage.class) != null) {
            long lastReportedTime = snapshot.getCurrentValue(GlucoseReadingImage.class).getCreationTime();
            Log.d(TAG, "Last reported time: " + lastReportedTime);
            long lastReportedTimeInSeconds = (lastReportedTime - c.getTimeInMillis())/1000;
            Log.d(TAG, "Last reported time in seconds : " + lastReportedTimeInSeconds);
            Log.d(TAG, "time - lastReportedTimeInSec: " + (time - lastReportedTimeInSeconds));
            if((time - lastReportedTimeInSeconds) > (3600*2)) {// it means 2 hours
                // TODO: there should also be = condition, last report can be >= to notificaiton check time minus delta
                Log.d(TAG, "Situation returning true");
                return true;
            }
            else {
                Log.d(TAG, "Situation returning false");
                return false;
            }
        } else {
            Log.d(TAG, "current value from snapshot is null. Situation returning true");
            //wait till notification time ("time from above") and then return true
            return true;
        }
    }
}
