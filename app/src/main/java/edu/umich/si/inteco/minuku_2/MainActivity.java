package edu.umich.si.inteco.minuku_2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import edu.umich.si.inteco.minuku.config.Constants;
import edu.umich.si.inteco.minuku.dao.UserSubmissionStatsDAO;
import edu.umich.si.inteco.minuku.event.DecrementLoadingProcessCountEvent;
import edu.umich.si.inteco.minuku.event.IncrementLoadingProcessCountEvent;
import edu.umich.si.inteco.minuku.logger.Log;
import edu.umich.si.inteco.minuku.manager.MinukuDAOManager;
import edu.umich.si.inteco.minuku.manager.MinukuNotificationManager;
import edu.umich.si.inteco.minuku.model.UserSubmissionStats;
import edu.umich.si.inteco.minuku_2.manager.InstanceManager;
import edu.umich.si.inteco.minuku_2.preferences.ApplicationConstants;
import edu.umich.si.inteco.minuku_2.service.BackgroundService;
import edu.umich.si.inteco.minuku_2.view.helper.CustomGridAdapter;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";
    private TextView compensationMessage;
    private AtomicInteger loadingProcessCount = new AtomicInteger(0);
    private ProgressDialog loadingProgressDialog;

    //private UserSubmissionStats mUserSubmissionStats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        compensationMessage = (TextView) findViewById(R.id.compensation_message);

        initializeActionList();
        startService(new Intent(getBaseContext(), BackgroundService.class));
        startService(new Intent(getBaseContext(), MinukuNotificationManager.class));

        UUID dummyUUID = UUID.randomUUID();
        EventBus.getDefault().register(this);

        if(!InstanceManager.isInitialized()) {
            InstanceManager.getInstance(getApplicationContext());

            loadingProgressDialog = ProgressDialog.show(MainActivity.this,
                    "Loading data", "Fetching information",true);
        }
    }

    //populate the list of elements in home screen
    private void initializeActionList() {
        //final ListView listview = (ListView) findViewById(R.id.list_view_actions_list);
        //final GridView gridView = (GridView) findViewById(R.id.list_view_actions_list);
        final GridView gridView;
        // the action object is the model behind the list that is shown on the main screen.
        /**final ActionObject[] array = {
                new ActionObject("Take Glucose Reading Picture", "A", R.drawable.glucose_reading),
                new ActionObject("Take Insulin Shot Picture", "A", R.drawable.insulin_shot),
                new ActionObject("Take Food Pitcure", "A", R.drawable.food),
                new ActionObject("Take Other Pictures", "A", R.drawable.camera),
                new ActionObject("Record Your Mood", "A", R.drawable.mood),
                new ActionObject("Upload Screenshot", "A", R.drawable.camera),
                new ActionObject("Take Notes","A", R.drawable.blue_circle)
                //new ActionObject("Answer some questions", "A", R.drawable.blue_circle)
        };**/

        String[] web = {
                "Record Glucose Reading",
                "Record Insulin Shot   ",
                "Record Food           ",
                "Record Mood           ",
                "Record Notes          ",
                "Upload Screenshots    ",
                "Record Other Pictures ",
                "Check Credit          "
        };

        int[] imageID = {
                R.drawable.glucometer_color,
                R.drawable.vaccine_color,
                R.drawable.food_plate_color,
                R.drawable.smiley_color,
                R.drawable.note_color,
                R.drawable.picture_color,
                R.drawable.photo_camera_color,
                R.drawable.money_bag_color
        };

        final CustomGridAdapter customGridAdapter = new CustomGridAdapter(MainActivity.this,
                web, imageID);
        gridView = (GridView) findViewById(R.id.list_view_actions_list);
        gridView.setAdapter(customGridAdapter);
        // The adapter takes the action object array and converts it into a view that can be
        // rendered as a list, one item at a time.
        /**final StableArrayAdapter adapter = new StableArrayAdapter(
                this.getApplicationContext(), array);
        listview.setAdapter(adapter);

         listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle extras = new Bundle();
                // The position here corresponds to position of objects in the array passed above.
                switch (position) {
                    case 0:
                        Intent addGlucoseReadingPhotoIntent = new Intent(MainActivity.this, AnnotatedImageDataRecordActivity.class);
                        extras.putString("photoType", ApplicationConstants.IMAGE_TYPE_GLUCOSE_READIMG);
                        addGlucoseReadingPhotoIntent.putExtras(extras);
                        startActivity(addGlucoseReadingPhotoIntent);
                        break;
                    case 1:
                        Intent addInsulinShotPhotoIntent = new Intent(MainActivity.this, AnnotatedImageDataRecordActivity.class);
                        extras.putString("photoType", ApplicationConstants.IMAGE_TYPE_INSULIN_SHOT);
                        addInsulinShotPhotoIntent.putExtras(extras);
                        startActivity(addInsulinShotPhotoIntent);
                        break;
                    case 2:
                        Intent addFoodPhotoIntent = new Intent(MainActivity.this, AnnotatedImageDataRecordActivity.class);
                        extras.putString("photoType", ApplicationConstants.IMAGE_TYPE_FOOD);
                        addFoodPhotoIntent.putExtras(extras);
                        startActivity(addFoodPhotoIntent);
                        break;
                    case 3:
                        Intent addOtherPhotoIntent = new Intent(MainActivity.this, AnnotatedImageDataRecordActivity.class);
                        extras.putString("photoType", ApplicationConstants.IMAGE_TYPE_OTHERS);
                        addOtherPhotoIntent.putExtras(extras);
                        startActivity(addOtherPhotoIntent);
                        break;
                    case 4:
                        Intent addMoodIntent = new Intent(MainActivity.this,
                                MoodDataRecordActivity.class);
                        startActivity(addMoodIntent);
                        break;
                    case 5:
                        Intent uploadScreenshotIntent = new Intent(MainActivity.this, UploadScreenshotActivity.class);
                        extras.putString("photoType", ApplicationConstants.IMAGE_TYPE_GALLERY_UPLOAD);
                        uploadScreenshotIntent.putExtras(extras);
                        startActivity(uploadScreenshotIntent);
                        break;
                    case 6:
                        Intent noteEntryIntent = new Intent(MainActivity.this,
                                NoteEntryActivity.class);
                        startActivity(noteEntryIntent);
                        break;
                    default:
                        showToast("Clicked unknown");
                        break;
                }
            }
        });**/
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle extras = new Bundle();
                // The position here corresponds to position of objects in the array passed above.
                switch (position) {
                    case 0:
                        Intent addGlucoseReadingPhotoIntent = new Intent(MainActivity.this, AnnotatedImageDataRecordActivity.class);
                        extras.putString("photoType", ApplicationConstants.IMAGE_TYPE_GLUCOSE_READIMG);
                        addGlucoseReadingPhotoIntent.putExtras(extras);
                        startActivity(addGlucoseReadingPhotoIntent);
                        break;
                    case 1:
                        Intent addInsulinShotPhotoIntent = new Intent(MainActivity.this, AnnotatedImageDataRecordActivity.class);
                        extras.putString("photoType", ApplicationConstants.IMAGE_TYPE_INSULIN_SHOT);
                        addInsulinShotPhotoIntent.putExtras(extras);
                        startActivity(addInsulinShotPhotoIntent);
                        break;
                    case 2:
                        Intent addFoodPhotoIntent = new Intent(MainActivity.this, AnnotatedImageDataRecordActivity.class);
                        extras.putString("photoType", ApplicationConstants.IMAGE_TYPE_FOOD);
                        addFoodPhotoIntent.putExtras(extras);
                        startActivity(addFoodPhotoIntent);
                        break;
                    case 6:
                        Intent addOtherPhotoIntent = new Intent(MainActivity.this, AnnotatedImageDataRecordActivity.class);
                        extras.putString("photoType", ApplicationConstants.IMAGE_TYPE_OTHERS);
                        addOtherPhotoIntent.putExtras(extras);
                        startActivity(addOtherPhotoIntent);
                        break;
                    case 3:
                        Intent addMoodIntent = new Intent(MainActivity.this,
                                MoodDataRecordActivity.class);
                        startActivity(addMoodIntent);
                        break;
                    case 5:
                        Intent uploadScreenshotIntent = new Intent(MainActivity.this, UploadScreenshotActivity.class);
                        extras.putString("photoType", ApplicationConstants.IMAGE_TYPE_GALLERY_UPLOAD);
                        uploadScreenshotIntent.putExtras(extras);
                        startActivity(uploadScreenshotIntent);
                        break;
                    case 4:
                        Intent noteEntryIntent = new Intent(MainActivity.this,
                                NoteEntryActivity.class);
                        startActivity(noteEntryIntent);
                        break;
                    case 7:
                        //money bag
                        Intent displayCreditIntent = new Intent(MainActivity.this, DisplayCreditActivity.class);
                        startActivity(displayCreditIntent);
                        break;
                    default:
                        showToast("Clicked unknown");
                        break;
                }
            }
        });

    }

    protected void showToast(String aText) {
        Toast.makeText(this, aText, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.action_settings) {
            showSettingsScreen();
            return true;
        }
        if(id == R.id.configure_location) {
            Intent configureLocations = new Intent(MainActivity.this,
                    LocationConfigurationActivity.class);
            startActivity(configureLocations);
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSharedPref.writePreference(Constants.CAN_SHOW_NOTIFICATION, Constants.NO);
    }


    private void showSettingsScreen() {
        //showToast("Clicked settings");
        Intent preferencesIntent = new Intent(this, SettingsActivity.class);
        startActivity(preferencesIntent);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    @Subscribe
    public void populateCompensationMessage(UserSubmissionStats userSubmissionStats) {
        Log.d(TAG, "Attempting to update compesnation message");
        if(userSubmissionStats != null && isEligibleForReward(userSubmissionStats)) {
            Log.d(TAG, "populating the compensation message");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    compensationMessage.setText("You are now eligible for today's reward!");
                    compensationMessage.setVisibility(View.VISIBLE);
                }});
        } else {
                compensationMessage.setText("");
                compensationMessage.setVisibility(View.INVISIBLE);
            }
    }

    @Subscribe
    public void incrementLoadingProcessCount(IncrementLoadingProcessCountEvent event) {
        Integer loadingCount = loadingProcessCount.incrementAndGet();
        Log.d(TAG, "Incrementing loading processes count: " + loadingCount);
    }

    @Subscribe
    public void decrementLoadingProcessCountEvent(DecrementLoadingProcessCountEvent event) {
        Integer loadingCount = loadingProcessCount.decrementAndGet();
        Log.d(TAG, "Decrementing loading processes count: " + loadingCount);
        maybeRemoveProgressDialog(loadingCount);
    }

    private void maybeRemoveProgressDialog(Integer loadingCount) {
        if(loadingCount <= 0) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loadingProgressDialog.hide();
                }
            });

        }
    }

    @Subscribe
    public boolean isEligibleForReward(UserSubmissionStats userSubmissionStats) {
        return getRewardRelevantSubmissionCount(userSubmissionStats) >= ApplicationConstants.MIN_REPORTS_TO_GET_REWARD;
    }
}
