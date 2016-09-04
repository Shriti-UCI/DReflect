package edu.umich.si.inteco.minuku_2;

import android.os.Bundle;
import android.widget.TextView;

import java.util.HashMap;

import edu.umich.si.inteco.minuku.logger.Log;
import edu.umich.si.inteco.minuku_2.manager.InstanceManager;
import edu.umich.si.inteco.minuku_2.preferences.ApplicationConstants;

/**
 * Created by shriti on 8/25/16.
 */
public class DisplayCreditActivity extends BaseActivity {

    private TextView generalMessage;
    private TextView remainingTasks;
    private TextView totalReportCount;
    private TextView rewardCount;

    private String TAG = "DisplayCreditActivity";
    private HashMap<String, String> compensationMessages;

    private final String GENERAL_MESSAGE = "general_message";
    private final String REMAINING_TASKS = "remaining_tasks";
    private final String TOTAL_RESPONSE_COUNT = "total_response_count";
    private final String REWARD_COUNT = "reward_count";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.credit_activity);

        generalMessage = (TextView) findViewById(R.id.general_compensation_message);
        remainingTasks = (TextView) findViewById(R.id.remaining_tasks);
        totalReportCount = (TextView) findViewById(R.id.total_response_count);
        rewardCount = (TextView) findViewById(R.id.reward_count);

        compensationMessages = getCompensationMessage();

        generalMessage.setText(compensationMessages.get(GENERAL_MESSAGE));
        remainingTasks.setText("Reports needed to earn today's reward : " + compensationMessages.get(REMAINING_TASKS));
        totalReportCount.setText("Total responses for today : " + compensationMessages.get(TOTAL_RESPONSE_COUNT));
        String reward = compensationMessages.get(REWARD_COUNT);
        rewardCount.setText("Total reward for today : $" + String.format("%.2f", Double.valueOf(reward)));
    }

    /**
     *
     * @return
     */
    public HashMap getCompensationMessage() {
        HashMap<String, String>compensationMessages = new HashMap<>();

        /**int relevantDataCount = mUserSubmissionStats.getFoodCount()+
                mUserSubmissionStats.getGlucoseReadingCount()+
                mUserSubmissionStats.getInsulinCount()+
                mUserSubmissionStats.getMoodCount();**/

        int relevantDataCount = getRewardRelevantSubmissionCount(
                InstanceManager.getInstance(getApplicationContext()).getUserSubmissionStats());

        double reward;
        double compensationAmount = relevantDataCount*0.10;
        if(compensationAmount<=1)
            reward=compensationAmount;
        else
            reward = 1.0;

        if(relevantDataCount>= ApplicationConstants.MIN_REPORTS_TO_GET_REWARD){
            Log.d(TAG, "User has required number of data points for today");

            compensationMessages.put(GENERAL_MESSAGE, "You are now eligible for today's reward!");
            compensationMessages.put(REMAINING_TASKS, "End of day diary");
            compensationMessages.put(TOTAL_RESPONSE_COUNT, String.valueOf(relevantDataCount));
            compensationMessages.put(REWARD_COUNT, String.valueOf(reward));
        }
        else {
            int remainingDataCount = 2-relevantDataCount;
            compensationMessages.put(GENERAL_MESSAGE, "You are not yet eligible for today's reward." +
                    " Log some more data and get going!");
            compensationMessages.put(REMAINING_TASKS, String.valueOf(remainingDataCount) +
                    " reports and end of day diary");
            compensationMessages.put(TOTAL_RESPONSE_COUNT, String.valueOf(relevantDataCount));
            compensationMessages.put(REWARD_COUNT, String.valueOf(reward));
        }
        return compensationMessages;
    }
}

