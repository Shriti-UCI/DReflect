package edu.umich.si.inteco.minuku_2.manager;

import android.content.Context;

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
import edu.umich.si.inteco.minuku_2.model.FoodImage;
import edu.umich.si.inteco.minuku_2.model.GlucoseReadingImage;
import edu.umich.si.inteco.minuku_2.model.InsulinAdminImage;
import edu.umich.si.inteco.minuku_2.question.QuestionConfig;
import edu.umich.si.inteco.minuku_2.situation.MissedFoodImageSituation;
import edu.umich.si.inteco.minuku_2.situation.MissedGlucoseReadingSituation;
import edu.umich.si.inteco.minuku_2.situation.MissedInsulinAdminSituation;
import edu.umich.si.inteco.minuku_2.situation.MoodAnnotationExpectedSituation;
import edu.umich.si.inteco.minuku_2.situation.MoodDataExpectedSituation;
import edu.umich.si.inteco.minuku_2.streamgenerator.FoodImageStreamGenerator;
import edu.umich.si.inteco.minuku_2.streamgenerator.GlucoseReadingImageStreamGenerator;
import edu.umich.si.inteco.minuku_2.streamgenerator.InsulinAdminImageStreamGenerator;
import edu.umich.si.inteco.minukucore.event.ShowNotificationEvent;
import edu.umich.si.inteco.minukucore.model.question.FreeResponse;
import edu.umich.si.inteco.minukucore.model.question.MultipleChoice;

/**
 * Created by neerajkumar on 8/28/16.
 */
public class InstanceManager {
    private static InstanceManager instance = null;
    private Context mApplicationContext = null;

    private InstanceManager(Context applicationContext) {
        this.mApplicationContext = applicationContext;
        initialize();
    }

    public static InstanceManager getInstance(Context applicationContext) {
        if (instance == null) {
            instance = new InstanceManager(applicationContext);
        }
        return instance;
    }

    public static boolean isInitialized() {
        return instance != null;
    }

    private Context getApplicationContext() {
        return mApplicationContext;
    }

    private void initialize() {
        // Add all initialization code here.
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
}
