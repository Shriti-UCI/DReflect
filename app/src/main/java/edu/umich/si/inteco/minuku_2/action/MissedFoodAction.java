package edu.umich.si.inteco.minuku_2.action;

import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


import java.util.HashMap;
import java.util.Map;

import edu.umich.si.inteco.minuku.config.Constants;
import edu.umich.si.inteco.minuku_2.QuestionnaireActivity;
import edu.umich.si.inteco.minuku_2.R;
import edu.umich.si.inteco.minuku_2.event.MissedFoodEvent;
import edu.umich.si.inteco.minuku_2.preferences.ApplicationConstants;
import edu.umich.si.inteco.minuku_2.question.QuestionConfig;
import edu.umich.si.inteco.minukucore.event.ShowNotificationEvent;
import edu.umich.si.inteco.minukucore.event.ShowNotificationEventBuilder;

/**
 * Created by shriti on 8/12/16.
 */
public class MissedFoodAction {

    private String TAG = "MissedFoodAction";

    public MissedFoodAction() {
        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void handleMissedFoodAction(MissedFoodEvent missedFoodEvent) {
        Log.d(TAG, "Handling no data event for  food image");
        Map<String, String> dataSentToQuestinnaireActivity = new HashMap<>();
        dataSentToQuestinnaireActivity.put(Constants.BUNDLE_KEY_FOR_QUESTIONNAIRE_ID,
                String.valueOf(QuestionConfig.missedReportQuestionnaire_2.getID()));

        EventBus.getDefault().post(
                new ShowNotificationEventBuilder()
                .setExpirationAction(ShowNotificationEvent.ExpirationAction.DISMISS)
                .setExpirationTimeSeconds(Constants.MISSED_REPORT_NOTIFICATION_EXPIRATION_TIME)
                .setViewToShow(QuestionnaireActivity.class)
                .setIconID(R.drawable.analysis)
                .setTitle("")
                .setCategory(ApplicationConstants.NOTIFICATION_CATEGORY_MISSED_ACTIVITY)
                .setMessage(Constants.MISSED_DATA_FOOD_PROMPT_TITLE)
                .setParams(dataSentToQuestinnaireActivity)
                .createShowNotificationEvent());
    }
}
