package edu.umich.si.inteco.minuku_2.question;

import android.content.Context;

import com.github.dkharrat.nexusdialog.FormElementController;
import com.github.dkharrat.nexusdialog.controllers.*;

import java.util.Arrays;
import java.util.LinkedList;

import edu.umich.si.inteco.minuku.manager.QuestionManager;
import edu.umich.si.inteco.minukucore.exception.QuestionNotFoundException;
import edu.umich.si.inteco.minukucore.model.question.FreeResponse;
import edu.umich.si.inteco.minukucore.model.question.MultipleChoice;
import edu.umich.si.inteco.minukucore.model.question.Question;

/**
 * Created by shriti on 7/29/16.
 */
public class QuestionConfig {

    private static QuestionConfig instance;

    //constants for IDs and Strings and options - mcq, likert
    public static final String QUESTION_1_STRING = "Question1";
    public static final String QUESTION_2_STRING = "Question2";
    public static final String QUESTION_3_STRING = "Question3";
    public static final String[] QUESTION_3_VALUES = {"1", "2", "3"};

    public static final FreeResponse QUESTION_1 = new FreeResponse(QUESTION_1_STRING);
    public static final FreeResponse QUESTION_2 = new FreeResponse(QUESTION_2_STRING);
    public static final MultipleChoice QUESTION_3 = new MultipleChoice(QUESTION_3_STRING,
            3,
            QUESTION_3_VALUES);

    public static LinkedList<Question> questionsList = new LinkedList();

    static {
        QUESTION_1.setID(1);
        QUESTION_2.setID(2);
        QUESTION_3.setID(3);

        questionsList.add(QUESTION_1);
        questionsList.add(QUESTION_2);
        questionsList.add(QUESTION_3);
    }

    // Create all Question instances here, make all Strings and instances created with those strings
    // static and final.
    // create a list of MCQ
    // create a list of FreeResponse

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
                QuestionManager.getInstance().register(
                        question,
                        getControllerFor(question,context));

            } catch (QuestionNotFoundException e) {
                e.printStackTrace();
            }
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
                            Arrays.asList(mcq.getChoiceValues()),
                            true /* useItemsAsValues */);
        }
        throw new QuestionNotFoundException();
    }
}
