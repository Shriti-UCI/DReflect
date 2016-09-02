package edu.umich.si.inteco.minuku.manager;


import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import edu.umich.si.inteco.minukucore.event.ActionEvent;
import edu.umich.si.inteco.minukucore.event.IsDataExpectedEvent;
import edu.umich.si.inteco.minukucore.event.NoDataChangeEvent;
import edu.umich.si.inteco.minukucore.event.StateChangeEvent;
import edu.umich.si.inteco.minukucore.exception.DataRecordTypeNotFound;
import edu.umich.si.inteco.minukucore.exception.StreamNotFoundException;
import edu.umich.si.inteco.minukucore.manager.SituationManager;
import edu.umich.si.inteco.minukucore.model.DataRecord;
import edu.umich.si.inteco.minukucore.model.StreamSnapshot;
import edu.umich.si.inteco.minukucore.situation.Situation;
import edu.umich.si.inteco.minuku.logger.Log;
/**
 * Created by neerajkumar on 7/23/16.
 */
public class MinukuSituationManager implements SituationManager {

    private static final String TAG = "MinikuSituationManager";
    private Map<Class<? extends DataRecord>, HashSet<Situation>> registeredSituationMap;
    private static MinukuSituationManager instance;

    private MinukuSituationManager() {
        registeredSituationMap = new HashMap<>();
    }

    public static MinukuSituationManager getInstance() {
        if(instance == null) {
            instance = new MinukuSituationManager();
        }
        return instance;
    }

    @Override
    public void onStateChange(StreamSnapshot snapshot, StateChangeEvent aStateChangeEvent) {
        Log.d(TAG, "Calling is state change  event on situation manager "
                + "Type:" + aStateChangeEvent.getType());
        if(!registeredSituationMap.containsKey(aStateChangeEvent.getType())) {
            Log.d(TAG, "Situation list for (state change)" +
                    aStateChangeEvent.getType() + " is null. Returning.");
            return;
        }
        for(Situation situation: registeredSituationMap.get(aStateChangeEvent.getType())) {
            ActionEvent actionEvent = situation.assertSituation(snapshot, aStateChangeEvent);
            if(actionEvent!=null) {
                EventBus.getDefault().post(actionEvent);
            }
        }

    }

    @Override
    public void onNoDataChange(StreamSnapshot snapshot, NoDataChangeEvent aNoDataChangeEvent) {
        Log.d(TAG, "Calling no data change event on situation manager "
                + "Type:" + aNoDataChangeEvent.getType());
        if(!registeredSituationMap.containsKey(aNoDataChangeEvent.getType())) {
            Log.d(TAG, "Situation list for (no data change)" +
                    aNoDataChangeEvent.getType() + " is null. Returning.");
            return;
        }
        for(Situation situation: registeredSituationMap.get(aNoDataChangeEvent.getType())) {
            Log.d(TAG, "Creation action event for situation " + situation.getClass());
            ActionEvent actionEvent = situation.assertSituation(snapshot, aNoDataChangeEvent);
            if(actionEvent != null) {
                EventBus.getDefault().post(actionEvent);
            }
        }
    }

    @Override
    public void onIsDataExpected(StreamSnapshot snapshot,
                                 IsDataExpectedEvent aIsDataExpectedEvent) {
        Log.d(TAG, "Calling is data expected event on situation manager "
                + "Type:" + aIsDataExpectedEvent.getType());
        if(!registeredSituationMap.containsKey(aIsDataExpectedEvent.getType())) {
            Log.d(TAG, "Situation list for (data expected)"
                    + aIsDataExpectedEvent.getType() + " is null. Returning.");
            return;
        }
        for(Situation situation: registeredSituationMap.get(aIsDataExpectedEvent.getType())) {
            ActionEvent actionEvent = situation.assertSituation(snapshot, aIsDataExpectedEvent);
            if(actionEvent != null) {
                EventBus.getDefault().post(actionEvent);
            }
        }
    }


    @Override
    public <T extends Situation> boolean register(T s) throws DataRecordTypeNotFound {

        Log.d(TAG, "Registering situation " + s.getClass().getSimpleName().toString());

        for(Class<? extends DataRecord> type: s.dependsOnDataRecordType()) {
            try {
                MinukuStreamManager.getInstance().getStreamFor(type);
                Log.d(TAG, "Registered situation successfully: " +
                        s.getClass().getSimpleName().toString());
                if(!registeredSituationMap.containsKey(type)) {
                    registeredSituationMap.put(type, new HashSet<Situation>());
                }
                return registeredSituationMap.get(type).add(s);
            } catch (StreamNotFoundException e) {
                e.printStackTrace();
                throw new DataRecordTypeNotFound();
            }
        }
        return true;
    }

    @Override
    public boolean unregister(Situation s) {
        boolean successful = true;
        for(Map.Entry<Class<? extends DataRecord>, HashSet<Situation>> entry:
                registeredSituationMap.entrySet()) {
            successful = successful && entry.getValue().remove(s);
        }
        return successful;
    }
}
