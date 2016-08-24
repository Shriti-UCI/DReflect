package edu.umich.si.inteco.minuku_2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import edu.umich.si.inteco.minuku.config.Constants;
import edu.umich.si.inteco.minuku.dao.AnnotatedImageDataRecordDAO;
import edu.umich.si.inteco.minuku.dao.FreeResponseQuestionDAO;
import edu.umich.si.inteco.minuku.dao.LocationDataRecordDAO;
import edu.umich.si.inteco.minuku.dao.MoodDataRecordDAO;
import edu.umich.si.inteco.minuku.dao.MultipleChoiceQuestionDAO;
import edu.umich.si.inteco.minuku.dao.NoteDataRecordDAO;
import edu.umich.si.inteco.minuku.dao.NotificationDAO;
import edu.umich.si.inteco.minuku.dao.SemanticLocationDataRecordDAO;
import edu.umich.si.inteco.minuku.dao.UserSubmissionStatsDAO;
import edu.umich.si.inteco.minuku.manager.MinukuDAOManager;
import edu.umich.si.inteco.minuku.manager.MinukuNotificationManager;
import edu.umich.si.inteco.minuku.manager.MinukuSituationManager;
import edu.umich.si.inteco.minuku.model.AnnotatedImageDataRecord;
import edu.umich.si.inteco.minuku.model.LocationDataRecord;
import edu.umich.si.inteco.minuku.model.MoodDataRecord;
import edu.umich.si.inteco.minuku.model.NoteDataRecord;
import edu.umich.si.inteco.minuku.model.SemanticLocationDataRecord;
import edu.umich.si.inteco.minuku.model.UserSubmissionStats;
import edu.umich.si.inteco.minuku.streamgenerator.AnnotatedImageStreamGenerator;
import edu.umich.si.inteco.minuku.streamgenerator.FreeResponseQuestionStreamGenerator;
import edu.umich.si.inteco.minuku.streamgenerator.LocationStreamGenerator;
import edu.umich.si.inteco.minuku.streamgenerator.MoodStreamGenerator;
import edu.umich.si.inteco.minuku.streamgenerator.MultipleChoiceQuestionStreamGenerator;
import edu.umich.si.inteco.minuku.streamgenerator.NoteStreamGenerator;
import edu.umich.si.inteco.minuku.streamgenerator.SemanticLocationStreamGenerator;
import edu.umich.si.inteco.minuku_2.action.MissedFoodAction;
import edu.umich.si.inteco.minuku_2.action.MissedGlucoseReadingAction;
import edu.umich.si.inteco.minuku_2.action.MissedInsulinAdminAction;
import edu.umich.si.inteco.minuku_2.action.MoodAnnotationExpectedAction;
import edu.umich.si.inteco.minuku_2.action.MoodDataExpectedAction;
import edu.umich.si.inteco.minuku_2.dao.FoodImageDAO;
import edu.umich.si.inteco.minuku_2.dao.GlucoseReadingImageDAO;
import edu.umich.si.inteco.minuku_2.dao.InsulinAdminImageDAO;
import edu.umich.si.inteco.minuku.event.DecrementLoadingProcessCountEvent;
import edu.umich.si.inteco.minuku.event.IncrementLoadingProcessCountEvent;
import edu.umich.si.inteco.minuku_2.model.FoodImage;
import edu.umich.si.inteco.minuku_2.model.GlucoseReadingImage;
import edu.umich.si.inteco.minuku_2.model.InsulinAdminImage;
import edu.umich.si.inteco.minuku_2.preferences.ApplicationConstants;
import edu.umich.si.inteco.minuku_2.question.QuestionConfig;
import edu.umich.si.inteco.minuku_2.service.BackgroundService;
import edu.umich.si.inteco.minuku_2.situation.MissedFoodImageSituation;
import edu.umich.si.inteco.minuku_2.situation.MissedGlucoseReadingSituation;
import edu.umich.si.inteco.minuku_2.situation.MissedInsulinAdminSituation;
import edu.umich.si.inteco.minuku_2.situation.MoodAnnotationExpectedSituation;
import edu.umich.si.inteco.minuku_2.situation.MoodDataExpectedSituation;
import edu.umich.si.inteco.minuku_2.streamgenerator.FoodImageStreamGenerator;
import edu.umich.si.inteco.minuku_2.streamgenerator.GlucoseReadingImageStreamGenerator;
import edu.umich.si.inteco.minuku_2.streamgenerator.InsulinAdminImageStreamGenerator;
import edu.umich.si.inteco.minuku_2.view.helper.ActionObject;
import edu.umich.si.inteco.minuku_2.view.helper.StableArrayAdapter;
import edu.umich.si.inteco.minukucore.event.ShowNotificationEvent;
import edu.umich.si.inteco.minukucore.model.question.FreeResponse;
import edu.umich.si.inteco.minukucore.model.question.MultipleChoice;

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

        loadingProgressDialog = ProgressDialog.show(MainActivity.this,
                "Loading data", "Fetching information",true);

        // DAO initialization stuff
        MinukuDAOManager daoManager = MinukuDAOManager.getInstance();
        //For location
        LocationDataRecordDAO locationDataRecordDAO = new LocationDataRecordDAO();
        daoManager.registerDaoFor(LocationDataRecord.class, locationDataRecordDAO);

        //For mood
        MoodDataRecordDAO moodDataRecordDAO = new MoodDataRecordDAO();
        daoManager.registerDaoFor(MoodDataRecord.class, moodDataRecordDAO);

        // SemanticLocation
        SemanticLocationDataRecordDAO semanticLocationDataRecordDAO = new SemanticLocationDataRecordDAO();
        daoManager.registerDaoFor(SemanticLocationDataRecord.class, semanticLocationDataRecordDAO);

        //Free Response questions
        FreeResponseQuestionDAO freeResponseQuestionDAO = new FreeResponseQuestionDAO();
        daoManager.registerDaoFor(FreeResponse.class, freeResponseQuestionDAO);

        //Questionnaire DAO
        MultipleChoiceQuestionDAO multipleChoiceQuestionDAO = new MultipleChoiceQuestionDAO();
        daoManager.registerDaoFor(MultipleChoice.class, multipleChoiceQuestionDAO);

        //note entry DAO
        NoteDataRecordDAO noteDataRecordDAO = new NoteDataRecordDAO();
        daoManager.registerDaoFor(NoteDataRecord.class, noteDataRecordDAO);

        //App speicif Image DAOs
        GlucoseReadingImageDAO glucoseReadingImageDAO = new GlucoseReadingImageDAO();
        daoManager.registerDaoFor(GlucoseReadingImage.class, glucoseReadingImageDAO);

        InsulinAdminImageDAO insulinAdminImageDAO = new InsulinAdminImageDAO();
        daoManager.registerDaoFor(InsulinAdminImage.class, insulinAdminImageDAO);

        FoodImageDAO foodImageDAO = new FoodImageDAO();
        daoManager.registerDaoFor(FoodImage.class, foodImageDAO);

        //For other images
        AnnotatedImageDataRecordDAO annotatedImageDataRecordDAO = new AnnotatedImageDataRecordDAO(
                AnnotatedImageDataRecord.class);
        daoManager.registerDaoFor(AnnotatedImageDataRecord.class, annotatedImageDataRecordDAO);

        //Notification DAO
        NotificationDAO notificationDAO = new NotificationDAO();
        daoManager.registerDaoFor(ShowNotificationEvent.class, notificationDAO);

        //UserSubmissionStats DAO
        UserSubmissionStatsDAO userSubmissionStatsDAO = new UserSubmissionStatsDAO();
        daoManager.registerDaoFor(UserSubmissionStats.class, userSubmissionStatsDAO);

        // Create corresponding stream generators. Only to be created once in Main Activity
        //creating a new stream registers it with the stream manager
        LocationStreamGenerator locationStreamGenerator =
                new LocationStreamGenerator(getApplicationContext());
        MoodStreamGenerator moodStreamGenerator =
                new MoodStreamGenerator(getApplicationContext());
        SemanticLocationStreamGenerator semanticLocationStreamGenerator =
                new SemanticLocationStreamGenerator(getApplicationContext());
        FreeResponseQuestionStreamGenerator freeResponseQuestionStreamGenerator =
                new FreeResponseQuestionStreamGenerator(getApplicationContext());
        MultipleChoiceQuestionStreamGenerator multipleChoiceQuestionStreamGenerator =
                new MultipleChoiceQuestionStreamGenerator(getApplicationContext());

        GlucoseReadingImageStreamGenerator glucoseReadingImageStreamGenerator =
                new GlucoseReadingImageStreamGenerator(getApplicationContext());
        InsulinAdminImageStreamGenerator insulinAdminImageStreamGenerator =
                new InsulinAdminImageStreamGenerator(getApplicationContext());
        FoodImageStreamGenerator foodImageStreamGenerator =
                new FoodImageStreamGenerator(getApplicationContext());
        AnnotatedImageStreamGenerator annotatedImageStreamGenerator =
                new AnnotatedImageStreamGenerator(getApplicationContext(), AnnotatedImageDataRecord.class);


        NoteStreamGenerator noteStreamGenerator =
                new NoteStreamGenerator(getApplicationContext());

        // All situations must be registered AFTER the stream generators are registers.
        MinukuSituationManager situationManager = MinukuSituationManager.getInstance();

        MoodDataExpectedSituation moodDataExpectedSituation = new MoodDataExpectedSituation();
        MoodDataExpectedAction moodDataExpectedAction = new MoodDataExpectedAction();

        MissedGlucoseReadingSituation missedGlucoseReadingSituation = new MissedGlucoseReadingSituation();
        MissedGlucoseReadingAction missedGlucoseReadingAction = new MissedGlucoseReadingAction();

        MissedInsulinAdminSituation missedInsulinAdminSituation = new MissedInsulinAdminSituation();
        MissedInsulinAdminAction missedInsulinAdminAction = new MissedInsulinAdminAction();

        MissedFoodImageSituation missedFoodImageSituation = new MissedFoodImageSituation();
        MissedFoodAction missedFoodAction = new MissedFoodAction();

        MoodAnnotationExpectedSituation moodAnnotationExpectedSituation = new MoodAnnotationExpectedSituation();
        MoodAnnotationExpectedAction moodAnnotationExpectedAction = new MoodAnnotationExpectedAction();

        //create questionnaires
        QuestionConfig.getInstance().setUpQuestions(getApplicationContext());
    }

    //populate the list of elements in home screen
    private void initializeActionList() {
        final ListView listview = (ListView) findViewById(R.id.list_view_actions_list);

        // the action object is the model behind the list that is shown on the main screen.
        final ActionObject[] array = {
                new ActionObject("Take Glucose Reading Picture", "A", R.drawable.glucose_reading),
                new ActionObject("Take Insulin Shot Picture", "A", R.drawable.insulin_shot),
                new ActionObject("Take Food Pitcure", "A", R.drawable.food),
                new ActionObject("Take Other Pictures", "A", R.drawable.camera),
                new ActionObject("Record Your Mood", "A", R.drawable.mood),
                new ActionObject("Upload Screenshot", "A", R.drawable.camera),
                new ActionObject("Take Notes","A", R.drawable.blue_circle)
                //new ActionObject("Answer some questions", "A", R.drawable.blue_circle)
        };
        // The adapter takes the action object array and converts it into a view that can be
        // rendered as a list, one item at a time.
        final StableArrayAdapter adapter = new StableArrayAdapter(
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

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                Future<UserSubmissionStats> submissionStatsFuture = ((UserSubmissionStatsDAO)
                        MinukuDAOManager.getInstance().getDaoFor(UserSubmissionStats.class)).get();
                while (!submissionStatsFuture.isDone()) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //
                try {
                    Log.d(TAG, "getting mUserSubmissionStats from future " +
                            submissionStatsFuture.get().getTotalSubmissionCount());
                    UserSubmissionStats userSubmissionStats = submissionStatsFuture.get();
                    if(userSubmissionStats==null)
                        userSubmissionStats = new UserSubmissionStats();
                    gotUserStatsFromDatabase(userSubmissionStats);
                    EventBus.getDefault().post(userSubmissionStats);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Log.d(TAG, "Creating mUserSubmissionStats");
                    gotUserStatsFromDatabase(null);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    Log.d(TAG, "Creating mUserSubmissionStats");
                    gotUserStatsFromDatabase(null);
                }
            }
        });
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

    @Override
    protected void gotUserStatsFromDatabase(UserSubmissionStats userSubmissionStats) {
        super.gotUserStatsFromDatabase(userSubmissionStats);
    }

    @Subscribe
    public void populateCompensationMessage(UserSubmissionStats userSubmissionStats) {
        Log.d(TAG, "Attempting to update compesnation message");
        if(userSubmissionStats != null) {
            Log.d(TAG, "populating the compensation message");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    compensationMessage.setText(getCompensationMessage());
                }});
            Log.d(TAG, getCompensationMessage());
        } else {
            compensationMessage.setText(" ");
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
}
