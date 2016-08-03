package edu.umich.si.inteco.minukucore.model.question;

import java.util.List;

/**
 * Created by shriti on 8/2/16.
 */
public class Questionnaire {

    private int ID;

    private List<Question> questionnaire;

    public Questionnaire(int ID, List<Question> questionnaire) {
        this.ID = ID;
        this.questionnaire = questionnaire;
    }

    public int getID() {
        return ID;
    }

    public List<Question> getQuestionnaire() {
        return questionnaire;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setQuestionnaire(List<Question> questionnaire) {
        this.questionnaire = questionnaire;
    }
}
