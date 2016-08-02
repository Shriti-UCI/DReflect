package edu.umich.si.inteco.minuku_2.action;

import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import edu.umich.si.inteco.minuku.config.Constants;
import edu.umich.si.inteco.minuku_2.event.MissedGlucoseReadingEvent;
import edu.umich.si.inteco.minuku_2.event.MissedInsulinAdminEvent;
import edu.umich.si.inteco.minukucore.event.ShowNotificationEvent;

/**
 * Created by shriti on 8/2/16.
 */
public class MissedInsulinAdminAction {
    private static final String TAG = "MisdInsulinAdminAction";

    public MissedInsulinAdminAction() {
        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void handleMissedInsulinAdmin(MissedInsulinAdminEvent missedDataActionEvent) {
        Log.d(TAG, "Handling no data event for Insulin Admin Image");
        EventBus.getDefault().post(
                new ShowNotificationEvent(Constants.MISSED_DATA_INSULIN_ADMIN_PROMPT_TITLE));
    }
}
