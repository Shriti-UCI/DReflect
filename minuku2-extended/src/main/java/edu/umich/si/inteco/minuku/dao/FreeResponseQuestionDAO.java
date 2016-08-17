package edu.umich.si.inteco.minuku.dao;

import android.util.Log;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.SettableFuture;
import edu.umich.si.inteco.minuku.config.Constants;
import edu.umich.si.inteco.minuku.config.UserPreferences;
import edu.umich.si.inteco.minuku.model.AnnotatedImageDataRecord;
import edu.umich.si.inteco.minuku.model.LocationDataRecord;
import edu.umich.si.inteco.minukucore.dao.DAO;
import edu.umich.si.inteco.minukucore.dao.DAOException;
import edu.umich.si.inteco.minukucore.model.question.FreeResponse;
import edu.umich.si.inteco.minukucore.user.User;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Future;

/**
 * Created by shriti on 7/28/16.
 */
public class FreeResponseQuestionDAO extends AbstractQuestionDAO<FreeResponse> {

    protected String TAG = "FreeResponseQuestionDAO";

    public FreeResponseQuestionDAO() {
        super(FreeResponse.class, Constants.FIREBASE_URL_FREE_RESPONSE);
    }
}
