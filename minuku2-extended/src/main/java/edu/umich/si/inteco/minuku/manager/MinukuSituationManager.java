package edu.umich.si.inteco.minuku.manager;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.umich.si.inteco.minukucore.event.StateChangeEvent;
import edu.umich.si.inteco.minukucore.exception.DataRecordTypeNotFound;
import edu.umich.si.inteco.minukucore.exception.StreamNotFoundException;
import edu.umich.si.inteco.minukucore.manager.SituationManager;
import edu.umich.si.inteco.minukucore.model.DataRecord;
import edu.umich.si.inteco.minukucore.model.StreamSnapshot;
import edu.umich.si.inteco.minukucore.situation.Situation;

/**
 * Created by neerajkumar on 7/23/16.
 */
public class MinukuSituationManager implements SituationManager {

    private Map<Class<? extends DataRecord>, Set<Situation>> registeredSituationMap;
    private static MinukuSituationManager instance;

    private MinukuSituationManager() {
        this.registeredSituationMap = new HashMap<>();
    }

    public static MinukuSituationManager getInstance() {
        if(instance == null) {
            instance = new MinukuSituationManager();
        }
        return instance;
    }

    @Override
    public void onStateChange(StreamSnapshot snapshot, StateChangeEvent event) {
        for(Situation situation: registeredSituationMap.get(event.getType())) {
            EventBus.getDefault().post(situation.assertSituation(snapshot));
        }
    }

    @Override
    public <T extends Situation> boolean register(T s) throws DataRecordTypeNotFound {
        for(Class<? extends DataRecord> type: s.dependsOnDataRecordType()) {
            try {
                MinukuStreamManager.getInstance().getStreamFor(type);
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
        for(Map.Entry<Class<? extends DataRecord>, Set<Situation>> entry:
                registeredSituationMap.entrySet()) {
            successful = successful & entry.getValue().remove(s);
        }
        return successful;
    }
}
