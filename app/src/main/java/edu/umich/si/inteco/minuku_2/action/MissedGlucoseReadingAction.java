package edu.umich.si.inteco.minuku_2.action;

import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;

import edu.umich.si.inteco.minuku.config.Constants;
import edu.umich.si.inteco.minuku_2.QuestionnaireActivity;
import edu.umich.si.inteco.minuku_2.R;
import edu.umich.si.inteco.minuku_2.event.MissedGlucoseReadingEvent;
import edu.umich.si.inteco.minuku_2.event.MoodDataExpectedActionEvent;
import edu.umich.si.inteco.minukucore.event.ShowNotificationEvent;
import edu.umich.si.inteco.minukucore.event.ShowNotificationEventBuilder;

/**
 * Created by shriti on 8/1/16.
 */
public class MissedGlucoseReadingAction {

    private static final String TAG = "MisdGlucoseReadingActn";

    public MissedGlucoseReadingAction() {
        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void handleMissedGlucoseReading(MissedGlucoseReadingEvent missedDataActionEvent) {
        Log.d(TAG, "Handling no data event for Glucose Reading Image");
        EventBus.getDefault().post(
                new ShowNotificationEventBuilder()
                        .setExpirationAction(ShowNotificationEvent.ExpirationAction.DISMISS)
                        .setExpirationTimeSeconds(Constants.MISSED_REPORT_NOTIFICATION_EXPIRATION_TIME)
                        .setViewToShow(QuestionnaireActivity.class)
                        .setIconID(R.drawable.analysis)
                        .setTitle(Constants.MISSED_DATA_GLUCOSE_READING_PROMPT_TITLE)
                        .setMessage("")
                        .setParams(new HashMap<String, String>())
                        .createShowNotificationEvent());    }
}
