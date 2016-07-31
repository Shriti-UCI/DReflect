package edu.umich.si.inteco.minuku_2.situation;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.umich.si.inteco.minuku.manager.MinukuSituationManager;
import edu.umich.si.inteco.minuku.model.MoodDataRecord;
import edu.umich.si.inteco.minuku_2.event.MoodDataExpectedActionEvent;
import edu.umich.si.inteco.minukucore.event.ActionEvent;
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
    public <T extends ActionEvent> T assertSituation(StreamSnapshot snapshot) {
        List<DataRecord> dataRecords = new ArrayList<>();
        return (T) new MoodDataExpectedActionEvent("TIME_FOR_MOOD_RECORDING", dataRecords);
    }

    @Override
    public List<Class<? extends DataRecord>> dependsOnDataRecordType() throws DataRecordTypeNotFound {
        List<Class<? extends  DataRecord>> dependsOn = new ArrayList<>();
        dependsOn.add(MoodDataRecord.class);
        return dependsOn;
    }
}
