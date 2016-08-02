package edu.umich.si.inteco.minuku_2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import edu.umich.si.inteco.minuku.dao.SemanticLocationDataRecordDAO;
import edu.umich.si.inteco.minuku.manager.MinukuDAOManager;
import edu.umich.si.inteco.minuku.manager.MinukuSituationManager;
import edu.umich.si.inteco.minuku.model.AnnotatedImageDataRecord;
import edu.umich.si.inteco.minuku.model.LocationDataRecord;
import edu.umich.si.inteco.minuku.model.MoodDataRecord;
import edu.umich.si.inteco.minuku.model.SemanticLocationDataRecord;
import edu.umich.si.inteco.minuku.streamgenerator.AnnotatedImageStreamGenerator;
import edu.umich.si.inteco.minuku.streamgenerator.FreeResponseQuestionStreamGenerator;
import edu.umich.si.inteco.minuku.streamgenerator.LocationStreamGenerator;
import edu.umich.si.inteco.minuku.streamgenerator.MoodStreamGenerator;
import edu.umich.si.inteco.minuku.streamgenerator.MultipleChoiceQuestionStreamGenerator;
import edu.umich.si.inteco.minuku.streamgenerator.SemanticLocationStreamGenerator;
import edu.umich.si.inteco.minuku_2.action.MissedGlucoseReadingAction;
import edu.umich.si.inteco.minuku_2.action.MissedInsulinAdminAction;
import edu.umich.si.inteco.minuku_2.action.MoodAnnotationExpectedAction;
import edu.umich.si.inteco.minuku_2.action.MoodDataExpectedAction;
import edu.umich.si.inteco.minuku_2.dao.FoodImageDAO;
import edu.umich.si.inteco.minuku_2.dao.GlucoseReadingImageDAO;
import edu.umich.si.inteco.minuku_2.dao.InsulinAdminImageDAO;
import edu.umich.si.inteco.minuku_2.event.MissedInsulinAdminEvent;
import edu.umich.si.inteco.minuku_2.model.FoodImage;
import edu.umich.si.inteco.minuku_2.model.GlucoseReadingImage;
import edu.umich.si.inteco.minuku_2.model.InsulinAdminImage;
import edu.umich.si.inteco.minuku_2.service.BackgroundService;
import edu.umich.si.inteco.minuku_2.situation.MissedGlucoseReadingSituation;
import edu.umich.si.inteco.minuku_2.situation.MissedInsulinAdminSituation;
import edu.umich.si.inteco.minuku_2.situation.MoodAnnotationExpectedSituation;
import edu.umich.si.inteco.minuku_2.situation.MoodDataExpectedSituation;
import edu.umich.si.inteco.minuku_2.stream.FoodImageStream;
import edu.umich.si.inteco.minuku_2.streamgenerator.FoodImageStreamGenerator;
import edu.umich.si.inteco.minuku_2.streamgenerator.GlucoseReadingImageStreamGenerator;
import edu.umich.si.inteco.minuku_2.streamgenerator.InsulinAdminImageStreamGenerator;
import edu.umich.si.inteco.minuku_2.view.helper.ActionObject;
import edu.umich.si.inteco.minuku_2.view.helper.StableArrayAdapter;
import edu.umich.si.inteco.minukucore.model.question.FreeResponse;
import edu.umich.si.inteco.minukucore.model.question.MultipleChoice;
import edu.umich.si.inteco.minukucore.user.User;

public class MainActivity extends BaseActivity {

    private static final String LOG_TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeActionList();
        startService(new Intent(getBaseContext(), BackgroundService.class));

        User myUser = mSharedPref.getUser();
        if(myUser == null) {
            Log.e(LOG_TAG, "User is set to null");
        }
        UUID dummyUUID = UUID.randomUUID();

        // DAO initialization stuff
        MinukuDAOManager daoManager = MinukuDAOManager.getInstance();
        //For location
        LocationDataRecordDAO locationDataRecordDAO = new LocationDataRecordDAO();
        locationDataRecordDAO.setDevice(myUser, dummyUUID);
        daoManager.registerDaoFor(LocationDataRecord.class, locationDataRecordDAO);

        //For image
        AnnotatedImageDataRecordDAO annotatedImageDataRecordDAO = new AnnotatedImageDataRecordDAO();
        annotatedImageDataRecordDAO.setDevice(myUser, dummyUUID);
        daoManager.registerDaoFor(AnnotatedImageDataRecord.class, annotatedImageDataRecordDAO);

