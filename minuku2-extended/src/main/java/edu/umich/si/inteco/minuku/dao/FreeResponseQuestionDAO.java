package edu.umich.si.inteco.minuku.dao;


import edu.umich.si.inteco.minuku.config.Constants;
import edu.umich.si.inteco.minukucore.model.question.FreeResponse;

/**
 * Created by shriti on 7/28/16.
 */
public class FreeResponseQuestionDAO extends AbstractQuestionDAO<FreeResponse> {

    protected String TAG = "FreeResponseQuestionDAO";

    public FreeResponseQuestionDAO() {
        super(FreeResponse.class, Constants.FIREBASE_URL_FREE_RESPONSE);
    }
}
