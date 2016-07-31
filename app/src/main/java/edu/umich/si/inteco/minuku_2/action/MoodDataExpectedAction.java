package edu.umich.si.inteco.minuku_2.action;

import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import edu.umich.si.inteco.minuku.config.Constants;
import edu.umich.si.inteco.minuku_2.event.MoodDataExpectedActionEvent;
import edu.umich.si.inteco.minukucore.event.ShowNotificationEvent;

/**
 * Created by neerajkumar on 7/30/16.
 */
public class MoodDataExpectedAction {
    private static final String TAG = "MoodDataExpectedAction";

    public MoodDataExpectedAction() {
        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void handleMoodDataExpectedEvent(MoodDataExpectedActionEvent expectedActionEvent) {
        Log.d(TAG, "Handling mood data expected event");
        EventBus.getDefault().post(
                new ShowNotificationEvent(Constants.MOOD_REMINDER_TITLE));
    }
}
