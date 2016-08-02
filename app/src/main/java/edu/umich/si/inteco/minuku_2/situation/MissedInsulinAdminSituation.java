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
import edu.umich.si.inteco.minuku_2.event.MissedInsulinAdminEvent;
import edu.umich.si.inteco.minuku_2.model.GlucoseReadingImage;
import edu.umich.si.inteco.minuku_2.model.InsulinAdminImage;
import edu.umich.si.inteco.minukucore.event.ActionEvent;
import edu.umich.si.inteco.minukucore.event.MinukuEvent;
import edu.umich.si.inteco.minukucore.event.NoDataChangeEvent;
import edu.umich.si.inteco.minukucore.exception.DataRecordTypeNotFound;
import edu.umich.si.inteco.minukucore.model.DataRecord;
import edu.umich.si.inteco.minukucore.model.StreamSnapshot;
import edu.umich.si.inteco.minukucore.situation.Situation;

/**
 * Created by shriti on 8/1/16.
 */
public class MissedInsulinAdminSituation implements Situation {

    String TAG = "MissedInsulinAdminSituation";

    public MissedInsulinAdminSituation() {
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
        dataRecords.add(snapshot.getCurrentValue(InsulinAdminImage.class));
        if(aMinukuEvent instanceof NoDataChangeEvent) {
            Log.d(TAG, "MinukuEvent is instance of no data change event. Checking if I should check"+
                    "time passed from last image report to check for missing reports");
            if(checkLastImageReport(snapshot)) {
                Log.d(TAG, "Should show questionnaire if returned true. Sending ActionEvent.");
                return (T) new MissedInsulinAdminEvent("MISSED_DATA_INSULIN_ADMIN", dataRecords);
            }

        }
        return null;    }

    @Override
    public List<Class<? extends DataRecord>> dependsOnDataRecordType() throws DataRecordTypeNotFound {
        List<Class<? extends  DataRecord>> dependsOn = new ArrayList<>();
        dependsOn.add(GlucoseReadingImage.class);
        return dependsOn;
    }

    private List<Integer> getTimesForCheckingLastReports() {
        Set<String> insulinAdminTimes = UserPreferences.getInstance().getPreferenceSet("insulinTimes");
        //String endTime = UserPreferences.getInstance().getPreference("endTime");

        List<Integer> timesForNotification = new LinkedList<>();

        if(insulinAdminTimes == null) {
            return timesForNotification;
        }
        Set<Integer> insulinAdminTimesInSeconds = new HashSet<Integer>();

        for (String time:insulinAdminTimes) {
            insulinAdminTimesInSeconds.add(convertHHMMtoSeconds(time));
        }
        //int endTimeInSeconds = convertHHMMtoSeconds(endTime);

        int partitionWindow = 1800; //30 mins
        for (Integer time: insulinAdminTimesInSeconds) {
            timesForNotification.add( time + partitionWindow);
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

        long time = secondsPassed;
        for(int i:getTimesForCheckingLastReports()) {
            Log.d(TAG, "Seconds passed: " + secondsPassed + "; Time: " + i);
            if (i <= secondsPassed && secondsPassed< i + Constants.IMAGE_STREAM_GENERATOR_UPDATE_FREQUENCY_MINUTES*60) {
                time = secondsPassed;
                break;
            }
        }

        if(snapshot.getCurrentValue(InsulinAdminImage.class) != null) {
            long lastReportedTime = snapshot.getCurrentValue(InsulinAdminImage.class).getCreationTime();
            long lastReportedTimeInSeconds = (lastReportedTime - c.getTimeInMillis())/1000;
            if((time - lastReportedTimeInSeconds) > (3600*2)) // it means 2 hours
                return true;
            else
                return false;
        } else {
            return true;
        }
    }
}
