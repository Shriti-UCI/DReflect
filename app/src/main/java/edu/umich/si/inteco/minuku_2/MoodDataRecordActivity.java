package edu.umich.si.inteco.minuku_2;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.text.DecimalFormat;
import java.util.Date;

import edu.umich.si.inteco.minuku.logger.Log;
import edu.umich.si.inteco.minuku.manager.MinukuStreamManager;
import edu.umich.si.inteco.minuku.model.MoodDataRecord;
import edu.umich.si.inteco.minuku_2.view.customview.MoodEntryView;
import edu.umich.si.inteco.minukucore.dao.DAOException;
import edu.umich.si.inteco.minukucore.exception.StreamNotFoundException;

/**
 * Created by shriti on 7/21/16.
 */
public class MoodDataRecordActivity extends BaseActivity {

    private float DEVICE_DENSITY = 0;
    private int DEVICE_WIDTH = 0;

    private float tapX = 0, tapY = 0;
    private float tap_X = 0, tap_Y = 0;
    float firstMoodX = 0, firstMoodY = 0, secondMoodX = 0, secondMoodY = 0;
    float scale;

    MoodDataRecord moodFirst = new MoodDataRecord();
    MoodDataRecord moodSecond = new MoodDataRecord();

    MoodEntryView moodEntryView;
    private Button btnTrends;

    private String TAG ="MoodDataRecordActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        scale = this.getResources().getDisplayMetrics().density;
        setContentView(R.layout.add_mood_activity);