        //For mood
        MoodDataRecordDAO moodDataRecordDAO = new MoodDataRecordDAO();
        moodDataRecordDAO.setDevice(myUser, dummyUUID);
        daoManager.registerDaoFor(MoodDataRecord.class, moodDataRecordDAO);

        // SemanticLocation
        SemanticLocationDataRecordDAO semanticLocationDataRecordDAO = new SemanticLocationDataRecordDAO();
        semanticLocationDataRecordDAO.setDevice(myUser, dummyUUID);
        daoManager.registerDaoFor(SemanticLocationDataRecord.class, semanticLocationDataRecordDAO);

        //Free Response questions
        FreeResponseQuestionDAO freeResponseQuestionDAO = new FreeResponseQuestionDAO();
        freeResponseQuestionDAO.setDevice(myUser, dummyUUID);
        daoManager.registerDaoFor(FreeResponse.class, freeResponseQuestionDAO);

        //Questionnaire DAO
        MultipleChoiceQuestionDAO multipleChoiceQuestionDAO = new MultipleChoiceQuestionDAO();
        multipleChoiceQuestionDAO.setDevice(myUser, dummyUUID);
        daoManager.registerDaoFor(MultipleChoice.class, multipleChoiceQuestionDAO);

        //App speicif Image DAOs
        GlucoseReadingImageDAO glucoseReadingImageDAO = new GlucoseReadingImageDAO();
        glucoseReadingImageDAO.setDevice(myUser, dummyUUID);
        daoManager.registerDaoFor(GlucoseReadingImage.class, glucoseReadingImageDAO);

        InsulinAdminImageDAO insulinAdminImageDAO = new InsulinAdminImageDAO();
        insulinAdminImageDAO.setDevice(myUser, dummyUUID);
        daoManager.registerDaoFor(InsulinAdminImage.class, insulinAdminImageDAO);

        FoodImageDAO foodImageDAO = new FoodImageDAO();
        foodImageDAO.setDevice(myUser, dummyUUID);
        daoManager.registerDaoFor(FoodImage.class, foodImageDAO);

        // Create corresponding stream generators. Only to be created once in Main Activity
        //creating a new stream registers it with the stream manager
        LocationStreamGenerator locationStreamGenerator =
                new LocationStreamGenerator(getApplicationContext());
        AnnotatedImageStreamGenerator annotatedImageStreamGenerator =
                new AnnotatedImageStreamGenerator(getApplicationContext(),
                        AnnotatedImageDataRecord.class);
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



        // All situations must be registered AFTER the stream generators are registers.
        MinukuSituationManager situationManager = MinukuSituationManager.getInstance();
        MoodDataExpectedSituation moodDataExpectedSituation = new MoodDataExpectedSituation();
        MoodDataExpectedAction moodDataExpectedAction = new MoodDataExpectedAction();

        MissedGlucoseReadingSituation missedGlucoseReadingSituation = new MissedGlucoseReadingSituation();
        MissedGlucoseReadingAction missedGlucoseReadingAction = new MissedGlucoseReadingAction();

        MissedInsulinAdminSituation missedInsulinAdminSituation = new MissedInsulinAdminSituation();
        MissedInsulinAdminAction missedInsulinAdminAction = new MissedInsulinAdminAction();

        MoodAnnotationExpectedSituation moodAnnotationExpectedSituation = new MoodAnnotationExpectedSituation();
        MoodAnnotationExpectedAction moodAnnotationExpectedAction = new MoodAnnotationExpectedAction();

    }

    //populate the list of elements in home screen
    private void initializeActionList() {
        final ListView listview = (ListView) findViewById(R.id.list_view_actions_list);

        // the action object is the model behind the list that is shown on the main screen.
        final ActionObject[] array = {
                new ActionObject("Take Glucose Reading Pictures", "A", R.drawable.camera),
                new ActionObject("Record Your Mood", "A", R.drawable.mood),
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
                // The position here corresponds to position of objects in the array passed above.
                switch (position) {
                    case 0:
                        Intent addPhotoIntent = new Intent(MainActivity.this, AnnotatedImageDataRecordActivity.class);
                        startActivity(addPhotoIntent);
                        break;
                    case 1:
                        Intent addMoodIntent = new Intent(MainActivity.this, MoodDataRecordActivity.class);
                        startActivity(addMoodIntent);
                    //case 2:
                        //Intent answerQuestionsIntent = new Intent(MainActivity.this, QuestionnaireActivity.class);
                        //startActivity(answerQuestionsIntent);
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
