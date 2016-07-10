package edu.umich.si.inteco.minukucore.manager;

import edu.umich.si.inteco.minukucore.model.StreamSnapshot;
import edu.umich.si.inteco.minukucore.situation.Situation;

/**
 * Created by shriti on 7/9/16.
 */
public interface SituationManager {

    public void onStateChange(StreamSnapshot s);

    public boolean register(Situation s);

    public boolean unregister(Situation s);
}