        // Add click listeners for buttons
        ImageView acceptButton = (ImageView) findViewById(R.id.acceptButton);
        ImageView rejectButton = (ImageView) findViewById(R.id.rejectButton);

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptResults();
            }
        });
        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rejectResults();
            }
        });

        getDeviceWidth();
        initUi();
    }

    private void getDeviceWidth() {
        DEVICE_DENSITY = getResources().getDisplayMetrics().density;
        DEVICE_WIDTH = getResources().getDisplayMetrics().widthPixels;
        Log.d(TAG, "**Device Density=" + DEVICE_DENSITY + " " + "DEVICE_WIDTH="
                + DEVICE_WIDTH);
    }

    private void initUi() {
        android.widget.LinearLayout.LayoutParams lp = new android.widget.LinearLayout.LayoutParams(
                DEVICE_WIDTH, DEVICE_WIDTH);
        moodEntryView = (MoodEntryView) findViewById(R.id.graphCustomView);
        moodEntryView.setLayoutParams(lp);
        moodEntryView.setOnTouchListener(moodEntryViewOncliClickListener);
    }

    View.OnTouchListener moodEntryViewOncliClickListener = new View.OnTouchListener()
    {
        @Override
        public boolean onTouch(View v, MotionEvent event)
        {
            if (event.getAction() == MotionEvent.ACTION_DOWN)
            {
                // is first mood selected
                if (event.getX() - 25 * scale < moodFirst.x
                        && event.getX() + 25 * scale > moodFirst.x
                        && event.getY() - 25 * scale < moodFirst.y
                        && event.getY() + 25 * scale > moodFirst.y)
                {
                    Log.d("First Mood", "Selected");
                    moodFirst.isSelected = true;
                    moodSecond.isSelected = false;
                }

                // is second mood selected
                else if (event.getX() - (25 * scale) < moodSecond.x
                        && event.getX() + (25 * scale) > moodSecond.x
                        && event.getY() - (25 * scale) < moodSecond.y
                        && event.getY() + (25 * scale) > moodSecond.y)
                {
                    Log.d("Second Mood", "Selected");
                    moodFirst.isSelected = false;
                    moodSecond.isSelected = true;
                }
                // else no mood is selected
                else
                {
                    Log.d("No Mood", "Selected");
                    moodFirst.isSelected = false;
                    moodSecond.isSelected = false;
                }

                /** Choice first or second mood creating */
                if (!moodFirst.isCreated && moodSecond.isSelected == false)
                {
                    moodFirst.x = event.getX();
                    moodFirst.y = event.getY();
                    moodFirst.isCreated = true;
                    moodFirst.isSelected = true;
                    setFirstMood(moodFirst);
                }

                //showOrHideArrowButton();
            }

            if (event.getAction() == MotionEvent.ACTION_UP)
            {

                float X, Y;
                X = event.getX();
                Y = event.getY();

                float viewWidth = moodEntryView.getWidth();
                float rectSize = viewWidth / 23;
                // float rectSize = viewWidth/23;
                Log.d("X* *,Y***", "==" + X + " , " + Y);

                if (X < ((viewWidth / 2) - 10 * scale) && (Y < (viewWidth / 2)))
                {
                    tapX = (float) ((Math.floor((viewWidth / 2) - X) / rectSize) * -1);// X
                    // should
                    // (-ve)
                    // in
                    // first
                    // quadrant
                    tapY = (float) (Math.floor((viewWidth / 2) - Y) / rectSize);
                    Log.d("Q", "1");
                }
                if ((X > (viewWidth / 2)) && (Y < (viewWidth / 2)))
                {
                    X = X - (viewWidth / 2);

                    /*
                     * tapX =(float)(Math.floor((X/rectSize))); tapY =
                     * (float)(Math.floor(((viewWidth/2)-Y)/rectSize)) ;
                     */
                    tapX = (X / rectSize);
                    tapY = ((viewWidth / 2) - Y) / rectSize;
                    Log.d("Q", "2");
                }
                if ((X > (viewWidth / 2)) && (Y > (viewWidth / 2)))
                {
                    X = X - (viewWidth / 2);
                    Y = Y - (viewWidth / 2);
                    /*
                     * tapX =(float)(Math.floor((X/rectSize))); tapY
                     * =(float)((Math.floor((Y/rectSize)))*-1);//Y should (-ve)
                     * in third quadrant
                     */
                    tapX = (X / rectSize);
                    tapY = (Y / rectSize) * (-1);// Y should (-ve) in third
                    // quadrant

                    Log.d("Q", "3");
                }
                if ((X < (viewWidth / 2)) && (Y > (viewWidth / 2)))
                {
                    /*
                     * tapX =(float)(Math.floor(((X-(viewWidth/2))/rectSize)));
                     * tapY =(float)(Math.floor(((viewWidth/2)-Y)/rectSize));
                     */
                    tapX = (X - (viewWidth / 2)) / rectSize;
                    tapY = ((viewWidth / 2) - Y) / rectSize;
                    Log.d("Q", "4");
                }

                if (tapX > 10.0)
                    tapX = (float) 10.0;
                else if (tapX < -10.0)
                    tapX = (float) -10.0;

                if (tapY > 10.0)
                    tapY = (float) 10.0;
                else if (tapY < -10.0)
                    tapY = (float) -10.0;

                Log.d("TAPX,TAPY", "" + tapX + " , " + tapY);

                DecimalFormat df = new DecimalFormat("#.#");
                String tapx = df.format(tapX);
                String tapy = df.format(tapY);
                tap_X = Float.parseFloat(tapx);
                tap_Y = Float.parseFloat(tapy);
                if (moodFirst.isSelected)
                {
                    moodFirst.x = event.getX();
                    moodFirst.y = event.getY();
                    moodFirst.moodLevel = tap_X;
                    moodFirst.energyLevel = tap_Y;

                }
                if (moodSecond.isSelected)
                {
                    moodSecond.x = event.getX();
                    moodSecond.y = event.getY();
                    moodSecond.moodLevel = tap_X;
                    moodSecond.energyLevel = tap_Y;
                }
                /** If mood drags */
                if (event.getX() < 10 * scale || event.getX() > moodEntryView.getWidth() - 10 * scale
                        || event.getY() < 10 * scale || event.getY() > moodEntryView.getWidth() - 10 * scale)
                {
                    if (moodFirst.isSelected)
                    {
                        moodFirst.x = -50 * scale;
                        moodFirst.y = -50 * scale;
                        setFirstMood(moodFirst);
                    }


                    moodEntryView.invalidate();
                    Log.d("Action", "Out of Bond");
                }
                moodFirst.isSelected = false;
                moodSecond.isSelected = false;
                //showOrHideArrowButton();
            }

            if (event.getAction() == MotionEvent.ACTION_MOVE)
            {
                Log.d("Action", "Move");
                if (moodFirst.isSelected)
                {
                    Log.d("first Mood ", "Moving");
                    moodFirst.x = event.getX() - 10 * scale;
                    moodFirst.y = event.getY() - 10 * scale;
                    setFirstMood(moodFirst);
                }
                else
                {
                    Log.d("NoMood", "X=" + event.getX() + " Y=" + event.getY());
                }

                /** Delete the mood entry */
                if (event.getX() < 20 * scale || event.getX() > moodEntryView.getWidth() - 20 * scale
                        || event.getY() < 20 * scale || event.getY() > moodEntryView.getWidth() - 20 * scale)
                {
                    if (moodFirst.isSelected)
                    {
                        moodFirst.x = -20 * scale;
                        moodFirst.y = -20 * scale;
                        moodFirst.isCreated = false;
                        setFirstMood(moodFirst);
                    }

                    moodEntryView.invalidate();
                    Log.d("Action", "Out of Bond");
                }
            }

            Log.d("TAPX formated ,TAPY", "" + tap_X + " , " + tap_Y);
            return true;
        }
    };

    /**
     * Called each time the user touches anywhere on the mood map.
     * @param mood
     */
    void setFirstMood(MoodDataRecord mood) {
        moodEntryView.setFirstMood(mood.x, mood.y);
    }


    /**
     * This is called when the user pressed "Tick" button on the screen.
     */
    public void acceptResults() {

        // Confirm that a mood was indeed selected.
        if(!moodFirst.isCreated) {
            showToast("Please select a mood on the map above first.");
            return;
        }
        moodFirst.creationTime = new Date().getTime();

        //Create a new mood data record
        MoodDataRecord moodDataRecordToSave = moodFirst;
        try {
            Log.d(TAG, "Saving mood to the stream and the database");
            MinukuStreamManager.getInstance()
                    .getStreamGeneratorFor(MoodDataRecord.class)
                    .offer(moodDataRecordToSave);
            //update reports submitted by user
            mUserSubmissionStats.incrementMoodCount();
            uploadUserSubmissionStats(mUserSubmissionStats);
        } catch (StreamNotFoundException e) {
            e.printStackTrace();
            Log.e(TAG, "The mood stream does not exist on this device");
        } catch (DAOException e) {
            e.printStackTrace();
            Log.e(TAG, "There was an error updating user stats information in the database.");
        }
        showToast("Your mood has been recorded");

        finish();
    }


    /**
     * This is called when the user presses the "X" button the screen.
     */
    public void rejectResults() {
        showToast("Going back to home screen");
        finish();
    }
}
