package edu.umich.si.inteco.minuku_2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.UUID;

import edu.umich.si.inteco.minuku.dao.AnnotatedImageDataRecordDAO;
import edu.umich.si.inteco.minuku.dao.FreeResponseQuestionDAO;
import edu.umich.si.inteco.minuku.dao.LocationDataRecordDAO;
import edu.umich.si.inteco.minuku.dao.MoodDataRecordDAO;
import edu.umich.si.inteco.minuku.dao.MultipleChoiceQuestionDAO;
import edu.umich.si.inteco.minuku.dao.NoteDataRecordDAO;
import edu.umich.si.inteco.minuku.dao.SemanticLocationDataRecordDAO;
import edu.umich.si.inteco.minuku.manager.MinukuDAOManager;
import edu.umich.si.inteco.minuku.manager.MinukuNotificationManager;
import edu.umich.si.inteco.minuku.manager.MinukuSituationManager;
import edu.umich.si.inteco.minuku.model.AnnotatedImageDataRecord;
import edu.umich.si.inteco.minuku.model.LocationDataRecord;
import edu.umich.si.inteco.minuku.model.MoodDataRecord;
import edu.umich.si.inteco.minuku.model.NoteDataRecord;
import edu.umich.si.inteco.minuku.model.SemanticLocationDataRecord;
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
import edu.umich.si.inteco.minuku_2.model.FoodImage;
import edu.umich.si.inteco.minuku_2.model.GlucoseReadingImage;
import edu.umich.si.inteco.minuku_2.model.InsulinAdminImage;
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
import edu.umich.si.inteco.minukucore.model.question.FreeResponse;
import edu.umich.si.inteco.minukucore.model.question.MultipleChoice;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeActionList();
        startService(new Intent(getBaseContext(), BackgroundService.class));
        startService(new Intent(getBaseContext(), MinukuNotificationManager.class));

        UUID dummyUUID = UUID.randomUUID();

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
                new ActionObject("Take Notes","A", R.drawable.blue_circle),
                new ActionObject("Configure locations", "A", R.drawable.ic_location_on_black_24dp)
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
                        extras.putString("photoType", "GLUCOSE_READING");
                        addGlucoseReadingPhotoIntent.putExtras(extras);
                        startActivity(addGlucoseReadingPhotoIntent);
                        break;
                    case 1:
                        Intent addInsulinShotPhotoIntent = new Intent(MainActivity.this, AnnotatedImageDataRecordActivity.class);
                        extras.putString("photoType", "INSULIN_SHOT");
                        addInsulinShotPhotoIntent.putExtras(extras);
                        startActivity(addInsulinShotPhotoIntent);
                        break;
                    case 2:
                        Intent addFoodPhotoIntent = new Intent(MainActivity.this, AnnotatedImageDataRecordActivity.class);
                        extras.putString("photoType", "FOOD");
                        addFoodPhotoIntent.putExtras(extras);
                        startActivity(addFoodPhotoIntent);
                        break;
                    case 3:
                        Intent addOtherPhotoIntent = new Intent(MainActivity.this, AnnotatedImageDataRecordActivity.class);
                        extras.putString("photoType", "OTHER");
                        addOtherPhotoIntent.putExtras(extras);
                        startActivity(addOtherPhotoIntent);
                        break;
                    case 4:
                        Intent addMoodIntent = new Intent(MainActivity.this, MoodDataRecordActivity.class);
                        startActivity(addMoodIntent);
                        break;
                    case 5:
                        Intent uploadScreenshotIntent = new Intent(MainActivity.this, UploadScreenshotActivity.class);
                        extras.putString("photoType", "GALLERY_PHOTO");
                        uploadScreenshotIntent.putExtras(extras);
                        startActivity(uploadScreenshotIntent);
                        break;
                    case 6:
                        Intent noteEntryIntent = new Intent(MainActivity.this, NoteEntryActivity.class);
                        startActivity(noteEntryIntent);
                        break;
                    case 7:
                        Intent configureLocations = new Intent(MainActivity.this, LocationConfigurationActivity.class);
                        startActivity(configureLocations);
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
        return super.onOptionsItemSelected(menuItem);
    }

    private void showSettingsScreen() {
        //showToast("Clicked settings");
        Intent preferencesIntent = new Intent(this, SettingsActivity.class);
        startActivity(preferencesIntent);
    }

}
