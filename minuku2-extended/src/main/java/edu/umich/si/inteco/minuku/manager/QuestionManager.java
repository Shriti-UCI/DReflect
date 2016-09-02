package edu.umich.si.inteco.minuku.manager;

import com.github.dkharrat.nexusdialog.FormElementController;

import java.util.HashMap;
import java.util.Map;

import edu.umich.si.inteco.minukucore.model.question.Question;
import edu.umich.si.inteco.minukucore.model.question.Questionnaire;

/**
 * Created by shriti on 7/29/16.
 */
public class QuestionManager {

    protected Map<Question, FormElementController> questionMap;
    protected Map<Integer, Questionnaire> questionnaireMap;
    private static QuestionManager instance;

    private QuestionManager () {
        this.questionMap = new HashMap<>();
        this.questionnaireMap = new HashMap<>();
    }

    public static QuestionManager getInstance() {
        if (instance == null) {
            instance = new QuestionManager();
        }
        return instance;
    }

    public FormElementController getControllerforQuestion(Question question) {
        //get ID for question and return controller for the sames
        return questionMap.get(question);
    }

    public boolean questionExists(Question question) {
        return questionMap.containsKey(question);
    }

    public void registerQuestion(Question question, FormElementController formElementController) {
        questionMap.put(question, formElementController);
    }

    public void registerQuestionnaire(Questionnaire questionnaire, int id) {
        questionnaireMap.put(id, questionnaire);
    }

    public Map getQuestionFormControllerMap() {
        return this.questionMap;
    }

    public Questionnaire getQuestionnaireForID(int id) {
        return questionnaireMap.get(id);
    }

}
