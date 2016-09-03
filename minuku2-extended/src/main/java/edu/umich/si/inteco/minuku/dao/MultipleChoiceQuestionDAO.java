package edu.umich.si.inteco.minuku.dao;

import edu.umich.si.inteco.minuku.config.Constants;
import edu.umich.si.inteco.minukucore.model.question.MultipleChoice;

/**
 * Created by shriti on 7/31/16.
 */
public class MultipleChoiceQuestionDAO extends AbstractQuestionDAO<MultipleChoice> {

    protected String TAG = "MultipleChoiceQuestionDAO";

    public MultipleChoiceQuestionDAO() {
        super(MultipleChoice.class, Constants.FIREBASE_URL_MCQ);
    }
}
