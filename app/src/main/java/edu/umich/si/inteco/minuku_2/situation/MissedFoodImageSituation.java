package edu.umich.si.inteco.minuku_2.situation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import edu.umich.si.inteco.minuku.config.UserPreferences;
import edu.umich.si.inteco.minuku.logger.Log;
import edu.umich.si.inteco.minuku.manager.MinukuSituationManager;
import edu.umich.si.inteco.minuku_2.event.MissedFoodEvent;
import edu.umich.si.inteco.minuku_2.model.FoodImage;
import edu.umich.si.inteco.minukucore.event.ActionEvent;
import edu.umich.si.inteco.minukucore.event.MinukuEvent;
import edu.umich.si.inteco.minukucore.event.NoDataChangeEvent;
import edu.umich.si.inteco.minukucore.exception.DataRecordTypeNotFound;
import edu.umich.si.inteco.minukucore.model.DataRecord;
import edu.umich.si.inteco.minukucore.model.StreamSnapshot;
import edu.umich.si.inteco.minukucore.situation.Situation;

/**
 * Created by shriti on 8/12/16.
 */
public class MissedFoodImageSituation implements Situation {

    private String TAG = "MissedFoodImageSituation";

    public MissedFoodImageSituation() {
        try {
            MinukuSituationManager.getInstance().register(this);
            Log.d(TAG, "Registered successfully.");
        } catch (DataRecordTypeNotFound dataRecordTypeNotFound) {
            Log.e(TAG, "Registration failed.");
            dataRecordTypeNotFound.printStackTrace();
        }
    }

    @Override
    public <T extends ActionEvent> T assertSituation(StreamSnapshot snapshot, MinukuEvent aMinukuEvent) {
        Log.d(TAG, "The type of minuku event received is:" + aMinukuEvent.getType().getSimpleName() );
        if(!aMinukuEvent.getType().equals(FoodImage.class)) {
            Log.e(TAG, "Something is fu**ed up. Expected type :" +
                    " FoodImage , Received:" + aMinukuEvent.getType() );
        }
        List<DataRecord> dataRecords = new ArrayList<>();
        dataRecords.add(snapshot.getCurrentValue(FoodImage.class));
        if (aMinukuEvent instanceof NoDataChangeEvent) {
            Log.d(TAG, "MinukuEvent is instance of no data change event. Checking if I should check" +
                    "time passed from last image report to check for missing reports");
            if (checkLastImageReport(snapshot)) {
                Log.d(TAG, "Should show questionnaire if returned true. Sending ActionEvent.");
                return (T) new MissedFoodEvent("MISSED_DATA_FOOD", dataRecords);
            }

        }
        return null;
    }

    @Override
    public List<Class<? extends DataRecord>> dependsOnDataRecordType() throws DataRecordTypeNotFound {
        List<Class<? extends  DataRecord>> dependsOn = new ArrayList<>();
        dependsOn.add(FoodImage.class);
        return dependsOn;
    }

    private boolean checkLastImageReport(StreamSnapshot snapshot) {

        Calendar c = Calendar.getInstance();
        long now = c.getTimeInMillis();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        long passed = now - c.getTimeInMillis();
        long secondsPassed = passed / 1000; /*time now*/

        //compare now with start and end time
        String endTime = UserPreferences.getInstance().getPreference("endTime");
        Log.d(TAG, "end time " + endTime);
        String startTime = UserPreferences.getInstance().getPreference("startTime");
        Log.d(TAG, "start time " + startTime);
        if(endTime!=null && startTime!=null) {
            int endTimeInSeconds = convertHHMMtoSeconds(endTime);
            Log.d(TAG, "end time in seconds " + endTimeInSeconds);
            int startTimeInSeconds = convertHHMMtoSeconds(startTime);
            Log.d(TAG, "start time in seconds " + startTimeInSeconds);

            if (secondsPassed > endTimeInSeconds || secondsPassed < startTimeInSeconds) {
                Log.d(TAG, "Situation returning false because time now is beyond start or end time" +
                        "for the user");
                return false;
            }
        }

        Log.d(TAG, "Time now is in the range of startTime and endTime for user");

        //if the last reported image was 3 hours before from now, return true
        if (snapshot.getCurrentValue(FoodImage.class) != null) {
            long lastReportedTime = snapshot.getCurrentValue(FoodImage.class).getCreationTime();
            long lastReportedTimeInSeconds = (lastReportedTime - c.getTimeInMillis()) / 1000;
            if ((secondsPassed - lastReportedTimeInSeconds) > (3600 * 3)) {
                Log.d(TAG, "Situation returning true"); /*3 hours*/
                return true;
            }
            else {
                Log.d(TAG, "Situation returning false");
                return false;
            }
        } else {
            Log.d(TAG, "current value from snapshot is null. Situation returning true");
            return true;
        }
    }

    /**
     * Given a String in the format HH:MM, returns the number of seconds from midnight.
     * @param aTime
     * @return
     */
    private int convertHHMMtoSeconds(String aTime) {
        //atime example: "23:55" , length =5 0-1, 3-4
        int timeInseconds = 0;
        String hour =null;
        String minutes = null;
        if(aTime!=null) {
            String[] time = aTime.split(":");
            if(time.length>0) {
                hour = time[0];
                timeInseconds = timeInseconds + Integer.valueOf(hour)*3600;
            }
            if(time.length>1) {
                minutes = time[1];
                timeInseconds = timeInseconds + Integer.valueOf(minutes)*60;
            }
            Log.d(TAG, "hour: " + hour + "minutes: " + minutes);
        }
        return timeInseconds;
    }

}
