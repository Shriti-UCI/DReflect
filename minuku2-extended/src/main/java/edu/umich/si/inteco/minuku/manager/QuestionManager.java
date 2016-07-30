package edu.umich.si.inteco.minuku.manager;

import com.github.dkharrat.nexusdialog.FormElementController;
import com.github.dkharrat.nexusdialog.controllers.*;

import java.util.HashMap;
import java.util.Map;

import edu.umich.si.inteco.minukucore.model.question.Question;

/**
 * Created by shriti on 7/29/16.
 */
public class QuestionManager {

    protected Map<Question, FormElementController> map;
    private static QuestionManager instance;

    private QuestionManager () {
        this.map = new HashMap<>();
    }

    public static QuestionManager getInstance() {
        if (instance == null) {
            instance = new QuestionManager();
        }
        return instance;
    }

    public FormElementController getControllerforQuestion(Question question) {
        //get ID for question and return controller for the sames
        return map.get(question);
    }

    public boolean questionExists(Question question) {
        return map.containsKey(question);
    }

    public void register(Question question, FormElementController formElementController) {
        map.put(question, formElementController);
    }

    public Map getQuestionFormControllerMap() {
        return this.map;
    }
}
