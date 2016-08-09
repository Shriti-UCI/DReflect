package edu.umich.si.inteco.minuku_2.situation;

import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import edu.umich.si.inteco.minuku.manager.MinukuSituationManager;
import edu.umich.si.inteco.minuku.model.MoodDataRecord;
import edu.umich.si.inteco.minuku_2.event.MoodAnnotationExpectedActionEvent;
import edu.umich.si.inteco.minuku_2.event.MoodDataExpectedActionEvent;
import edu.umich.si.inteco.minukucore.event.ActionEvent;
import edu.umich.si.inteco.minukucore.event.IsDataExpectedEvent;
import edu.umich.si.inteco.minukucore.event.MinukuEvent;
import edu.umich.si.inteco.minukucore.event.StateChangeEvent;
import edu.umich.si.inteco.minukucore.exception.DataRecordTypeNotFound;
import edu.umich.si.inteco.minukucore.model.DataRecord;
import edu.umich.si.inteco.minukucore.model.StreamSnapshot;
import edu.umich.si.inteco.minukucore.situation.Situation;
import edu.umich.si.inteco.minukucore.stream.Stream;

/**
 * Created by shriti on 8/2/16.
 */
public class MoodAnnotationExpectedSituation implements Situation {

    //tirggered when there is a new mood - in the offer of stream generator
    //check the last reported/current value of mood
    //check the previous value of mood
    //if too much change, then ask a question

    private String TAG = "MoodAnnotationExpectedSituation";

    public MoodAnnotationExpectedSituation() {
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
        if(aMinukuEvent instanceof StateChangeEvent) {
            Log.d(TAG, "MinukuEvent is instance of state change event. Checking if I should show" +
                    "notification.");
            if(shouldAskForAnnotation(snapshot)) {
                Log.d(TAG, "Should show notification returned true. Sending ActionEvent.");
                return (T) new MoodAnnotationExpectedActionEvent("EXPLAIN_MOOD_CHANGES",
                        dataRecords);
            }

        }
        return null;    }

    @Override
    public List<Class<? extends DataRecord>> dependsOnDataRecordType() throws DataRecordTypeNotFound {
        List<Class<? extends  DataRecord>> dependsOn = new ArrayList<>();
        dependsOn.add(MoodDataRecord.class);
        return dependsOn;
    }

    private boolean shouldAskForAnnotation(StreamSnapshot snapshot) {

        Log.d(TAG, "Calling method from assert situation");
        if (snapshot.getCurrentValue(MoodDataRecord.class) != null &&
                snapshot.getPreviousValue(MoodDataRecord.class) != null) {
            MoodDataRecord currentMood = snapshot.getCurrentValue(MoodDataRecord.class);
            Log.d(TAG, "current val energy: " + currentMood.getEnergyLevel());
            Log.d(TAG, "current val mood: " + currentMood.getMoodLevel());

            MoodDataRecord previousMood = snapshot.getPreviousValue(MoodDataRecord.class);
            Log.d(TAG, "previous val energy: " + previousMood.getEnergyLevel());
            Log.d(TAG, "previous val mood: " + previousMood.getMoodLevel());

            if(areDatesEqual(currentMood.getCreationTime(), previousMood.getCreationTime())) {
                Log.d(TAG, "both moods were recorded on the same day");
                float moodLevelDiff = Math.abs(currentMood.getMoodLevel() - previousMood.getMoodLevel());
                Log.d(TAG, "different in mood levels: " + moodLevelDiff);
                float energyLevelDiff = Math.abs(currentMood.getEnergyLevel() - previousMood.getEnergyLevel());
                Log.d(TAG, "different in energy levels: " + energyLevelDiff);

                if (moodLevelDiff > 5 || energyLevelDiff > 5) {
                    Log.d(TAG, "returing true for annotation expected");
                    return true;
                }
            }
        }
        Log.d(TAG, "returing false for annotation expected");
        return false;
    }

    private boolean areDatesEqual(long currentTime, long previousTime) {
        Log.d(TAG, "checking if the both moods were recorded on the same day");
        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(currentTime);
        Date currentDate = calendar.getTime();

        calendar.setTimeInMillis(previousTime);
        Date previousDate = calendar.getTime();

        return (currentDate.equals(previousDate));
    }
}
