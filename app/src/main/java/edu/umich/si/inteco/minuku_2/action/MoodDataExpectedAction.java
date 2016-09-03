package edu.umich.si.inteco.minuku_2.action;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;

import edu.umich.si.inteco.minuku.config.Constants;
import edu.umich.si.inteco.minuku.logger.Log;
import edu.umich.si.inteco.minuku_2.MoodDataRecordActivity;
import edu.umich.si.inteco.minuku_2.R;
import edu.umich.si.inteco.minuku_2.event.MoodDataExpectedActionEvent;
import edu.umich.si.inteco.minuku_2.preferences.ApplicationConstants;
import edu.umich.si.inteco.minukucore.event.ShowNotificationEvent;
import edu.umich.si.inteco.minukucore.event.ShowNotificationEventBuilder;

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
                new ShowNotificationEventBuilder()
                        .setExpirationAction(ShowNotificationEvent.ExpirationAction.DISMISS)
                        .setExpirationTimeSeconds(Constants.MOOD_NOTIFICATION_EXPIRATION_TIME)
                        .setViewToShow(MoodDataRecordActivity.class)
                        .setIconID(R.drawable.self_reflection)
                        .setTitle(Constants.MOOD_REMINDER_TITLE)
                        .setMessage(Constants.MOOD_REMINDER_MESSAGE)
                        .setCategory(ApplicationConstants.NOTIFICATION_CATEGORY_MOOD_REPORT)
                        .setParams(new HashMap<String, String>())
                        .createShowNotificationEvent());
    }
}
