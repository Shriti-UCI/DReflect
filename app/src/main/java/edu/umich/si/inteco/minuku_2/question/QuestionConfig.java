package edu.umich.si.inteco.minuku_2.question;

import android.content.Context;

import com.github.dkharrat.nexusdialog.FormElementController;
import com.github.dkharrat.nexusdialog.controllers.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import edu.umich.si.inteco.minuku.manager.QuestionManager;
import edu.umich.si.inteco.minukucore.exception.QuestionNotFoundException;
import edu.umich.si.inteco.minukucore.model.question.FreeResponse;
import edu.umich.si.inteco.minukucore.model.question.MultipleChoice;
import edu.umich.si.inteco.minukucore.model.question.Question;
import edu.umich.si.inteco.minukucore.model.question.Questionnaire;

/**
 * Created by shriti on 7/29/16.
 */
public class QuestionConfig {

    private static QuestionConfig instance;

    //constants for IDs and Strings and options - mcq, likert

    //missed report questions v1
    // notification title - we want to hear from you!
    public static final String MISSED_REPORT_QU_1_STRING =
            "How busy are you currently?";
    public static final Question MISSED_REPORT_QU_1 =
            new FreeResponse(MISSED_REPORT_QU_1_STRING);

    public static final String MISSED_REPORT_QU_2_STRING =
            "What have you been doing in the last 2 hours?";
    public static final Question MISSED_REPORT_QU_2 =
            new FreeResponse(MISSED_REPORT_QU_2_STRING);

    //missed report questions v2
    //notification title - we want to hear from you!
    public static final String MISSED_REPORT_QU_3_STRING = "Did you miss to report something?";
    public static final String[] MISSED_REPORT_QU_3_VALUES = {"Yes", "No"};
    public static final Question MISSED_REPORT_QU_3 = new MultipleChoice(MISSED_REPORT_QU_3_STRING,
            2,
            MISSED_REPORT_QU_3_VALUES);

    public static final String MISSED_REPORT_QU_4_STRING = "If yes, please tell us what did you miss?";
    public static final Question MISSED_REPORT_QU_4 = new FreeResponse(MISSED_REPORT_QU_4_STRING);

    //mood change questions
    //notification title - tell us what happened?
    public static final String MOOD_CHANGE_NEG_QU_STRING =
            "You went from being in a good mood to bad mood." +
            "Did something happen that changed your mood?";
    public static final Question MOOD_CHANGE_NEG_QU=
            new FreeResponse(MOOD_CHANGE_NEG_QU_STRING);

    public static final String MOOD_CHANGE_POS_QU_STRING =
            "You went from being in a bad mood to good mood." +
            "Did something happen that changed your mood?";
    public static final Question MOOD_CHANGE_POS_QU=
            new FreeResponse(MOOD_CHANGE_POS_QU_STRING);

    public static LinkedList<Question> questionsList = new LinkedList();
    public static LinkedList<Questionnaire> questionnaires = new LinkedList<>();
    private static List<Question> list1 = new LinkedList<>();
    private static List<Question> list2 = new LinkedList<>();
    private static List<Question> list3 = new LinkedList<>();
    private static List<Question> list4 = new LinkedList<>();

    public static Questionnaire missedReportQuestionnaire_1;
    public static Questionnaire missedReportQuestionnaire_2;
    public static Questionnaire moodChangeNegQuestionnaire;
    public static Questionnaire moodChangePosQuestionnaire;



    static {
        MISSED_REPORT_QU_1.setID(1);
        MISSED_REPORT_QU_2.setID(2);
        MISSED_REPORT_QU_3.setID(3);
        MISSED_REPORT_QU_4.setID(4);
        MOOD_CHANGE_NEG_QU.setID(5);
        MOOD_CHANGE_POS_QU.setID(6);

        questionsList.add(MISSED_REPORT_QU_1);
        questionsList.add(MISSED_REPORT_QU_2);
        questionsList.add(MISSED_REPORT_QU_3);
        questionsList.add(MISSED_REPORT_QU_4);
        questionsList.add(MOOD_CHANGE_NEG_QU);
        questionsList.add(MOOD_CHANGE_POS_QU);

        list1.add(MISSED_REPORT_QU_1);
        list1.add(MISSED_REPORT_QU_2);
        missedReportQuestionnaire_1 = new Questionnaire(1, list1);

        list2.add(MISSED_REPORT_QU_3);
        list2.add(MISSED_REPORT_QU_4);
        missedReportQuestionnaire_2 = new Questionnaire(2, list2);

        list3.add(MOOD_CHANGE_NEG_QU);
        moodChangeNegQuestionnaire = new Questionnaire(3, list3);

        list4.add(MOOD_CHANGE_POS_QU);
        moodChangePosQuestionnaire = new Questionnaire(4, list4);

        questionnaires.add(missedReportQuestionnaire_1);
        questionnaires.add(missedReportQuestionnaire_2);
        questionnaires.add(moodChangeNegQuestionnaire);
        questionnaires.add(moodChangePosQuestionnaire);

    }

    // make this a singleton
    private QuestionConfig() {

    }

    public static QuestionConfig getInstance() {
        if(instance == null)
            instance = new QuestionConfig();
        return instance;
    }
    // getControllerFor(MCQ, AC) throws ACNotSetEx
    // getControllerFor(FreeResponse, AC) throws ACNotSetEx

    // setup(AC)
        // create controllers for all created questions
        // calls register of QuMgr for each question<=>controller pair

    public static void setUpQuestions(Context context){
        for (Question question:QuestionConfig.questionsList) {
            try {
                QuestionManager.getInstance().registerQuestion(
                        question,
                        getControllerFor(question,context));

            } catch (QuestionNotFoundException e) {
                e.printStackTrace();
            }
        }
        for(Questionnaire questionnaire: questionnaires) {
            QuestionManager.getInstance().registerQuestionnaire(questionnaire, questionnaire.getID());
        }
    }

    public static <T extends Question, E extends FormElementController> E getControllerFor(
            T aQuestion, Context aContext) throws QuestionNotFoundException {

        if(aQuestion instanceof FreeResponse) {
            return (E)
                    new EditTextController(aContext,
                            String.valueOf(aQuestion.getID()),
                            aQuestion.getQuestion());
        } else if (aQuestion instanceof MultipleChoice) {
            MultipleChoice mcq = (MultipleChoice) aQuestion;
            return (E)
                    new CheckBoxController(aContext,
                            String.valueOf(aQuestion.getID()),
                            aQuestion.getQuestion(),
                            true /* isRequired */,
                            Arrays.asList(mcq.getLabels()),
                            true /* useItemsAsValues */);
        }
        throw new QuestionNotFoundException();
    }
}
